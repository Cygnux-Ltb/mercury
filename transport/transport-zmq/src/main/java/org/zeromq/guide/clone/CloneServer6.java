package org.zeromq.guide.clone;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZLoop;
import org.zeromq.ZLoop.IZLoopHandler;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.PollItem;
import org.zeromq.ZMQ.Socket;
import org.zeromq.guide.star.BinaryStarReactor;
import org.zeromq.guide.util.KvMsg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

//  Clone server - Model Six
public class CloneServer6 {
	private final ZContext ctx; // Context wrapper
	private Map<String, KvMsg> kvmap; // Key-value store
	private final BinaryStarReactor bStar; // Bstar reactor core
	private long sequence; // How many updates we're at
	private int port; // Main port we're working on
	private int peer; // Main port of our peer
	private final Socket publisher; // Publish updates and hugz
	private final Socket collector; // Collect updates from clients
	private final Socket subscriber; // Get updates from peer
	private final List<KvMsg> pending; // Pending updates from clients
	@SuppressWarnings("unused")
	private boolean primary; // TRUE if we're primary
	private boolean active; // TRUE if we're active
	@SuppressWarnings("unused")
	private boolean passive; // TRUE if we're passive

	private static class Snapshots implements IZLoopHandler {
		@Override
		public int handle(ZLoop loop, PollItem item, Object arg) {
			CloneServer6 srv = (CloneServer6) arg;
			Socket socket = item.getSocket();

			byte[] identity = socket.recv();
			if (identity != null) {
				// Request is in second frame of message
				String request = socket.recvStr();
				String subtree = null;
				if (request.equals("ICANHAZ?")) {
					subtree = socket.recvStr();
				} else
					System.out.print("E: bad request, aborting\n");

				if (subtree != null) {
					// Send state socket to client
					for (Entry<String, KvMsg> entry : srv.kvmap.entrySet()) {
						sendSingle(entry.getValue(), identity, subtree, socket);
					}

					// Now send END message with getSequence number
					System.out.printf("I: sending shapshot=%d\n", srv.sequence);
					socket.send(identity, ZMQ.SNDMORE);
					KvMsg kvmsg = new KvMsg(srv.sequence);
					kvmsg.setKey("KTHXBAI");
					kvmsg.setBody(subtree.getBytes(ZMQ.CHARSET));
					kvmsg.send(socket);
					kvmsg.destroy();
				}
			}
			return 0;
		}
	}

	private static class Collector implements IZLoopHandler {
		@Override
		public int handle(ZLoop loop, PollItem item, Object arg) {
			CloneServer6 srv = (CloneServer6) arg;
			Socket socket = item.getSocket();

			KvMsg msg = KvMsg.recv(socket);
			if (msg != null) {
				if (srv.active) {
					msg.setSequence(++srv.sequence);
					msg.send(srv.publisher);
					int ttl = Integer.parseInt(msg.getProp("ttl"));
					if (ttl > 0)
						msg.setProp("ttl", "%d", System.currentTimeMillis() + ttl * 1000L);
					msg.store(srv.kvmap);
					System.out.printf("I: publishing update=%d\n", srv.sequence);
				} else {
					// If we already got message from active, drop it, else
					// hold on pending list
					if (srv.wasPending(msg))
						msg.destroy();
					else
						srv.pending.add(msg);
				}
			}

			return 0;
		}
	}

	// .split heartbeating
	// We send a HUGZ message once a second to all subscribers so that they
	// can detect if our server dies. They'll then switch over to the backup
	// server, which will become active:
	private static class SendHugz implements IZLoopHandler {
		@Override
		public int handle(ZLoop loop, PollItem item, Object arg) {
			CloneServer6 srv = (CloneServer6) arg;

			KvMsg msg = new KvMsg(srv.sequence);
			msg.setKey("HUGZ");
			msg.setBody(ZMQ.MESSAGE_SEPARATOR);
			msg.send(srv.publisher);
			msg.destroy();

			return 0;
		}
	}

	private static class FlushTTL implements IZLoopHandler {
		@Override
		public int handle(ZLoop loop, PollItem item, Object arg) {
			CloneServer6 srv = (CloneServer6) arg;
			if (srv.kvmap != null) {
				for (KvMsg msg : new ArrayList<KvMsg>(srv.kvmap.values())) {
					srv.flushSingle(msg);
				}
			}
			return 0;
		}
	}

	// .split handling state changes
	// When we switch from passive to active, we apply our pending list so that
	// our kvmap is up-to-date. When we switch to passive, we wipe our kvmap
	// and grab a new snapshot from the active server:
	private static class NewActive implements IZLoopHandler {
		@Override
		public int handle(ZLoop loop, PollItem item, Object arg) {
			CloneServer6 srv = (CloneServer6) arg;

			srv.active = true;
			srv.passive = false;

			// Stop subscribing to updates
			PollItem poller = new PollItem(srv.subscriber, ZMQ.Poller.POLLIN);
			srv.bStar.zloop().removePoller(poller);

			// Apply pending list to own hash table
			for (KvMsg msg : srv.pending) {
				msg.setSequence(++srv.sequence);
				msg.send(srv.publisher);
				msg.store(srv.kvmap);
				System.out.printf("I: publishing pending=%d\n", srv.sequence);
			}

			return 0;
		}
	}

	private static class NewPassive implements IZLoopHandler {
		@Override
		public int handle(ZLoop loop, PollItem item, Object arg) {
			CloneServer6 srv = (CloneServer6) arg;

			if (srv.kvmap != null) {
				for (KvMsg msg : srv.kvmap.values())
					msg.destroy();
			}
			srv.active = false;
			srv.passive = true;

			// Start subscribing to updates
			PollItem poller = new PollItem(srv.subscriber, ZMQ.Poller.POLLIN);
			srv.bStar.zloop().addPoller(poller, new Subscriber(), srv);
			return 0;
		}
	}

	// .split subscriber handler
	// When we get an update, we create a new kvmap if necessary, and then
	// add our update to our kvmap. We're always passive in this case:
	private static class Subscriber implements IZLoopHandler {
		@Override
		public int handle(ZLoop loop, PollItem item, Object arg) {
			CloneServer6 srv = (CloneServer6) arg;
			@SuppressWarnings("unused")
			Socket socket = item.getSocket();

			// Get state snapshot if necessary
			if (srv.kvmap == null) {
				srv.kvmap = new HashMap<String, KvMsg>();
				Socket snapshot = srv.ctx.createSocket(SocketType.DEALER);
				snapshot.connect(String.format("tcp://localhost:%d", srv.peer));

				System.out.printf("I: asking for snapshot from: tcp://localhost:%d\n", srv.peer);
				snapshot.sendMore("ICANHAZ?");
				snapshot.send(""); // blank subtree to get all

				while (true) {
					KvMsg msg = KvMsg.recv(snapshot);
					if (msg == null)
						break; // Interrupted
					if (msg.getKey().equals("KTHXBAI")) {
						srv.sequence = msg.getSequence();
						msg.destroy();
						break; // Done
					}
					msg.store(srv.kvmap);
				}
				System.out.printf("I: received snapshot=%d\n", srv.sequence);
				snapshot.close();

			}

			// Find and remove update off pending list
			KvMsg msg = KvMsg.recv(item.getSocket());
			if (msg == null)
				return 0;

			if (!msg.getKey().equals("HUGZ")) {
				if (!srv.wasPending(msg)) {
					// If active update came before client update, flip it
					// around, store active update (with sequence) on pending
					// list and use to clear client update when it comes later
					srv.pending.add(msg.dup());
				}
				// If update is more recent than our kvmap, apply it
				if (msg.getSequence() > srv.sequence) {
					srv.sequence = msg.getSequence();
					msg.store(srv.kvmap);
					System.out.printf("I: received update=%d\n", srv.sequence);
				}
			}
			msg.destroy();

			return 0;
		}
	}

	public CloneServer6(boolean primary) {
		if (primary) {
			bStar = new BinaryStarReactor(true, "tcp://*:5003", "tcp://localhost:5004");
			bStar.voter("tcp://*:5556", SocketType.ROUTER, new Snapshots(), this);

			port = 5556;
			peer = 5566;
			this.primary = true;
		} else {
			bStar = new BinaryStarReactor(false, "tcp://*:5004", "tcp://localhost:5003");
			bStar.voter("tcp://*:5566", SocketType.ROUTER, new Snapshots(), this);

			port = 5566;
			peer = 5556;
			this.primary = false;
		}

		// Primary server will become first active
		if (primary)
			kvmap = new HashMap<String, KvMsg>();

		ctx = new ZContext();
		pending = new ArrayList<KvMsg>();
		bStar.setVerbose(true);

		// Set up our clone server sockets
		publisher = ctx.createSocket(SocketType.PUB);
		collector = ctx.createSocket(SocketType.SUB);
		collector.subscribe(ZMQ.SUBSCRIPTION_ALL);
		publisher.bind(String.format("tcp://*:%d", port + 1));
		collector.bind(String.format("tcp://*:%d", port + 2));

		// Set up our own clone client interface to peer
		subscriber = ctx.createSocket(SocketType.SUB);
		subscriber.subscribe(ZMQ.SUBSCRIPTION_ALL);
		subscriber.connect(String.format("tcp://localhost:%d", peer + 1));
	}

	// .split main task body
	// After we've setup our sockets, we register our binary star
	// event handlers, and then start the bstar reactor. This finishes
	// when the user presses Ctrl-C or when the process receives a SIGINT
	// interrupt:
	public void run() {
		// Register state change handlers
		bStar.newActive(new NewActive(), this);
		bStar.newPassive(new NewPassive(), this);

		// Register our other handlers with the bstar reactor
		PollItem poller = new PollItem(collector, ZMQ.Poller.POLLIN);

		bStar.zloop().addPoller(poller, new Collector(), this);
		bStar.zloop().addTimer(1000, 0, new FlushTTL(), this);
		bStar.zloop().addTimer(1000, 0, new SendHugz(), this);

		// Start the bstar reactor
		bStar.start();

		// Interrupted, so shut down
		for (KvMsg value : pending)
			value.destroy();

		bStar.destroy();
		for (KvMsg value : kvmap.values())
			value.destroy();

		ctx.destroy();
	}

	// Send one state snapshot key-value pair to a socket
	// Hash item data is our kvmsg object, ready to send
	private static void sendSingle(KvMsg msg, byte[] identity, String subtree, Socket socket) {
		if (msg.getKey().startsWith(subtree)) {
			socket.send(identity, // Choose recipient
					ZMQ.SNDMORE);
			msg.send(socket);
		}
	}

	// The collector is more complex than in the clonesrv5 example because the
	// way it processes updates depends on whether we're active or passive.
	// The active applies them immediately to its kvmap, whereas the passive
	// queues them as pending:

	// If message was already on pending list, remove it and return TRUE,
	// else return FALSE.
	boolean wasPending(KvMsg msg) {
		Iterator<KvMsg> it = pending.iterator();
		while (it.hasNext()) {
			if (java.util.Arrays.equals(msg.UUID(), it.next().UUID())) {
				it.remove();
				return true;
			}

		}
		return false;
	}

	// We purge ephemeral values using exactly the same code as in
	// the previous clonesrv5 example.
	// .skip
	// If key-value pair has expired, delete it and publish the
	// fact to listening clients.
	private void flushSingle(KvMsg msg) {
		long ttl = Long.parseLong(msg.getProp("ttl"));
		if (ttl > 0 && System.currentTimeMillis() >= ttl) {
			msg.setSequence(++sequence);
			msg.setBody(ZMQ.MESSAGE_SEPARATOR);
			msg.send(publisher);
			msg.store(kvmap);
			System.out.printf("I: publishing delete=%d\n", sequence);
		}
	}

	// .split main task setup
	// The main task parses the command line to decide whether to start
	// as a primary or backup server. We're using the Binary Star pattern
	// for reliability. This interconnects the two servers so they can
	// agree on which one is primary and which one is backup. To allow the
	// two servers to run on the same box, we use different ports for
	// primary and backup. Ports 5003/5004 are used to interconnect the
	// servers. Ports 5556/5566 are used to receive voting events (snapshot
	// requests in the clone pattern). Ports 5557/5567 are used by the
	// publisher, and ports 5558/5568 are used by the collector:
	public static void main(String[] args) {
		CloneServer6 srv = null;

		if (args.length == 1 && "-p".equals(args[0])) {
			srv = new CloneServer6(true);
		} else if (args.length == 1 && "-b".equals(args[0])) {
			srv = new CloneServer6(false);
		} else {
			System.out.print("Usage: clonesrv4 { -p | -b }\n");
			System.exit(0);
		}
		srv.run();
	}
}

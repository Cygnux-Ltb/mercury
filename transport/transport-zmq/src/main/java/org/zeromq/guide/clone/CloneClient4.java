package org.zeromq.guide.clone;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;
import org.zeromq.guide.util.KvSimple;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Clone client Model Four
 *
 */
public class CloneClient4 {
	// This client is identical to clonecli3 except for where we
	// handles subtrees.
	private final static String SUBTREE = "/client/";

	private static final Map<String, KvSimple> kvMap = new HashMap<>();

	public void run() {
		try (ZContext ctx = new ZContext()) {
			Socket snapshot = ctx.createSocket(SocketType.DEALER);
			snapshot.connect("tcp://localhost:5556");

			Socket subscriber = ctx.createSocket(SocketType.SUB);
			subscriber.connect("tcp://localhost:5557");
			subscriber.subscribe(SUBTREE.getBytes(ZMQ.CHARSET));

			Socket push = ctx.createSocket(SocketType.PUSH);
			push.connect("tcp://localhost:5558");

			// get state snapshot
			snapshot.sendMore("ICANHAZ?");
			snapshot.send(SUBTREE);
			long sequence = 0;

			while (true) {
				KvSimple kvMsg = KvSimple.recv(snapshot);
				if (kvMsg == null)
					break; // Interrupted

				sequence = kvMsg.getSequence();
				if ("KTHXBAI".equalsIgnoreCase(kvMsg.getKey())) {
					System.out.println("Received snapshot = " + kvMsg.getSequence());
					break; // done
				}

				System.out.println("receiving " + kvMsg.getSequence());
				CloneClient4.kvMap.put(kvMsg.getKey(), kvMsg);
			}

			Poller poller = ctx.createPoller(1);
			poller.register(subscriber);

			Random random = new Random();

			// now apply pending updates, discard out-of-getSequence messages
			long alarm = System.currentTimeMillis() + 5000;
			while (true) {
				int rc = poller.poll(Math.max(0, alarm - System.currentTimeMillis()));
				if (rc == -1)
					break; // Context has been shut down

				if (poller.pollin(0)) {
					KvSimple kvMsg = KvSimple.recv(subscriber);
					if (kvMsg == null)
						break; // Interrupted
					if (kvMsg.getSequence() > sequence) {
						sequence = kvMsg.getSequence();
						System.out.println("receiving " + sequence);
						CloneClient4.kvMap.put(kvMsg.getKey(), kvMsg);
					}
				}

				if (System.currentTimeMillis() >= alarm) {
					String key = String.format("%s%d", SUBTREE, random.nextInt(10000));
					int body = random.nextInt(1000000);

					ByteBuffer b = ByteBuffer.allocate(4);
					b.asIntBuffer().put(body);

					KvSimple kvUpdateMsg = new KvSimple(key, 0, b.array());
					kvUpdateMsg.send(push);
					alarm = System.currentTimeMillis() + 1000;
				}
			}
		}
	}

	public static void main(String[] args) {
		new CloneClient4().run();
	}
}

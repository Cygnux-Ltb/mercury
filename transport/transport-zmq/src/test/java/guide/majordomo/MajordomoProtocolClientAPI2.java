package guide.majordomo;

import java.util.Formatter;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

/**
 * Majordomo Protocol Client API, asynchronous Java version. Implements the
 * MDP/Worker spec at http://rfc.zeromq.org/spec:7.
 */
public class MajordomoProtocolClientAPI2 {

	private String broker;
	private ZContext ctx;
	private ZMQ.Socket client;
	private long timeout = 2500;
	private boolean verbose;
	private Formatter log = new Formatter(System.out);

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public MajordomoProtocolClientAPI2(String broker, boolean verbose) {
		this.broker = broker;
		this.verbose = verbose;
		ctx = new ZContext();
		reconnectToBroker();
	}

	/**
	 * Connect or reconnect to broker
	 */
	void reconnectToBroker() {
		if (client != null) {
			client.close();
			;
		}
		client = ctx.createSocket(SocketType.DEALER);
		client.connect(broker);
		if (verbose)
			log.format("I: connecting to broker at %s...\n", broker);
	}

	/**
	 * Returns the reply message or NULL if there was no reply. Does not attempt to
	 * recover from a broker failure, this is not possible without storing all
	 * unanswered requests and resending them all…
	 */
	public ZMsg recv() {
		ZMsg reply = null;

		// Poll socket for a reply, with timeout
		ZMQ.Poller items = ctx.createPoller(1);
		items.register(client, ZMQ.Poller.POLLIN);
		if (items.poll(timeout * 1000) == -1)
			return null; // Interrupted

		if (items.pollin(0)) {
			ZMsg msg = ZMsg.recvMsg(client);
			if (verbose) {
				log.format("I: received reply: \n");
				msg.dump(log.out());
			}
			// Don't try to handle errors, just assert noisily
			assert (msg.size() >= 4);

			ZFrame empty = msg.pop();
			assert (empty.getData().length == 0);
			empty.destroy();

			ZFrame header = msg.pop();
			assert (MDP.C_CLIENT.equals(header.toString()));
			header.destroy();

			ZFrame replyService = msg.pop();
			replyService.destroy();

			reply = msg;
		}
		items.close();
		return reply;
	}

	/**
	 * Send request to broker and get reply by hook or crook Takes ownership of
	 * request message and destroys it when sent.
	 */
	public boolean send(String service, ZMsg request) {
		assert (request != null);

		// Prefix request with protocol frames
		// Frame 0: empty (REQ emulation)
		// Frame 1: "MDPCxy" (six bytes, MDP/Client x.y)
		// Frame 2: Service name (printable string)
		request.addFirst(service);
		request.addFirst(MDP.C_CLIENT.newFrame());
		request.addFirst("");
		if (verbose) {
			log.format("I: send request to '%s' service: \n", service);
			request.dump(log.out());
		}
		return request.send(client);
	}

	public void destroy() {
		ctx.destroy();
	}
}

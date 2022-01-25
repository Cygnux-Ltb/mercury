package org.zeromq.guide.clone;

import java.util.HashMap;
import java.util.Map;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;
import org.zeromq.guide.util.KvSimple;

/**
 * Clone client Model Two
 * 
 * @author Danish Shrestha &lt;dshrestha06@gmail.com&gt;
 * 
 */
public class CloneClient2 {
	
	private static Map<String, KvSimple> kvMap = new HashMap<>();

	public void run() {
		try (ZContext ctx = new ZContext()) {
			Socket snapshot = ctx.createSocket(SocketType.DEALER);
			snapshot.connect("tcp://localhost:5556");

			Socket subscriber = ctx.createSocket(SocketType.SUB);
			subscriber.connect("tcp://localhost:5557");
			subscriber.subscribe(ZMQ.SUBSCRIPTION_ALL);

			// get state snapshot
			snapshot.send("ICANHAZ?".getBytes(ZMQ.CHARSET), 0);
			long sequence = 0;
			while (true) {
				KvSimple kvMsg = KvSimple.recv(snapshot);
				if (kvMsg == null)
					break;
				sequence = kvMsg.getSequence();
				if ("KTHXBAI".equalsIgnoreCase(kvMsg.getKey())) {
					System.out.println("Received snapshot = " + kvMsg.getSequence());
					break; // done
				}

				System.out.println("receiving " + kvMsg.getSequence());
				CloneClient2.kvMap.put(kvMsg.getKey(), kvMsg);
			}

			// now apply pending updates, discard out-of-getSequence messages
			while (true) {
				KvSimple kvMsg = KvSimple.recv(subscriber);

				if (kvMsg == null)
					break;

				if (kvMsg.getSequence() > sequence) {
					sequence = kvMsg.getSequence();
					System.out.println("receiving " + sequence);
					CloneClient2.kvMap.put(kvMsg.getKey(), kvMsg);
				}
			}
		}
	}

	public static void main(String[] args) {
		new CloneClient2().run();
	}
}

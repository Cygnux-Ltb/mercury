package org.zeromq.guide.clone;

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ.Socket;
import org.zeromq.guide.util.KvSimple;

/**
 * 
 * Clone server model 1
 * 
 * @author Danish Shrestha &lt;dshrestha06@gmail.com&gt;
 *
 */
public class CloneServer1 {
	
	private static final AtomicLong sequence = new AtomicLong();

	public void run() {
		try (ZContext ctx = new ZContext()) {
			Socket publisher = ctx.createSocket(SocketType.PUB);
			publisher.bind("tcp://*:5556");

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Random random = new Random();

			while (true) {
				long currentSequenceNumber = sequence.incrementAndGet();
				int key = random.nextInt(10000);
				int body = random.nextInt(1000000);

				ByteBuffer b = ByteBuffer.allocate(4);
				b.asIntBuffer().put(body);

				KvSimple kvMsg = new KvSimple(key + "", currentSequenceNumber, b.array());
				kvMsg.send(publisher);
				System.out.println("sending " + kvMsg);
			}
		}
	}

	public static void main(String[] args) {
		new CloneServer1().run();
	}
}

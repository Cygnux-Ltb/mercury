package guide.clone;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import guide.util.KvSimple;

/**
 * Clone client model 1
 * 
 * @author Danish Shrestha &lt;dshrestha06@gmail.com&gt;
 *
 */
public class CloneClient1 {
	private static Map<String, KvSimple> kvMap = new HashMap<>();
	private static AtomicLong sequence = new AtomicLong();

	public void run() {
		try (ZContext ctx = new ZContext()) {
			Socket subscriber = ctx.createSocket(SocketType.SUB);
			subscriber.connect("tcp://localhost:5556");
			subscriber.subscribe(ZMQ.SUBSCRIPTION_ALL);

			while (true) {
				KvSimple kvMsg = KvSimple.recv(subscriber);
				if (kvMsg == null)
					break;

				CloneClient1.kvMap.put(kvMsg.getKey(), kvMsg);
				System.out.println("receiving " + kvMsg);
				sequence.incrementAndGet();
			}
		}
	}

	public static void main(String[] args) {
		new CloneClient1().run();
	}
}

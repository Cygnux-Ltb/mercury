package guide.clone;

import java.util.Random;

/**
 * Clone client model 6
 */
public class CloneClient6 {
	private final static String SUBTREE = "/client/";

	public void run() {

		// Create distributed hash instance
		Clone clone = new Clone();
		Random rand = new Random(System.nanoTime());

		// Specify configuration
		clone.subtree(SUBTREE);
		clone.connect("tcp://localhost", "5556");
		clone.connect("tcp://localhost", "5566");

		// Set random tuples into the distributed hash
		while (!Thread.currentThread().isInterrupted()) {
			// Set random value, check it was stored
			String key = String.format("%s%d", SUBTREE, rand.nextInt(10000));
			String value = String.format("%d", rand.nextInt(1000000));
			clone.set(key, value, rand.nextInt(30));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		clone.destroy();
	}

	public static void main(String[] args) {
		new CloneClient6().run();
	}
}

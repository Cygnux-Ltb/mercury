package net.openhft.chronicle.queue.simple.translator;

import java.util.Scanner;

import net.openhft.chronicle.queue.impl.RollingChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

/**
 * Created by catherine on 26/07/2016.
 */
public class InputMain {

	public static void main(String[] args) {
		String path = "queue-en";
		RollingChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).build();
		MessageConsumer messageConsumer = queue.acquireAppender().methodWriter(MessageConsumer.class);
		try (Scanner read = new Scanner(System.in)) {
			while (true) {
				System.out.println("type something");
				String line = read.nextLine();
				if (line.isEmpty())
					break;
				messageConsumer.onMessage(line);
			}
			System.out.println("... bye.");
		}
	}

}

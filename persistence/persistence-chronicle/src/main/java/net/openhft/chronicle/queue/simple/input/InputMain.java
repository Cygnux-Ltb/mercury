package net.openhft.chronicle.queue.simple.input;

import java.util.Scanner;

import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.impl.RollingChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

/**
 * Created by catherine on 17/07/2016.
 */
public class InputMain {

	public static void main(String[] args) {

		String path = "queue";
		RollingChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).build();
		ExcerptAppender appender = queue.acquireAppender();
		try (Scanner read = new Scanner(System.in)) {
			while (true) {
				System.out.println("type something");
				String line = read.nextLine();
				if (line.isEmpty()) {
					break;
				}
				appender.writeText(line);
			}
			System.out.println("... bye.");
		}

	}
}

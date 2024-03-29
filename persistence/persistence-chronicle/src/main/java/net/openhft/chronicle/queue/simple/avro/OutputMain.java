package net.openhft.chronicle.queue.simple.avro;

import java.io.IOException;

import org.apache.avro.generic.GenericRecord;

import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.impl.RollingChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import net.openhft.chronicle.wire.DocumentContext;

public class OutputMain {

	public static void main(String[] args) throws IOException {
		AvroHelper avro = new AvroHelper();

		String path = "avro-queue";
		RollingChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).build();
		ExcerptTailer tailer = queue.createTailer();

		while (true) {
			try (DocumentContext dc = tailer.readingDocument()) {
				if (dc.wire() == null)
					break;
				GenericRecord user = avro.readFromIS(dc.wire().bytes().inputStream());
				System.out.println("Read: " + user);
			}
		}
		System.out.println("All done");
	}
}

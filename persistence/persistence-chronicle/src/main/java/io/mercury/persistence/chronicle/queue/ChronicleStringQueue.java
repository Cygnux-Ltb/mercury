package io.mercury.persistence.chronicle.queue;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;

import io.mercury.common.number.RandomNumber;
import io.mercury.common.thread.ThreadTool;
import io.mercury.persistence.chronicle.queue.AbstractChronicleReader.ReaderParam;

@Immutable
public class ChronicleStringQueue
		extends AbstractChronicleQueue<String, ChronicleStringReader, ChronicleStringAppender> {

	private ChronicleStringQueue(Builder builder) {
		super(builder);
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	@Override
	protected ChronicleStringReader createReader(String readerName, ReaderParam readerParam, Logger logger,
			Consumer<String> consumer) throws IllegalStateException {
		return new ChronicleStringReader(System.nanoTime(), readerName, fileCycle(), readerParam, logger,
				internalQueue.createTailer(), consumer);
	}

	@Override
	protected ChronicleStringAppender acquireAppender(String appenderName, Logger logger, Supplier<String> supplier)
			throws IllegalStateException {
		return new ChronicleStringAppender(System.nanoTime(), appenderName, logger, internalQueue.acquireAppender(),
				supplier);
	}

	public static final class Builder extends QueueBuilder<Builder> {

		private Builder() {
		}

		public ChronicleStringQueue build() {
			return new ChronicleStringQueue(this);
		}

		@Override
		protected Builder self() {
			return this;
		}

	}

	public static void main(String[] args) {
		ChronicleStringQueue queue = ChronicleStringQueue.newBuilder().fileCycle(FileCycle.MINUTELY).build();
		ChronicleStringAppender queueWriter = queue.acquireAppender();
		ChronicleStringReader queueReader = queue.createReader(next -> System.out.println(next));
		new Thread(() -> {
			for (;;) {
				try {
					queueWriter.append(String.valueOf(RandomNumber.randomLong()));
					ThreadTool.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		queueReader.runningOnNewThread();
	}

}

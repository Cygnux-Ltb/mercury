package io.mercury.persistence.chronicle.queue;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;

import io.mercury.common.number.Randoms;
import io.mercury.common.sequence.EpochSeqAllocator;
import io.mercury.common.thread.Threads;
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
		return new ChronicleStringReader(EpochSeqAllocator.allocate(), readerName, fileCycle(), readerParam, logger,
				internalQueue.createTailer(), consumer);
	}

	@Override
	protected ChronicleStringAppender acquireAppender(String appenderName, Logger logger, Supplier<String> supplier)
			throws IllegalStateException {
		return new ChronicleStringAppender(EpochSeqAllocator.allocate(), appenderName, logger,
				internalQueue.acquireAppender(), supplier);
	}

	/**
	 * 
	 * @author yellow013
	 *
	 */
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
		ChronicleStringAppender writer = queue.acquireAppender();
		ChronicleStringReader reader = queue.createReader(next -> System.out.println(next));
		new Thread(() -> {
			for (;;) {
				try {
					writer.append(String.valueOf(Randoms.randomLong()));
					Threads.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		reader.runningOnNewThread();
	}

}

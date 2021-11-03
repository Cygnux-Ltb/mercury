package io.mercury.persistence.chronicle.queue;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;

import io.mercury.common.number.Randoms;
import io.mercury.common.sequence.EpochSequence;
import io.mercury.common.thread.SleepSupport;
import io.mercury.persistence.chronicle.queue.AbstractChronicleReader.ReaderParam;
import io.mercury.persistence.chronicle.queue.ChronicleStringQueue.ChronicleStringAppender;
import io.mercury.persistence.chronicle.queue.ChronicleStringQueue.ChronicleStringReader;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.ExcerptTailer;

@Immutable
public class ChronicleStringQueue
		extends AbstractChronicleQueue<String, ChronicleStringReader, ChronicleStringAppender> {

	private ChronicleStringQueue(StringQueueBuilder builder) {
		super(builder);
	}

	public static StringQueueBuilder newBuilder() {
		return new StringQueueBuilder();
	}

	@Override
	protected ChronicleStringReader createReader(String readerName, ReaderParam readerParam, Logger logger,
			Consumer<String> consumer) throws IllegalStateException {
		return new ChronicleStringReader(EpochSequence.allocate(), readerName, fileCycle(), readerParam, logger,
				internalQueue.createTailer(), consumer);
	}

	@Override
	protected ChronicleStringAppender acquireAppender(String appenderName, Logger logger, Supplier<String> supplier)
			throws IllegalStateException {
		return new ChronicleStringAppender(EpochSequence.allocate(), appenderName, logger,
				internalQueue.acquireAppender(), supplier);
	}

	/**
	 * 
	 * @author yellow013
	 *
	 */
	public static final class StringQueueBuilder extends AbstractQueueBuilder<StringQueueBuilder> {

		private StringQueueBuilder() {
		}

		public ChronicleStringQueue build() {
			return new ChronicleStringQueue(this);
		}

		@Override
		protected StringQueueBuilder self() {
			return this;
		}

	}

	@Immutable
	@NotThreadSafe
	public static final class ChronicleStringAppender extends AbstractChronicleAppender<String> {

		ChronicleStringAppender(long allocateSeq, String appenderName, Logger logger, ExcerptAppender excerptAppender,
				Supplier<String> supplier) {
			super(allocateSeq, appenderName, logger, excerptAppender, supplier);
		}

		@Override
		protected void append0(String t) {
			excerptAppender.writeText(t);
		}

	}

	@Immutable
	@NotThreadSafe
	public static final class ChronicleStringReader extends AbstractChronicleReader<String> {

		ChronicleStringReader(long allocateSeq, String readerName, FileCycle fileCycle, ReaderParam param,
				Logger logger, ExcerptTailer excerptTailer, Consumer<String> consumer) {
			super(allocateSeq, readerName, fileCycle, param, logger, excerptTailer, consumer);
		}

		@Override
		protected String next0() {
			return excerptTailer.readText();
		}

	}

	public static void main(String[] args) {
		ChronicleStringQueue queue = ChronicleStringQueue.newBuilder().fileCycle(FileCycle.MINUTELY).build();
		ChronicleStringAppender writer = queue.acquireAppender();
		ChronicleStringReader reader = queue.createReader(next -> System.out.println(next));
		new Thread(() -> {
			for (;;) {
				try {
					writer.append(String.valueOf(Randoms.nextLong()));
					SleepSupport.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		reader.runningOnNewThread();
	}

}

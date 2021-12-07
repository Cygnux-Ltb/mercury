package io.mercury.persistence.chronicle.queue;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;

import io.mercury.common.lang.Assertor;
import io.mercury.common.sequence.EpochSequence;
import io.mercury.persistence.chronicle.queue.AbstractChronicleReader.ReaderParam;
import io.mercury.persistence.chronicle.queue.ChronicleDocumentQueue.ChronicleDocumentAppender;
import io.mercury.persistence.chronicle.queue.ChronicleDocumentQueue.ChronicleDocumentReader;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.wire.Marshallable;

@Immutable
public class ChronicleDocumentQueue<T extends Marshallable>
		extends AbstractChronicleQueue<T, ChronicleDocumentReader<T>, ChronicleDocumentAppender<T>> {

	private final Supplier<T> marshallableSupplier;

	private ChronicleDocumentQueue(DocumentQueueBuilder<T> builder) {
		super(builder);
		Assertor.nonNull(builder.marshallableSupplier, "builder.marshallableSupplier");
		this.marshallableSupplier = builder.marshallableSupplier;
	}

	public static <T extends Marshallable> DocumentQueueBuilder<T> newBuilder(Class<T> saveType) {
		return new DocumentQueueBuilder<>();
	}

	@Override
	protected ChronicleDocumentReader<T> createReader(String readerName, ReaderParam readerParam, Logger logger,
			Consumer<T> consumer) throws IllegalStateException {
		return new ChronicleDocumentReader<>(EpochSequence.allocate(), readerName, fileCycle(), readerParam, logger,
				internalQueue.createTailer(), consumer, marshallableSupplier);
	}

	@Override
	protected ChronicleDocumentAppender<T> acquireAppender(String appenderName, Logger logger, Supplier<T> supplier)
			throws IllegalStateException {
		return new ChronicleDocumentAppender<>(EpochSequence.allocate(), appenderName, logger,
				internalQueue.acquireAppender(), supplier);
	}

	/**
	 * 
	 * @author yellow013
	 *
	 */
	public static final class DocumentQueueBuilder<T extends Marshallable>
			extends AbstractQueueBuilder<DocumentQueueBuilder<T>> {

		private Supplier<T> marshallableSupplier;

		private DocumentQueueBuilder() {
		}

		public ChronicleDocumentQueue<T> build() {
			return new ChronicleDocumentQueue<>(this);
		}

		@Override
		protected DocumentQueueBuilder<T> self() {
			return this;
		}

		public DocumentQueueBuilder<T> setMarshallableSupplier(@Nonnull Supplier<T> marshallableSupplier) {
			this.marshallableSupplier = marshallableSupplier;
			return this;
		}

	}

	@Immutable
	@NotThreadSafe
	public static final class ChronicleDocumentAppender<T extends Marshallable> extends AbstractChronicleAppender<T> {

		ChronicleDocumentAppender(long allocateSeq, String appenderName, Logger logger, ExcerptAppender excerptAppender,
				Supplier<T> supplier) {
			super(allocateSeq, appenderName, logger, excerptAppender, supplier);
		}

		@Override
		protected void append0(T t) {
			excerptAppender.writeDocument(t);
		}

	}

	@Immutable
	@NotThreadSafe
	public static final class ChronicleDocumentReader<T extends Marshallable> extends AbstractChronicleReader<T> {

		private final Supplier<T> marshallableSupplier;

		ChronicleDocumentReader(long allocateSeq, String readerName, FileCycle fileCycle, ReaderParam param,
				Logger logger, ExcerptTailer excerptTailer, Consumer<T> consumer, Supplier<T> marshallableSupplier) {
			super(allocateSeq, readerName, fileCycle, param, logger, excerptTailer, consumer);
			this.marshallableSupplier = marshallableSupplier;
		}

		@Override
		protected T next0() {
			final T t = marshallableSupplier.get();
			return excerptTailer.readDocument(t) ? t : null;
		}

	}

}

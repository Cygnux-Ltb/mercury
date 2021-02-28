package io.mercury.persistence.chronicle.queue;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;

import io.mercury.common.sequence.EpochSequence;
import io.mercury.common.util.Assertor;
import io.mercury.persistence.chronicle.queue.AbstractChronicleReader.ReaderParam;
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

}

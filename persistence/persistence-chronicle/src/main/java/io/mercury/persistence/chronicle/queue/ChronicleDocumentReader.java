package io.mercury.persistence.chronicle.queue;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;

import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.wire.Marshallable;

@Immutable
@NotThreadSafe
public final class ChronicleDocumentReader<T extends Marshallable> extends AbstractChronicleReader<T> {

	private final Supplier<T> marshallableSupplier;

	ChronicleDocumentReader(long allocateSeq, String readerName, FileCycle fileCycle, ReaderParam param, Logger logger,
			ExcerptTailer excerptTailer, Consumer<T> consumer, Supplier<T> marshallableSupplier) {
		super(allocateSeq, readerName, fileCycle, param, logger, excerptTailer, consumer);
		this.marshallableSupplier = marshallableSupplier;
	}

	@Override
	protected T next0() {
		final T t = marshallableSupplier.get();
		return excerptTailer.readDocument(t) ? t : null;
	}

}

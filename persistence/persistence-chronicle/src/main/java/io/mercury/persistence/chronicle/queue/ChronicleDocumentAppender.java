package io.mercury.persistence.chronicle.queue;

import java.util.function.Supplier;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;

import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.wire.Marshallable;

@Immutable
@NotThreadSafe
public final class ChronicleDocumentAppender<T extends Marshallable> extends AbstractChronicleAppender<T> {

	ChronicleDocumentAppender(long allocateSeq, String appenderName, Logger logger, ExcerptAppender excerptAppender,
			Supplier<T> supplier) {
		super(allocateSeq, appenderName, logger, excerptAppender, supplier);
	}

	@Override
	protected void append0(T t) {
		excerptAppender.writeDocument(t);
	}

}

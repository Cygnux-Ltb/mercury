package io.mercury.persistence.chronicle.queue;

import java.util.function.Supplier;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;

import net.openhft.chronicle.queue.ExcerptAppender;

@Immutable
@NotThreadSafe
public final class ChronicleStringAppender extends AbstractChronicleAppender<String> {

	ChronicleStringAppender(long allocateSeq, String appenderName, Logger logger, ExcerptAppender excerptAppender,
			Supplier<String> supplier) {
		super(allocateSeq, appenderName, logger, excerptAppender, supplier);
	}

	@Override
	protected void append0(String t) {
		excerptAppender.writeText(t);
	}

}

package io.mercury.persistence.chronicle.queue;

import java.nio.ByteBuffer;
import java.util.function.Supplier;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;

import net.openhft.chronicle.bytes.BytesStore;
import net.openhft.chronicle.queue.ExcerptAppender;

@Immutable
@NotThreadSafe
public final class ChronicleBytesAppender extends AbstractChronicleAppender<ByteBuffer> {

	ChronicleBytesAppender(long allocateSeq, String appenderName, Logger logger, ExcerptAppender excerptAppender,
			Supplier<ByteBuffer> supplier) {
		super(allocateSeq, appenderName, logger, excerptAppender, supplier);
	}

	@Override
	protected void append0(ByteBuffer t) {
		// use heap memory or direct by the byteBuffer
		excerptAppender.writeBytes(BytesStore.wrap(t));
	}

}

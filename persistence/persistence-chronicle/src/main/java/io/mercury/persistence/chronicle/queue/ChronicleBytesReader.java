package io.mercury.persistence.chronicle.queue;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.queue.ExcerptTailer;

@Immutable
@NotThreadSafe
public final class ChronicleBytesReader extends AbstractChronicleReader<ByteBuffer> {

	private final int bufferSize;
	private final boolean useDirectMemory;

	ChronicleBytesReader(long allocateSeq, String readerName, FileCycle fileCycle, ReaderParam param, Logger logger,
			int bufferSize, boolean useDirectMemory, ExcerptTailer excerptTailer, Consumer<ByteBuffer> consumer) {
		super(allocateSeq, readerName, fileCycle, param, logger, excerptTailer, consumer);
		this.bufferSize = bufferSize;
		this.useDirectMemory = useDirectMemory;
	}

	@Override
	protected ByteBuffer next0() {
		Bytes<ByteBuffer> bytes;
		if (useDirectMemory)
			// use direct memory
			bytes = Bytes.elasticByteBuffer(bufferSize);
		else
			// use heap memory
			bytes = Bytes.elasticHeapByteBuffer(bufferSize);
		excerptTailer.readBytes(bytes);
		if (bytes.isEmpty())
			return null;
		logger.debug("ChronicleBytesReader.next0() -> {}", bytes.toDebugString());
		return bytes.underlyingObject();
	}

}

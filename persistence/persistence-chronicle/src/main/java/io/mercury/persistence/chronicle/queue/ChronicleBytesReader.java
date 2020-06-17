package io.mercury.persistence.chronicle.queue;

import static net.openhft.chronicle.bytes.Bytes.elasticByteBuffer;
import static net.openhft.chronicle.bytes.Bytes.elasticHeapByteBuffer;

import java.nio.ByteBuffer;
import java.util.function.Consumer;

import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.queue.ExcerptTailer;

@NotThreadSafe
public final class ChronicleBytesReader extends AbstractChronicleReader<ByteBuffer> {

	private final int bufferSize;
	private final boolean useDirectMemory;

	ChronicleBytesReader(long allocationNo, String readerName, FileCycle fileCycle, ReaderParam readerParam,
			Logger logger, int bufferSize, boolean useDirectMemory, ExcerptTailer excerptTailer,
			Consumer<ByteBuffer> consumer) {
		super(allocationNo, readerName, fileCycle, readerParam, logger, excerptTailer, consumer);
		this.bufferSize = bufferSize;
		this.useDirectMemory = useDirectMemory;
	}

	@Override
	protected ByteBuffer next0() {
		Bytes<ByteBuffer> bytes;
		if (useDirectMemory)
			// use direct memory
			bytes = elasticByteBuffer(bufferSize);
		else
			// use heap memory
			bytes = elasticHeapByteBuffer(bufferSize);
		excerptTailer.readBytes(bytes);
		if (bytes.isEmpty())
			return null;
		// System.out.println(bytes.toDebugString());
		return bytes.underlyingObject();
	}

}

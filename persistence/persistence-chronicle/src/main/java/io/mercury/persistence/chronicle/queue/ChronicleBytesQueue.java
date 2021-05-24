package io.mercury.persistence.chronicle.queue;

import java.nio.ByteBuffer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;

import io.mercury.common.number.Randoms;
import io.mercury.common.sequence.EpochSequence;
import io.mercury.common.thread.Threads;
import io.mercury.persistence.chronicle.queue.AbstractChronicleReader.ReaderParam;
import io.mercury.persistence.chronicle.queue.ChronicleBytesQueue.ChronicleBytesAppender;
import io.mercury.persistence.chronicle.queue.ChronicleBytesQueue.ChronicleBytesReader;
import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.bytes.BytesStore;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.ExcerptTailer;

@Immutable
public class ChronicleBytesQueue
		extends AbstractChronicleQueue<ByteBuffer, ChronicleBytesReader, ChronicleBytesAppender> {

	private final int bufferSize;
	private final boolean useDirectMemory;

	private ChronicleBytesQueue(BytesQueueBuilder builder) {
		super(builder);
		this.bufferSize = builder.bufferSize;
		this.useDirectMemory = builder.useDirectMemory;
	}

	/**
	 * 
	 * @return
	 */
	public static BytesQueueBuilder newBuilder() {
		return new BytesQueueBuilder();
	}

	@Override
	protected ChronicleBytesReader createReader(String readerName, ReaderParam readerParam, Logger logger,
			Consumer<ByteBuffer> consumer) throws IllegalStateException {
		return new ChronicleBytesReader(EpochSequence.allocate(), readerName, fileCycle(), readerParam, logger,
				bufferSize, useDirectMemory, internalQueue.createTailer(), consumer);
	}

	@Override
	protected ChronicleBytesAppender acquireAppender(String appenderName, Logger logger, Supplier<ByteBuffer> supplier)
			throws IllegalStateException {
		return new ChronicleBytesAppender(EpochSequence.allocate(), appenderName, logger,
				internalQueue.acquireAppender(), supplier);
	}

	/**
	 * 
	 * @author yellow013
	 *
	 */
	public static final class BytesQueueBuilder extends AbstractQueueBuilder<BytesQueueBuilder> {

		private int bufferSize = 256;
		private boolean useDirectMemory = false;

		private BytesQueueBuilder() {
		}

		public ChronicleBytesQueue build() {
			return new ChronicleBytesQueue(this);
		}

		/**
		 * if set size less than 256, use default size the 256
		 * 
		 * @param readBufferSize
		 * @return
		 */
		public BytesQueueBuilder bufferSize(int bufferSize) {
			this.bufferSize = Math.max(bufferSize, 256);
			return this;
		}

		public BytesQueueBuilder useDirectMemory(boolean useDirectMemory) {
			this.useDirectMemory = useDirectMemory;
			return this;
		}

		@Override
		protected BytesQueueBuilder self() {
			return this;
		}

	}

	/**
	 * 
	 * @author yellow013
	 *
	 */
	@Immutable
	@NotThreadSafe
	public static final class ChronicleBytesAppender extends AbstractChronicleAppender<ByteBuffer> {

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

	/**
	 * 
	 * @author yellow013
	 *
	 */
	@Immutable
	@NotThreadSafe
	public static final class ChronicleBytesReader extends AbstractChronicleReader<ByteBuffer> {

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

	public static void main(String[] args) {
		ChronicleBytesQueue queue = ChronicleBytesQueue.newBuilder().folder("byte-test").bufferSize(512)
				.fileCycle(FileCycle.MINUTELY).build();
		ChronicleBytesAppender writer = queue.acquireAppender();
		ChronicleBytesReader reader = queue.createReader(next -> System.out.println(new String(next.array())));
		new Thread(() -> {
			ByteBuffer buffer = ByteBuffer.allocate(512);
			for (;;) {
				try {
					writer.append(buffer.put(String.valueOf(Randoms.nextLong()).getBytes()));
					buffer.clear();
					Threads.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		reader.runningOnNewThread();
	}

}

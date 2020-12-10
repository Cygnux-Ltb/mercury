package io.mercury.persistence.chronicle.queue;

import java.nio.ByteBuffer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;

import io.mercury.common.number.Randoms;
import io.mercury.common.sequence.EpochSeqAllocator;
import io.mercury.common.thread.Threads;
import io.mercury.persistence.chronicle.queue.AbstractChronicleReader.ReaderParam;

@Immutable
public class ChronicleBytesQueue
		extends AbstractChronicleQueue<ByteBuffer, ChronicleBytesReader, ChronicleBytesAppender> {

	private final int bufferSize;
	private final boolean useDirectMemory;

	private ChronicleBytesQueue(Builder builder) {
		super(builder);
		this.bufferSize = builder.bufferSize;
		this.useDirectMemory = builder.useDirectMemory;
	}

	/**
	 * 
	 * @return
	 */
	public static Builder newBuilder() {
		return new Builder();
	}

	@Override
	protected ChronicleBytesReader createReader(String readerName, ReaderParam readerParam, Logger logger,
			Consumer<ByteBuffer> consumer) throws IllegalStateException {
		return new ChronicleBytesReader(EpochSeqAllocator.allocate(), readerName, fileCycle(), readerParam, logger, bufferSize,
				useDirectMemory, internalQueue.createTailer(), consumer);
	}

	@Override
	protected ChronicleBytesAppender acquireAppender(String appenderName, Logger logger, Supplier<ByteBuffer> supplier)
			throws IllegalStateException {
		return new ChronicleBytesAppender(EpochSeqAllocator.allocate(), appenderName, logger, internalQueue.acquireAppender(),
				supplier);
	}

	/**
	 * 
	 * @author yellow013
	 *
	 */
	public static final class Builder extends QueueBuilder<Builder> {

		private int bufferSize = 256;
		private boolean useDirectMemory = false;

		private Builder() {
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
		public Builder bufferSize(int bufferSize) {
			this.bufferSize = Math.max(bufferSize, 256);
			return this;
		}

		public Builder useDirectMemory(boolean useDirectMemory) {
			this.useDirectMemory = useDirectMemory;
			return this;
		}

		@Override
		protected Builder self() {
			return this;
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
					writer.append(buffer.put(String.valueOf(Randoms.randomLong()).getBytes()));
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

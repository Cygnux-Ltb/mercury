package io.mercury.persistence.chronicle.queue.base;

import io.mercury.common.annotation.AbstractFunction;

/**
 * 通用访问器抽象类
 * 
 * @author yellow013
 *
 */
public abstract class CloseableChronicleAccessor implements net.openhft.chronicle.core.io.Closeable {

	protected volatile boolean isClose = false;

	private final long allocateSeq;

	protected CloseableChronicleAccessor(long allocateSeq) {
		this.allocateSeq = allocateSeq;
	}

	@Override
	public void close() {
		this.isClose = true;
		close0();
	}

	public long getAllocateSeq() {
		return allocateSeq;
	}

	@Override
	public boolean isClosed() {
		return isClose;
	}

	@AbstractFunction
	protected abstract void close0();

}

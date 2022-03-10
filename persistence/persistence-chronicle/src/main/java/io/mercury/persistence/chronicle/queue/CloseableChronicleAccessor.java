package io.mercury.persistence.chronicle.queue;

import io.mercury.common.annotation.AbstractFunction;
import org.slf4j.Logger;

/**
 * 通用访问器抽象类
 *
 * @author yellow013
 */
public abstract class CloseableChronicleAccessor implements net.openhft.chronicle.core.io.Closeable {

    protected volatile boolean isClose = false;

    private final long allocateSeq;

    protected final String accessorName;
    protected final Logger logger;

    protected CloseableChronicleAccessor(long allocateSeq, String accessorName, Logger logger) {
        this.allocateSeq = allocateSeq;
        this.accessorName = accessorName;
        this.logger = logger;
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

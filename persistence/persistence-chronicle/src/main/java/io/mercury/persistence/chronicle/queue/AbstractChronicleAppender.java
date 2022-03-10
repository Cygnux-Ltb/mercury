package io.mercury.persistence.chronicle.queue;

import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import io.mercury.common.annotation.thread.OnlyAllowSingleThreadAccess;
import org.slf4j.Logger;

import io.mercury.common.annotation.AbstractFunction;
import io.mercury.common.serialization.basic.Serializer;
import io.mercury.persistence.chronicle.exception.ChronicleAppendException;
import net.openhft.chronicle.queue.ExcerptAppender;

@Immutable
@NotThreadSafe
public abstract class AbstractChronicleAppender<IN> extends CloseableChronicleAccessor implements Runnable {

    protected final ExcerptAppender appender;

    protected final Supplier<IN> dataProducer;

    protected AbstractChronicleAppender(long allocateSeq,
                                        String appenderName,
                                        Logger logger,
                                        ExcerptAppender appender,
                                        Supplier<IN> dataProducer) {
        super(allocateSeq, appenderName, logger);
        this.appender = appender;
        this.dataProducer = dataProducer;
    }

    public ExcerptAppender getExcerptAppender() {
        return appender;
    }

    public int getCycle() {
        return appender.cycle();
    }

    public int getSourceId() {
        return appender.sourceId();
    }

    public String getAppenderName() {
        return accessorName;
    }

    /**
     * @param t
     * @param serializer
     * @throws IllegalStateException
     * @throws ChronicleAppendException
     */
    @OnlyAllowSingleThreadAccess
    public <T> void append(@Nullable T t,
                           @Nonnull Serializer<T, IN> serializer)
            throws IllegalStateException, ChronicleAppendException {
        append(serializer.serialization(t));
    }

    /**
     * @param in
     * @throws IllegalStateException
     * @throws ChronicleAppendException
     */
    @OnlyAllowSingleThreadAccess
    public void append(@Nullable IN in) throws IllegalStateException, ChronicleAppendException {
        if (isClose) {
            throw new IllegalStateException("Unable to append data, Chronicle queue is closed");
        }
        try {
            if (in != null) {
                append0(in);
            } else {
                logger.warn("ChronicleAppender -> [{}] : received null object, Not written to the queue", getAppenderName());
            }
        } catch (Exception e) {
            throw new ChronicleAppendException(e.getMessage(), e);
        }
    }


    @AbstractFunction
    protected abstract void append0(@Nonnull IN in);

    @Override
    public void run() {
        if (dataProducer != null) {
            for (; ; ) {
                if (!isClose) {
                    logger.info("Chronicle queue is closed, {} Thread exit", getAppenderName());
                    break;
                } else {
                    IN in = dataProducer.get();
                    append(in);
                }
            }
        } else {
            logger.warn("Data producer function is null, Thread exit");
        }
    }

    protected void close0() {
        logger.info("Appender -> {} is closed.", getAppenderName());
    }

}

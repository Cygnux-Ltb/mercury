package io.mercury.persistence.chronicle.queue;

import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;

import io.mercury.common.annotation.AbstractFunction;
import io.mercury.common.serialization.Serializer;
import io.mercury.persistence.chronicle.exception.ChronicleAppendException;
import io.mercury.persistence.chronicle.queue.base.CloseableChronicleAccessor;
import net.openhft.chronicle.queue.ExcerptAppender;

@Immutable
@NotThreadSafe
public abstract class AbstractChronicleAppender<IN> extends CloseableChronicleAccessor implements Runnable {

	private final String appenderName;

	protected final Logger logger;
	protected final ExcerptAppender appender;

	protected final Supplier<IN> dataProducer;

	protected AbstractChronicleAppender(long allocateSeq, String appenderName, Logger logger, ExcerptAppender appender,
			Supplier<IN> dataProducer) {
		super(allocateSeq);
		this.appenderName = appenderName;
		this.logger = logger;
		this.appender = appender;
		this.dataProducer = dataProducer;
	}

	public ExcerptAppender getExcerptAppender() {
		return appender;
	}

	public int cycle() {
		return appender.cycle();
	}

	public int sourceId() {
		return appender.sourceId();
	}

	public String appenderName() {
		return appenderName;
	}

	/**
	 * 
	 * @param in
	 * @throws IllegalStateException
	 * @throws ChronicleAppendException
	 */
	public void append(@Nonnull IN in) throws IllegalStateException, ChronicleAppendException {
		if (isClose) {
			throw new IllegalStateException("Unable to append data, Chronicle queue is closed");
		}
		try {
			if (in != null) {
				append0(in);
			} else {
				logger.warn("ChronicleAppender -> [{}] : received null object, Not written to the queue", appenderName);
			}
		} catch (Exception e) {
			throw new ChronicleAppendException(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param t
	 * @param serializer
	 * @throws IllegalStateException
	 * @throws ChronicleAppendException
	 */
	public <T> void append(@Nonnull T t, Serializer<T, IN> serializer)
			throws IllegalStateException, ChronicleAppendException {
		append(serializer.serialization(t));
	}

	@AbstractFunction
	protected abstract void append0(@Nonnull IN in);

	@Override
	public void run() {
		if (dataProducer != null) {
			for (;;) {
				if (isClose) {
					logger.info("Chronicle queue is closed, {} Thread exit", appenderName);
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

	}

}

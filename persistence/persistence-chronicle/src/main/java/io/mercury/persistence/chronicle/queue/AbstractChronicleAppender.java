package io.mercury.persistence.chronicle.queue;

import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;

import io.mercury.common.annotation.AbstractFunction;
import io.mercury.common.serialization.api.Serializer;
import io.mercury.persistence.chronicle.exception.ChronicleAppendException;
import io.mercury.persistence.chronicle.queue.AbstractChronicleQueue.CloseableChronicleAccessor;
import net.openhft.chronicle.queue.ExcerptAppender;

@Immutable
@NotThreadSafe
public abstract class AbstractChronicleAppender<IN> extends CloseableChronicleAccessor implements Runnable {

	private final String appenderName;

	protected final Logger logger;
	protected final ExcerptAppender excerptAppender;

	private final Supplier<IN> dataSupplier;

	protected AbstractChronicleAppender(long allocateSeq, String appenderName, Logger logger,
			ExcerptAppender excerptAppender, Supplier<IN> dataSupplier) {
		super(allocateSeq);
		this.appenderName = appenderName;
		this.logger = logger;
		this.excerptAppender = excerptAppender;
		this.dataSupplier = dataSupplier;
	}

	public ExcerptAppender getExcerptAppender() {
		return excerptAppender;
	}

	public int cycle() {
		return excerptAppender.cycle();
	}

	public int sourceId() {
		return excerptAppender.sourceId();
	}

	public String appenderName() {
		return appenderName;
	}

	/**
	 * 
	 * @param t
	 * @throws IllegalStateException
	 * @throws ChronicleAppendException
	 */
	public void append(@Nonnull IN t) throws IllegalStateException, ChronicleAppendException {
		if (isClose) {
			throw new IllegalStateException("Unable to append data, Chronicle queue is closed");
		}
		try {
			if (t != null) {
				append0(t);
			} else {
				logger.warn("appenderName -> {} : received null object, Not written to the queue.", appenderName);
			}
		} catch (Exception e) {
			throw new ChronicleAppendException(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param obj
	 * @param serializer
	 * @throws IllegalStateException
	 * @throws ChronicleAppendException
	 */
	public void append(@Nonnull Object obj, Serializer<Object, IN> serializer)
			throws IllegalStateException, ChronicleAppendException {
		append(serializer.serialization(obj));
	}

	@AbstractFunction
	protected abstract void append0(@Nonnull IN t);

	@Override
	public void run() {
		if (dataSupplier != null) {
			for (;;) {
				if (isClose) {
					logger.info("Chronicle queue is closed, {} Thread exit", appenderName);
					break;
				} else {
					IN t = dataSupplier.get();
					append(t);
				}
			}
		} else {
			logger.warn("Supplier is null, Thread exit");
		}
	}

	protected void close0() {

	}

}

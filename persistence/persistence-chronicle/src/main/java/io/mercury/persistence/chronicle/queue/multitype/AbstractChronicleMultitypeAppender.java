package io.mercury.persistence.chronicle.queue.multitype;

import java.util.function.Supplier;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;

import io.mercury.common.annotation.lang.AbstractFunction;
import io.mercury.common.codec.Envelope;
import io.mercury.common.serialization.Serializer;
import io.mercury.persistence.chronicle.exception.ChronicleAppendException;
import io.mercury.persistence.chronicle.queue.multitype.AbstractChronicleMultitypeQueue.CloseableChronicleAccessor;
import net.openhft.chronicle.queue.ExcerptAppender;

@Immutable
@NotThreadSafe
public abstract class AbstractChronicleMultitypeAppender<E extends Envelope, IN> extends CloseableChronicleAccessor
		implements Runnable {

	private final String appenderName;

	protected final Logger logger;
	protected final ExcerptAppender excerptAppender;

	private final Supplier<IN> dataSupplier;

	protected AbstractChronicleMultitypeAppender(long allocateSeq, String appenderName, Logger logger,
			ExcerptAppender excerptAppender, Supplier<IN> dataSupplier) {
		super(allocateSeq);
		this.appenderName = appenderName;
		this.logger = logger;
		this.excerptAppender = excerptAppender;
		this.dataSupplier = dataSupplier;
	}

	public ExcerptAppender excerptAppender() {
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
	public void append(@Nonnull E envelope, @Nonnull IN t) throws IllegalStateException, ChronicleAppendException {
		if (isClose) {
			throw new IllegalStateException("Unable to append data, Chronicle queue is closed");
		}
		try {
			if (t != null) {
				append0(envelope, t);
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
	public void append(@Nonnull E envelope, @Nonnull Object obj, Serializer<Object, IN> serializer)
			throws IllegalStateException, ChronicleAppendException {
		append(envelope, serializer.serialization(obj));
	}

	@AbstractFunction
	protected abstract void append0(@Nonnull E envelope, @Nonnull IN t);

	@CheckForNull
	private E envelope;

	/**
	 * 设置默认信封
	 * 
	 * @param envelope
	 */
	public void setEnvelope(E envelope) {
		this.envelope = envelope;
	}

	@Override
	public void run() {
		if (dataSupplier == null) {
			logger.error("Supplier is null, Thread exit");
			return;
		}
		if (envelope == null) {
			logger.error("Default envelope is null, Thread exit");
			return;
		}
		for (;;) {
			if (isClose) {
				logger.info("Chronicle queue is closed, {} Thread exit", appenderName);
				break;
			} else {
				IN t = dataSupplier.get();
				append(envelope, t);
			}
		}
	}

	protected void close0() {

	}

}

package io.mercury.common.concurrent.queue;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;

import io.mercury.common.annotation.AbstractFunction;
import io.mercury.common.functional.Processor;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.number.ThreadSafeRandoms;
import io.mercury.common.util.Assertor;

/**
 * @author yellow013
 *
 * @param <T> Single Consumer Queue base implements
 */
public abstract class SingleConsumerQueue<E> implements Queue<E> {

	private static final Logger log = CommonLoggerFactory.getLogger(SingleConsumerQueue.class);

	/**
	 * Processor Function
	 */
	protected final Processor<E> processor;

	/**
	 * Running flag
	 */
	private final AtomicBoolean isRunning = new AtomicBoolean(false);

	/**
	 * Closed flag
	 */
	protected final AtomicBoolean isClosed = new AtomicBoolean(false);

	/**
	 * 
	 */
	protected String queueName = "SCQueue-" + Integer.toString(ThreadSafeRandoms.randomUnsignedInt());

	protected SingleConsumerQueue(Processor<E> processor) {
		Assertor.nonNull(processor, "processor");
		this.processor = processor;
	}

	protected boolean isRunning() {
		return isRunning.get();
	}

	public void start() {
		if (isRunning.compareAndSet(false, true)) {
			try {
				start0();
			} catch (Exception e) {
				isRunning.set(false);
				log.error("Queue start0 throw Exception -> {}", e.getMessage(), e);
				throw new RuntimeException("start0 function have exception", e);
			}
		} else {
			log.info("Queue -> {}, Error call, This queue is started", queueName);
		}
	}

	@AbstractFunction
	protected abstract void start0() throws Exception;

	public void stop() {
		this.isRunning.set(false);
		if (isClosed.compareAndSet(false, true)) {
			try {
				stop0();
			} catch (Exception e) {
				log.error("Queue stop0 throw Exception -> {}", e.getMessage(), e);
				throw new RuntimeException("stop0 function have exception", e);
			}
		} else {

		}
	}

	protected abstract void stop0() throws Exception;

	@Override
	public String getQueueName() {
		return queueName;
	}

}

package io.mercury.common.concurrent.queue;

import java.util.concurrent.atomic.AtomicBoolean;

import io.mercury.common.annotation.lang.AbstractFunction;
import io.mercury.common.functional.Processor;
import io.mercury.common.number.ThreadSafeRandoms;
import io.mercury.common.util.Assertor;

/**
 * @author yellow013
 *
 * @param <T> Single Consumer Queue
 */
public abstract class SCQueue<E> implements Queue<E> {

	/**
	 * Processor Function
	 */
	protected final Processor<E> processor;

	/**
	 * Running flag
	 */
	protected final AtomicBoolean isRunning = new AtomicBoolean(false);

	/**
	 * Close flag
	 */
	protected final AtomicBoolean isClosed = new AtomicBoolean(true);

	/**
	 * 
	 */
	protected String queueName = "SCQueue-" + Integer.toString(ThreadSafeRandoms.randomUnsignedInt());

	protected SCQueue(Processor<E> processor) {
		Assertor.nonNull(processor, "processor");
		this.processor = processor;
	}

	@AbstractFunction
	protected abstract void startProcessThread();

	public void start() {
		startProcessThread();
	}

	public void stop() {
		this.isRunning.set(false);
		this.isClosed.set(true);
	}

	@Override
	public String queueName() {
		return queueName;
	}

	public static enum StartMode {

		Auto, Manual, Delay

	}

}

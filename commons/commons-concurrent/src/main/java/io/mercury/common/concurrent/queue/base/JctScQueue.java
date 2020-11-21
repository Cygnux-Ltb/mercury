package io.mercury.common.concurrent.queue.base;

import java.util.concurrent.atomic.AtomicBoolean;

import io.mercury.common.annotation.lang.AbstractFunction;
import io.mercury.common.collections.queue.api.Queue;
import io.mercury.common.functional.Processor;
import io.mercury.common.number.Randoms;
import io.mercury.common.util.Assertor;

/**
 * @author yellow013
 *
 * @param <T> Single Consumer Queue
 */
public abstract class JctScQueue<Q extends java.util.Queue<E>, E> implements Queue<E> {

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
	protected final AtomicBoolean isClose = new AtomicBoolean(true);

	protected Q queue;

	protected String queueName = "SCQueue-" + Integer.toString(Randoms.threadSafeRandomUnsignedInt());

	public JctScQueue(Processor<E> processor) {
		Assertor.nonNull(processor, "processor");
		this.processor = processor;
	}

	@AbstractFunction
	protected abstract void startProcessThread();

	@AbstractFunction
	protected abstract Q createQueue(int capacity);

	public void start() {
		startProcessThread();
	}

	public void stop() {
		this.isRunning.set(false);
		this.isClose.set(true);
	}

	@Override
	public String queueName() {
		return queueName;
	}

}

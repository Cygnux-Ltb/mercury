package io.mercury.common.collections.queue.api;

import java.util.concurrent.atomic.AtomicBoolean;

import io.mercury.common.annotation.lang.AbstractFunction;
import io.mercury.common.functional.Processor;
import io.mercury.common.number.Randoms;
import io.mercury.common.util.Assertor;

/**
 * @author yellow013
 *
 * @param <T> Single Consumer Queue
 */
public abstract class SCQueue<E> implements Queue<E> {

	protected Processor<E> processor;

	/**
	 * Running flag
	 */
	protected AtomicBoolean isRun = new AtomicBoolean(false);

	/**
	 * Close flag
	 */
	protected AtomicBoolean isClose = new AtomicBoolean(true);

	protected String queueName = "SCQueue-" + Integer.toString(Randoms.threadSafeRandomUnsignedInt());

	public SCQueue(Processor<E> processor) {
		Assertor.nonNull(processor, "processor");
		this.processor = processor;
	}

	@AbstractFunction
	protected abstract void startProcessThread();

	public void start() {
		startProcessThread();
	}

	public void stop() {
		this.isRun.set(false);
		this.isClose.set(true);
	}

	@Override
	public String queueName() {
		return queueName;
	}

}

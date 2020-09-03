package io.mercury.common.collections.queue.api;

import static io.mercury.common.number.RandomNumber.randomUnsignedInt;

import java.util.concurrent.atomic.AtomicBoolean;

import io.mercury.common.annotation.lang.AbstractFunction;
import io.mercury.common.functional.Processor;

/**
 * @author yellow013
 *
 * @param <T> Single Consumer Queue
 */
public abstract class SCQueue<E> implements Queue<E> {

	protected Processor<E> processor;

	protected AtomicBoolean isRun = new AtomicBoolean(false);

	protected AtomicBoolean isClose = new AtomicBoolean(true);

	protected String queueName = "SCQueue-" + Integer.toString(randomUnsignedInt());

	public SCQueue(Processor<E> processor) {
		if (processor == null)
			throw new IllegalArgumentException("processor is null...");
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
	public String name() {
		return queueName;
	}

}

package io.mercury.common.collections.queue.api;

import java.util.concurrent.atomic.AtomicBoolean;

import io.mercury.common.annotation.lang.ProtectedAbstractMethod;
import io.mercury.common.functional.Processor;
import io.mercury.common.number.RandomNumber;

/**
 * @author yellow013
 *
 * @param <T> Single Consumer Queue
 */
public abstract class SCQueue<E> implements Queue<E> {

	protected Processor<E> processor;

	protected AtomicBoolean isRun = new AtomicBoolean(false);

	protected AtomicBoolean isClose = new AtomicBoolean(true);

	protected String queueName = "SCQueue-" + Integer.toString(RandomNumber.randomUnsignedIntOfSafe());

	public SCQueue(Processor<E> processor) {
		if (processor == null)
			throw new IllegalArgumentException("processor is null...");
		this.processor = processor;
	}

	@ProtectedAbstractMethod
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

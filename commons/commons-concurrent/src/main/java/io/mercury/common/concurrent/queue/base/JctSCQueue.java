package io.mercury.common.concurrent.queue.base;

import io.mercury.common.annotation.lang.AbstractFunction;
import io.mercury.common.concurrent.queue.api.SCQueue;
import io.mercury.common.functional.Processor;

/**
 * @author yellow013
 *
 * @param <T> Single Consumer Queue
 */
public abstract class JctSCQueue<Q extends java.util.Queue<E>, E> extends SCQueue<E> {

	protected final Q innerQueue;

	protected JctSCQueue(Processor<E> processor, int capacity) {
		super(processor);
		this.innerQueue = createQueue(capacity);
	}

	@AbstractFunction
	protected abstract Q createQueue(int capacity);

}

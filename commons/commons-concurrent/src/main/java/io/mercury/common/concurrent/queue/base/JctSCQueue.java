package io.mercury.common.concurrent.queue.base;

import io.mercury.common.annotation.lang.AbstractFunction;
import io.mercury.common.concurrent.queue.api.ScQueue;
import io.mercury.common.functional.Processor;

/**
 * @author yellow013
 *
 * @param <T> Single Consumer Queue
 */
public abstract class JctScQueue<Q extends java.util.Queue<E>, E> extends ScQueue<E> {

	protected final Q innerQueue;

	protected JctScQueue(Processor<E> processor, int capacity) {
		super(processor);
		this.innerQueue = createQueue(capacity);
	}

	@AbstractFunction
	protected abstract Q createQueue(int capacity);

}

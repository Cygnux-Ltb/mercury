package io.mercury.common.concurrent.queue.jct.impl;

import static io.mercury.common.number.ThreadSafeRandoms.randomInt;
import static io.mercury.common.util.StringUtil.isNullOrEmpty;

import javax.annotation.Nonnull;

import org.jctools.queues.SpmcArrayQueue;

import io.mercury.common.concurrent.queue.base.McQueue;

public class JctSpmcQueue<E> implements McQueue<E> {

	private final String queueName;
	private final SpmcArrayQueue<E> queue;

	public JctSpmcQueue(int capacity) {
		this("", capacity);
	}

	public JctSpmcQueue(@Nonnull String queueName, int capacity) {
		this.queueName = isNullOrEmpty(queueName) ? "JctSPMCQueue-" + randomInt() : queueName;
		this.queue = new SpmcArrayQueue<>(capacity);
	}

	@Override
	public boolean enqueue(E e) {
		if (e != null)
			return queue.offer(e);
		return false;
	}

	@Override
	public String queueName() {
		return queueName;
	}

	@Override
	public E dequeue() {
		queue.poll();
		return null;
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

}

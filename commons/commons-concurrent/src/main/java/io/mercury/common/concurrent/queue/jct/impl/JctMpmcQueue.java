package io.mercury.common.concurrent.queue.jct.impl;

import org.jctools.queues.MpmcArrayQueue;

import io.mercury.common.concurrent.queue.McQueue;
import io.mercury.common.concurrent.queue.QueueStyle;

public final class JctMpmcQueue<E> implements McQueue<E> {

	private final MpmcArrayQueue<E> queue;

	public JctMpmcQueue(int capacity) {
		this.queue = new MpmcArrayQueue<>(capacity);
	}

	@Override
	public boolean enqueue(E e) {
		return queue.offer(e);
	}

	@Override
	public String queueName() {
		return null;
	}

	@Override
	public E dequeue() {
		return null;
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	public QueueStyle getQueueStyle() {
		return QueueStyle.MPMC;
	}

}

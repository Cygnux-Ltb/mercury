package io.mercury.common.concurrent.queue.jct.impl;

import org.jctools.queues.MpmcArrayQueue;

import io.mercury.common.concurrent.queue.MultiConsumerQueue;
import io.mercury.common.concurrent.queue.QueueType;

public final class JctMpmcQueue<E> implements MultiConsumerQueue<E> {

	private final MpmcArrayQueue<E> queue;

	public JctMpmcQueue(int capacity) {
		this.queue = new MpmcArrayQueue<>(capacity);
	}

	@Override
	public boolean enqueue(E e) {
		return queue.offer(e);
	}

	@Override
	public String getQueueName() {
		return null;
	}

	@Override
	public E dequeue() {
		return queue.poll();
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	public QueueType getQueueType() {
		return QueueType.MPMC;
	}

}

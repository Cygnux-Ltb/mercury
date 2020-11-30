package io.mercury.common.concurrent.queue.jct;

import org.jctools.queues.MpmcArrayQueue;

import io.mercury.common.concurrent.queue.MCQueue;

public final class JctMPMCQueue<E> implements MCQueue<E> {

	private final MpmcArrayQueue<E> queue;

	public JctMPMCQueue(int capacity) {
		this.queue = new MpmcArrayQueue<>(capacity);
	}

	@Override
	public boolean enqueue(E e) {
		return queue.offer(e);
	}

	@Override
	public String queueName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E dequeue() {
		// TODO Auto-generated method stub
		return null;
	}

}

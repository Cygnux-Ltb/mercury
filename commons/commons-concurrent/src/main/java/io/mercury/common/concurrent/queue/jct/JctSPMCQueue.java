package io.mercury.common.concurrent.queue.jct;

import org.jctools.queues.SpmcArrayQueue;

import io.mercury.common.concurrent.queue.MCQueue;

public class JctSPMCQueue<E> implements MCQueue<E> {

	private final SpmcArrayQueue<E> queue;

	public JctSPMCQueue(int capacity) {
		this.queue = new SpmcArrayQueue<>(capacity);
	}

	@Override
	public boolean enqueue(E e) {
		queue.offer(e);
		return false;
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

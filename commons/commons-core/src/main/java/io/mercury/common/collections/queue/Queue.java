package io.mercury.common.collections.queue;

public interface Queue<E> {

	boolean enqueue(E e);

	String getQueueName();

	boolean isEmpty();

	QueueType getQueueType();

	public static enum QueueType {

		SPSC, SPMC, MPSC, MPMC

	}

}
package io.mercury.common.concurrent.queue;

public interface Queue<E> {

	boolean enqueue(E e);

	String getQueueName();

	boolean isEmpty();

	QueueType getQueueType();

}
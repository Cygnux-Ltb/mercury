package io.mercury.common.concurrent.queue;

public interface Queue<E> {

	boolean enqueue(E e);

	String queueName();

	boolean isEmpty();

	QueueStyle getQueueStyle();

}
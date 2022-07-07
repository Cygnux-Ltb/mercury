package io.mercury.common.collections.queue;

public interface Queue<E> {

    boolean enqueue(E e);

    String getQueueName();

    boolean isEmpty();

    QueueType getQueueType();

    enum QueueType {

        OneToOne, OneToMany, ManyToOne, ManyToMany

    }

}
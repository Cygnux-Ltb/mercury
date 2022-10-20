package io.mercury.common.concurrent.queue.jct;

import io.mercury.common.annotation.thread.SpinLock;
import io.mercury.common.collections.Capacity;
import io.mercury.common.concurrent.queue.MultiConsumerQueue;
import io.mercury.common.concurrent.queue.WaitingStrategy;
import io.mercury.common.thread.SleepSupport;
import io.mercury.common.thread.ThreadSupport;
import io.mercury.common.util.StringSupport;
import org.jctools.queues.MpmcArrayQueue;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class ConcurrentQueue<E> implements MultiConsumerQueue<E> {

    private final MpmcArrayQueue<E> queue;

    private final WaitingStrategy strategy;

    private final String queueName;

    public ConcurrentQueue(String queueName, Capacity capacity, WaitingStrategy strategy) {
        this.queue = new MpmcArrayQueue<>(Math.max(capacity.value(), 64));
        this.queueName = StringSupport.isNullOrEmpty(queueName)
                ? "ConcurrentQueue-" + ThreadSupport.getCurrentThreadName()
                : queueName;
        this.strategy = strategy == null ? WaitingStrategy.Sleep : strategy;
    }

    @Override
    @SpinLock
    public boolean enqueue(E e) {
        while (!queue.offer(e))
            waiting();
        return true;
    }

    @Override
    @SpinLock
    public E dequeue() {
        do {
            E e = queue.poll();
            if (e != null)
                return e;
            waiting();
        } while (true);
    }

    @Override
    public String getQueueName() {
        return queueName;
    }

    private void waiting() {
        switch (strategy) {
            case Spin, Blocking -> {
            }
            case Sleep -> SleepSupport.sleep(10);
        }
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public QueueType getQueueType() {
        return QueueType.ManyToMany;
    }

}

package io.mercury.common.concurrent.queue;

import org.jctools.queues.MessagePassingQueue;
import org.jctools.queues.MpmcArrayQueue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.atomic.AtomicInteger;

import static io.mercury.common.util.BitOperator.minPow2;

@ThreadSafe
public final class ConcurrentPriorityQueue<E> implements Comparable<ConcurrentPriorityQueue<E>> {

    private final long sequence;

    private final AtomicInteger counter = new AtomicInteger(0);

    private final MessagePassingQueue<E> priorityQueue;

    private final MessagePassingQueue<E> normalQueue;

    public ConcurrentPriorityQueue(long sequence, int priorityQueueSize, int normalQueueSize) {
        this.sequence = sequence;
        this.priorityQueue = new MpmcArrayQueue<>(minPow2(priorityQueueSize));
        this.normalQueue = new MpmcArrayQueue<>(minPow2(priorityQueueSize));
    }

    public long getSequence() {
        return sequence;
    }

    public boolean rematchWithdraw(@Nonnull E e) {
        boolean offer = priorityQueue.offer(e);
        if (offer) {
            counter.incrementAndGet();
        }
        return offer;
    }

    public boolean addWithdraw(@Nonnull E e) {
        boolean offer = normalQueue.offer(e);
        if (offer) {
            counter.incrementAndGet();
        }
        return offer;
    }

    public int count() {
        return counter.get();
    }

    @Nullable
    public E getFirst() {
        if (!priorityQueue.isEmpty()) {
            return priorityQueue.poll();
        }
        if (!normalQueue.isEmpty()) {
            return normalQueue.poll();
        }
        return null;
    }


    public static void main(String[] args) {


    }


    @Override
    public int compareTo(ConcurrentPriorityQueue<E> o) {
        return Long.compare(sequence, o.sequence);
    }
}

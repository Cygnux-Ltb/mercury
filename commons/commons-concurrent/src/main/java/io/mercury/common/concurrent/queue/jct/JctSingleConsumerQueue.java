package io.mercury.common.concurrent.queue.jct;

import java.util.Queue;

import org.jctools.queues.MpscArrayQueue;
import org.jctools.queues.SpscArrayQueue;
import org.slf4j.Logger;

import io.mercury.common.annotation.AbstractFunction;
import io.mercury.common.annotation.thread.SpinLock;
import io.mercury.common.concurrent.queue.SingleConsumerQueue;
import io.mercury.common.concurrent.queue.QueueWorkingException;
import io.mercury.common.concurrent.queue.WaitingStrategy;
import io.mercury.common.functional.Processor;
import io.mercury.common.log.Log4j2LoggerFactory;
import io.mercury.common.thread.SleepSupport;
import io.mercury.common.thread.ThreadSupport;
import io.mercury.common.util.StringSupport;

/**
 * @param <E> Single consumer queue use jctools implements
 * @author yellow013
 */
public abstract class JctSingleConsumerQueue<E> extends SingleConsumerQueue<E> implements Runnable {

    /**
     * Logger
     */
    private static final Logger log = Log4j2LoggerFactory.getLogger(JctSingleConsumerQueue.class);

    /**
     * internal queue
     */
    protected final Queue<E> queue;

    /**
     * consumer runnable
     */
    protected final Runnable consumer;

    /**
     * waiting strategy
     */
    private final WaitingStrategy strategy;

    /**
     * sleep millis
     */
    private final long sleepMillis;

    /**
     * Single Producer Single Consumer Queue
     *
     * @return Builder
     */
    public static Builder spscQueue() {
        return new Builder(QueueType.OneToOne);
    }

    /**
     * Single Producer Single Consumer Queue
     *
     * @param name String
     * @return Builder
     */
    public static Builder spscQueue(String name) {
        return new Builder(QueueType.OneToOne).setQueueName(name);
    }

    /**
     * Multiple Producer Single Consumer Queue
     *
     * @return Builder
     */
    public static Builder mpscQueue() {
        return new Builder(QueueType.ManyToOne);
    }

    /**
     * Multiple Producer Single Consumer Queue
     *
     * @param name String
     * @return Builder
     */
    public static Builder mpscQueue(String name) {
        return new Builder(QueueType.ManyToOne).setQueueName(name);
    }

    /**
     * @param processor   Processor<E>
     * @param capacity    int
     * @param strategy    WaitingStrategy
     * @param sleepMillis long
     */
    protected JctSingleConsumerQueue(Processor<E> processor, int capacity,
                                     WaitingStrategy strategy, long sleepMillis) {
        super(processor);
        this.queue = createQueue(capacity);
        this.strategy = strategy;
        this.sleepMillis = sleepMillis;
        this.consumer = () -> {
            try {
                while (isRunning() || !queue.isEmpty()) {
                    @SpinLock
                    E e = queue.poll();
                    if (e != null)
                        processor.process(e);
                    else
                        waiting();
                }
            } catch (Exception e) {
                throw new QueueWorkingException(name + " process thread throw exception", e);
            }
        };
    }

    @AbstractFunction
    protected abstract Queue<E> createQueue(int capacity);

    /**
     *
     */
    private void waiting() {
        if (strategy == WaitingStrategy.Sleep) {
            SleepSupport.sleepIgnoreInterrupts(sleepMillis);
        }
    }

    @Override
    @SpinLock
    public boolean enqueue(E e) {
        if (isClosed.get()) {
            log.error("Queue -> {} : enqueue failure, This queue is closed", name);
            return false;
        }
        if (e == null) {
            log.error("Queue -> {} : enqueue element is null", name);
            return false;
        }
        while (!queue.offer(e))
            waiting();
        return true;
    }

    @Override
    public void run() {
        consumer.run();
    }

    @Override
    protected void start0() {
        ThreadSupport.startNewThread(name + "-SubThread", consumer);
        log.info("Queue -> {}, This queue is already working", name);
    }

    @Override
    protected void stop0() {
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * @param <E> Single Producer Single Consumer Queue
     * @author yellow013
     */
    private static final class JctSpscQueue<E> extends JctSingleConsumerQueue<E> {

        private JctSpscQueue(String queueName, int capacity, StartMode mode,
                             WaitingStrategy strategy, long sleepMillis,
                             Processor<E> processor) {
            super(processor, Math.max(capacity, 16), strategy, sleepMillis);
            super.name = StringSupport.isNullOrEmpty(queueName)
                    ? "JctSpscQueue-" + ThreadSupport.getCurrentThreadName()
                    : queueName;
            startWith(mode);
        }

        @Override
        protected SpscArrayQueue<E> createQueue(int capacity) {
            return new SpscArrayQueue<>(capacity);
        }

        @Override
        public QueueType getQueueType() {
            return QueueType.OneToOne;
        }

    }

    /**
     * @param <E> Multiple Producer Single Consumer Queue
     * @author yellow013
     */
    private static final class JctMpscQueue<E> extends JctSingleConsumerQueue<E> {

        private JctMpscQueue(String queueName, int capacity, StartMode mode,
                             WaitingStrategy strategy, long sleepMillis,
                             Processor<E> processor) {
            super(processor, Math.max(capacity, 16), strategy, sleepMillis);
            super.name = StringSupport.isNullOrEmpty(queueName)
                    ? "JctMpscQueue-" + ThreadSupport.getCurrentThreadName()
                    : queueName;
            startWith(mode);
        }

        @Override
        protected Queue<E> createQueue(int capacity) {
            return new MpscArrayQueue<>(capacity);
        }

        @Override
        public QueueType getQueueType() {
            return QueueType.ManyToOne;
        }

    }

    /**
     * JctQueue Builder
     *
     * @author yellow013
     */
    public static class Builder {

        private final QueueType type;
        private String queueName = null;
        private StartMode mode = StartMode.auto();
        private WaitingStrategy strategy = WaitingStrategy.Spin;
        private long sleepMillis = 5;
        private int capacity = 32;

        private Builder(QueueType type) {
            this.type = type;
        }

        public Builder setQueueName(String queueName) {
            this.queueName = queueName;
            return this;
        }

        public Builder setStartMode(StartMode mode) {
            this.mode = mode;
            return this;
        }

        public Builder useSpinStrategy() {
            this.strategy = WaitingStrategy.Spin;
            return this;
        }

        public Builder useSleepStrategy(long sleepMillis) {
            this.strategy = WaitingStrategy.Sleep;
            if (sleepMillis > 0)
                this.sleepMillis = sleepMillis;
            return this;
        }

        public Builder setCapacity(int capacity) {
            this.capacity = capacity;
            return this;
        }

        public final <E> JctSingleConsumerQueue<E> process(Processor<E> processor) {
            return switch (type) {
                case OneToOne -> new JctSpscQueue<>(queueName, capacity, mode, strategy, sleepMillis, processor);
                case ManyToOne -> new JctMpscQueue<>(queueName, capacity, mode, strategy, sleepMillis, processor);
                default -> throw new IllegalArgumentException("Error enum value");
            };
        }
    }

}

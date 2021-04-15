package io.mercury.common.concurrent.queue.jct;

import java.util.Queue;

import org.jctools.queues.MpscArrayQueue;
import org.jctools.queues.SpscArrayQueue;
import org.slf4j.Logger;

import io.mercury.common.annotation.lang.AbstractFunction;
import io.mercury.common.annotation.thread.SpinLock;
import io.mercury.common.concurrent.queue.QueueStyle;
import io.mercury.common.concurrent.queue.QueueWorkingException;
import io.mercury.common.concurrent.queue.SingleConsumerQueue;
import io.mercury.common.concurrent.queue.StartMode;
import io.mercury.common.concurrent.queue.WaitingStrategy;
import io.mercury.common.functional.Processor;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.Threads;
import io.mercury.common.util.StringUtil;

/**
 * @author yellow013
 *
 * @param <T>
 * 
 *            Single Consumer Queue
 * 
 */
public abstract class JctSingleConsumerQueue<E> extends SingleConsumerQueue<E> {

	// Logger
	private static final Logger log = CommonLoggerFactory.getLogger(JctSingleConsumerQueue.class);

	// internal queue
	protected final Queue<E> queue;

	// consumer runnable
	protected final Runnable consumer;

	// waiting strategy
	private final WaitingStrategy strategy;

	/**
	 * Single Producer Single Consumer Queue
	 * 
	 * @return
	 */
	public static Builder singleProducer() {
		return new Builder(QueueStyle.SPSC);
	}

	/**
	 * Single Producer Single Consumer Queue
	 * 
	 * @param queueName
	 * @return
	 */
	public static Builder singleProducer(String queueName) {
		return new Builder(QueueStyle.SPSC).setQueueName(queueName);
	}

	/**
	 * Multiple Producer Single Consumer Queue
	 * 
	 * @return
	 */
	public static Builder multiProducer() {
		return new Builder(QueueStyle.MPSC);
	}

	/**
	 * Multiple Producer Single Consumer Queue
	 * 
	 * @param queueName
	 * @return
	 */
	public static Builder multiProducer(String queueName) {
		return new Builder(QueueStyle.MPSC).setQueueName(queueName);
	}

	protected JctSingleConsumerQueue(Processor<E> processor, int capacity, WaitingStrategy strategy) {
		super(processor);
		this.queue = createQueue(capacity);
		this.strategy = strategy;
		this.consumer = () -> {
			try {
				while (isRunning.get() || !queue.isEmpty()) {
					@SpinLock
					E e = queue.poll();
					if (e != null)
						processor.process(e);
					else
						waiting();
				}
			} catch (Exception e) {
				throw new QueueWorkingException(queueName + " process thread throw exception", e);
			}
		};

	}

	@AbstractFunction
	protected abstract Queue<E> createQueue(int capacity);

	/**
	 * 
	 */
	private void waiting() {
		switch (strategy) {
		case SpinWaiting:
			break;
		case SleepWaiting:
			Threads.sleepIgnoreInterrupts(10);
			break;
		default:
			break;
		}
	}

	@Override
	@SpinLock
	public boolean enqueue(E e) {
		if (isClosed.get()) {
			log.error("Queue -> {}, enqueue failure, This queue is closed", queueName);
			return false;
		}
		if (e == null) {
			log.error("Queue -> {}, enqueue element is null", queueName);
			return false;
		}
		while (!queue.offer(e))
			waiting();
		return true;
	}

	@Override
	protected void startProcessThread() {
		if (isRunning.compareAndSet(false, true)) {
			Threads.startNewMaxPriorityThread(queueName + "-ProcessThread", consumer);
		} else {
			log.error("Queue -> {}, Error call, This queue is started", queueName);
			return;
		}
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	/**
	 * 
	 * @author yellow013
	 *
	 * @param <E>
	 * 
	 *            Single Producer Single Consumer Queue
	 * 
	 */
	private static final class JctSpscQueue<E> extends JctSingleConsumerQueue<E> {

		private static final Logger log = CommonLoggerFactory.getLogger(JctSpscQueue.class);

		private JctSpscQueue(String queueName, int capacity, StartMode mode, WaitingStrategy strategy,
				Processor<E> processor) {
			super(processor, Math.max(capacity, 16), strategy);
			super.queueName = StringUtil.isNullOrEmpty(queueName) ? "JctSpscQueue-" + Threads.currentThreadName()
					: queueName;
			switch (mode) {
			case Auto:
				start();
				break;
			case Manual:
				log.info("Queue -> {} :: Run mode is [Manual], wating start...", super.queueName);
				break;
			}
		}

		@Override
		protected SpscArrayQueue<E> createQueue(int capacity) {
			return new SpscArrayQueue<>(capacity);
		}

		@Override
		public QueueStyle getQueueStyle() {
			return QueueStyle.SPSC;
		}

	}

	/**
	 * 
	 * @author yellow013
	 *
	 * @param <E>
	 * 
	 *            Multiple Producer Single Consumer Queue
	 * 
	 */
	private static final class JctMpscQueue<E> extends JctSingleConsumerQueue<E> {

		private static final Logger log = CommonLoggerFactory.getLogger(JctMpscQueue.class);

		private JctMpscQueue(String queueName, int capacity, StartMode mode, WaitingStrategy strategy,
				Processor<E> processor) {
			super(processor, Math.max(capacity, 16), strategy);
			super.queueName = StringUtil.isNullOrEmpty(queueName) ? "JctMpscQueue-" + Threads.currentThreadName()
					: queueName;
			switch (mode) {
			case Auto:
				start();
				break;
			case Manual:
				log.info("Queue -> {} :: Run mode is [Manual], wating start...", super.queueName);
				break;
			}
		}

		@Override
		protected Queue<E> createQueue(int capacity) {
			return new MpscArrayQueue<>(capacity);
		}

		@Override
		public QueueStyle getQueueStyle() {
			return QueueStyle.MPSC;
		}
	}

	/**
	 * 
	 * JctQueue Builder
	 * 
	 * @author yellow013
	 */
	public static class Builder {

		private final QueueStyle style;
		private String queueName = null;
		private StartMode mode = StartMode.Auto;
		private WaitingStrategy strategy = WaitingStrategy.SpinWaiting;
		private int capacity = 32;

		private Builder(QueueStyle style) {
			this.style = style;
		}

		public Builder setQueueName(String queueName) {
			this.queueName = queueName;
			return this;
		}

		public Builder setStartMode(StartMode mode) {
			this.mode = mode;
			return this;
		}

		public Builder setWaitingStrategy(WaitingStrategy strategy) {
			this.strategy = strategy;
			return this;
		}

		public Builder setCapacity(int capacity) {
			this.capacity = capacity;
			return this;
		}

		public final <E> JctSingleConsumerQueue<E> buildWithProcessor(Processor<E> processor) {
			switch (style) {
			case SPSC:
				return new JctSpscQueue<>(queueName, capacity, mode, strategy, processor);
			case MPSC:
				return new JctMpscQueue<>(queueName, capacity, mode, strategy, processor);
			default:
				throw new IllegalArgumentException("Error enum item");
			}
		}

	}

}

package io.mercury.common.concurrent.queue.jct;

import java.util.Queue;

import org.jctools.queues.MpscArrayQueue;
import org.jctools.queues.SpscArrayQueue;
import org.slf4j.Logger;

import io.mercury.common.annotation.AbstractFunction;
import io.mercury.common.annotation.thread.SpinLock;
import io.mercury.common.concurrent.queue.QueueStyle;
import io.mercury.common.concurrent.queue.QueueWorkingException;
import io.mercury.common.concurrent.queue.SingleConsumerQueue;
import io.mercury.common.concurrent.queue.StartMode;
import io.mercury.common.concurrent.queue.WaitingStrategy;
import io.mercury.common.functional.Processor;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.SleepSupport;
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

	// sleep millis
	private final long sleepMillis;

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

	protected JctSingleConsumerQueue(Processor<E> processor, int capacity, WaitingStrategy strategy, long sleepMillis) {
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
		case Spin:
			break;
		case Sleep:
			SleepSupport.sleepIgnoreInterrupts(sleepMillis);
			break;
		default:
			break;
		}
	}

	@Override
	@SpinLock
	public boolean enqueue(E e) {
		if (isClosed.get()) {
			log.error("Queue -> {} : enqueue failure, This queue is closed", queueName);
			return false;
		}
		if (e == null) {
			log.error("Queue -> {} : enqueue element is null", queueName);
			return false;
		}
		while (!queue.offer(e))
			waiting();
		return true;
	}

	@Override
	protected void start0() {
		Threads.startNewMaxPriorityThread(queueName + "-ProcessThread", consumer);
		log.info("Queue -> {}, Error call, This queue is started", queueName);
	}

	@Override
	protected void stop0() throws Exception {
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

		private JctSpscQueue(String queueName, int capacity, StartMode mode, WaitingStrategy strategy, long sleepMillis,
				Processor<E> processor) {
			super(processor, Math.max(capacity, 16), strategy, sleepMillis);
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

		private JctMpscQueue(String queueName, int capacity, StartMode mode, WaitingStrategy strategy, long sleepMillis,
				Processor<E> processor) {
			super(processor, Math.max(capacity, 16), strategy, sleepMillis);
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
		private WaitingStrategy strategy = WaitingStrategy.Spin;
		private long sleepMillis = 4;
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

		public final <E> JctSingleConsumerQueue<E> buildWithProcessor(Processor<E> processor) {
			switch (style) {
			case SPSC:
				return new JctSpscQueue<>(queueName, capacity, mode, strategy, sleepMillis, processor);
			case MPSC:
				return new JctMpscQueue<>(queueName, capacity, mode, strategy, sleepMillis, processor);
			default:
				throw new IllegalArgumentException("Error enum item");
			}
		}

	}

}

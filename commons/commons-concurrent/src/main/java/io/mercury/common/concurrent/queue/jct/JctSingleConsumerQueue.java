package io.mercury.common.concurrent.queue.jct;

import java.util.Queue;

import org.jctools.queues.MpscArrayQueue;
import org.jctools.queues.SpscArrayQueue;
import org.slf4j.Logger;

import io.mercury.common.annotation.lang.AbstractFunction;
import io.mercury.common.annotation.thread.SpinWaiting;
import io.mercury.common.concurrent.queue.QueueStyle;
import io.mercury.common.concurrent.queue.QueueWorkingException;
import io.mercury.common.concurrent.queue.ScQueue;
import io.mercury.common.concurrent.queue.StartMode;
import io.mercury.common.concurrent.queue.WaitingStrategy;
import io.mercury.common.disruptor.SpscQueue;
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
public abstract class JctSingleConsumerQueue<E> extends ScQueue<E> {

	/**
	 * 
	 */
	protected final Queue<E> queue;

	/**
	 * 
	 */
	protected final Runnable queueRunnable;

	/**
	 * 
	 */
	private final WaitingStrategy strategy;

	protected JctSingleConsumerQueue(Processor<E> processor, int capacity, WaitingStrategy strategy) {
		super(processor);
		this.queue = createQueue(capacity);
		this.strategy = strategy;
		this.queueRunnable = () -> {
			try {
				while (isRunning.get() || !queue.isEmpty()) {
					@SpinWaiting
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
	protected void waiting() {
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

		private static final Logger log = CommonLoggerFactory.getLogger(SpscQueue.class);

		public JctSpscQueue(String queueName, int capacity, StartMode mode, WaitingStrategy strategy,
				Processor<E> processor) {
			super(processor, Math.max(capacity, 16), strategy);
			super.queueName = StringUtil.isNullOrEmpty(queueName) ? "JctSpscQueue-" + Threads.currentThreadName()
					: queueName;
			switch (mode) {
			case Auto:
				start();
				break;
			case Manual:
				log.info("JctSpscQueue -> {} :: Run mode is [Manual], wating start...", this.queueName);
				break;
			}
		}

		@Override
		protected SpscArrayQueue<E> createQueue(int capacity) {
			return new SpscArrayQueue<>(capacity);
		}

		@Override
		public boolean enqueue(E e) {
			if (!isClosed.get()) {
				log.error("JctSpscQueue -> {}, enqueue failure, This queue is closed", queueName);
				return false;
			}
			if (e == null)
				log.error("JctSpscQueue -> {}, enqueue element is null", queueName);
			while (!queue.offer(e))
				waiting();
			return true;
		}

		@Override
		protected void startProcessThread() {
			if (!isRunning.compareAndSet(false, true)) {
				log.error("JctSpscQueue -> {}, Error call, This queue is started", queueName);
				return;
			}
			Threads.startNewMaxPriorityThread(queueName + "-ProcessThread", queueRunnable);
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
			this.queueName = StringUtil.isNullOrEmpty(queueName) ? "JctMPSCQueue-" + Threads.currentThreadName()
					: queueName;
			switch (mode) {
			case Auto:
				start();
				break;
			case Manual:
				log.info("JctMpscQueue -> {} :: Run mode is [Manual], wating start...", this.queueName);
				break;
			}
		}

		@Override
		protected Queue<E> createQueue(int capacity) {
			return new MpscArrayQueue<>(capacity);
		}

		@Override
		@SpinWaiting
		public boolean enqueue(E e) {
			if (!isClosed.get()) {
				log.error("JctMpscQueue -> {}, enqueue failure, This queue is closed", queueName);
				return false;
			}
			if (e == null)
				log.error("JctMpscQueue -> {}, enqueue element is null", queueName);
			while (!queue.offer(e))
				waiting();
			return true;
		}

		@Override
		protected void startProcessThread() {
			if (!isRunning.compareAndSet(false, true)) {
				log.error("JctMPSCQueue -> {}, Error call, This queue is started", queueName);
				return;
			}
			Threads.startNewThread(queueName + "-ProcessThread", queueRunnable);
		}

		@Override
		public QueueStyle getQueueStyle() {
			return QueueStyle.MPSC;
		}
	}

	/**
	 * Single Producer Single Consumer Queue
	 * 
	 * @return
	 */
	public static JctQueueBuilder newSingleProducerQueue() {
		return new JctQueueBuilder(QueueStyle.SPSC);
	}

	/**
	 * Single Producer Single Consumer Queue
	 * 
	 * @param queueName
	 * @return
	 */
	public static JctQueueBuilder newSingleProducerQueue(String queueName) {
		return new JctQueueBuilder(QueueStyle.SPSC).queueName(queueName);
	}

	/**
	 * Multiple Producer Single Consumer Queue
	 * 
	 * @return
	 */
	public static JctQueueBuilder newMultiProducersQueue() {
		return new JctQueueBuilder(QueueStyle.MPSC);
	}

	/**
	 * Multiple Producer Single Consumer Queue
	 * 
	 * @param queueName
	 * @return
	 */
	public static JctQueueBuilder newMultiProducersQueue(String queueName) {
		return new JctQueueBuilder(QueueStyle.MPSC).queueName(queueName);
	}

	/**
	 * 
	 * JctQueue Builder
	 * 
	 * @author yellow013
	 */
	public static class JctQueueBuilder {

		private final QueueStyle style;
		private String queueName = null;
		private StartMode mode = StartMode.Auto;
		private WaitingStrategy strategy = WaitingStrategy.SpinWaiting;
		private int capacity = 32;

		private JctQueueBuilder(QueueStyle style) {
			this.style = style;
		}

		public JctQueueBuilder queueName(String queueName) {
			this.queueName = queueName;
			return this;
		}

		public JctQueueBuilder startMode(StartMode mode) {
			this.mode = mode;
			return this;
		}

		public JctQueueBuilder waitingStrategy(WaitingStrategy strategy) {
			this.strategy = strategy;
			return this;
		}

		public JctQueueBuilder capacity(int capacity) {
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

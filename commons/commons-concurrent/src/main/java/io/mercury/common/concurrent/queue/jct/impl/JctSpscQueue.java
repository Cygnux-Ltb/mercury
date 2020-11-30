package io.mercury.common.concurrent.queue.jct;

import java.util.concurrent.TimeUnit;

import org.jctools.queues.SpscArrayQueue;
import org.slf4j.Logger;

import io.mercury.common.annotation.thread.SpinWaiting;
import io.mercury.common.concurrent.disruptor.SpscQueue;
import io.mercury.common.concurrent.queue.QueueWorkingException;
import io.mercury.common.concurrent.queue.base.JctSCQueue;
import io.mercury.common.functional.Processor;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.Threads;
import io.mercury.common.util.StringUtil;

public final class JctSPSCQueue<E> extends JctSCQueue<SpscArrayQueue<E>, E> {

	private static final Logger log = CommonLoggerFactory.getLogger(SpscQueue.class);

	private final WaitingStrategy strategy;

	private JctSPSCQueue(String queueName, int capacity, StartMode startMode, long delayMillis,
			WaitingStrategy strategy, Processor<E> processor) {
		super(processor, Math.max(capacity, 16));
		super.queueName = StringUtil.isNullOrEmpty(queueName) ? "JctSPSCQueue-" + Threads.currentThreadName()
				: queueName;
		this.strategy = strategy;
		switch (startMode) {
		case Auto:
			start();
			break;
		case Delay:
			Threads.startNewThread(() -> {
				Threads.sleep(delayMillis);
				start();
			});
			break;
		case Manual:
			log.info("JctSPSCQueue :: Start mode is [Manual], wating start...");
			break;
		}
	}

	public static <E> JctSPSCQueue<E> autoStartQueue(WaitingStrategy waitingStrategy, Processor<E> processor) {
		return new JctSPSCQueue<>(null, 8, StartMode.Auto, 0L, waitingStrategy, processor);
	}

	public static <E> JctSPSCQueue<E> autoStartQueue(int capacity, WaitingStrategy waitingStrategy,
			Processor<E> processor) {
		return new JctSPSCQueue<>(null, capacity, StartMode.Auto, 0L, waitingStrategy, processor);
	}

	public static <E> JctSPSCQueue<E> autoStartQueue(String queueName, int capacity, WaitingStrategy waitingStrategy,
			Processor<E> processor) {
		return new JctSPSCQueue<>(queueName, capacity, StartMode.Auto, 0L, waitingStrategy, processor);
	}

	public static <E> JctSPSCQueue<E> manualStartQueue(WaitingStrategy waitingStrategy, Processor<E> processor) {
		return new JctSPSCQueue<>(null, 8, StartMode.Manual, 0L, waitingStrategy, processor);
	}

	public static <E> JctSPSCQueue<E> manualStartQueue(int capacity, WaitingStrategy waitingStrategy,
			Processor<E> processor) {
		return new JctSPSCQueue<>(null, capacity, StartMode.Manual, 0L, waitingStrategy, processor);
	}

	public static <E> JctSPSCQueue<E> manualStartQueue(String queueName, int capacity, WaitingStrategy waitingStrategy,
			Processor<E> processor) {
		return new JctSPSCQueue<>(queueName, capacity, StartMode.Manual, 0L, waitingStrategy, processor);
	}

	public static <E> JctSPSCQueue<E> delayStartQueue(long delay, TimeUnit timeUnit, WaitingStrategy waitingStrategy,
			Processor<E> processor) {
		return new JctSPSCQueue<>(null, 8, StartMode.Delay, timeUnit.toMillis(delay), waitingStrategy, processor);
	}

	public static <E> JctSPSCQueue<E> delayStartQueue(int capacity, long delay, TimeUnit timeUnit,
			WaitingStrategy waitingStrategy, Processor<E> processor) {
		return new JctSPSCQueue<>(null, capacity, StartMode.Delay, timeUnit.toMillis(delay), waitingStrategy,
				processor);
	}

	public static <E> JctSPSCQueue<E> delayStartQueue(String queueName, int capacity, long delay, TimeUnit timeUnit,
			WaitingStrategy waitingStrategy, Processor<E> processor) {
		return new JctSPSCQueue<>(queueName, capacity, StartMode.Delay, timeUnit.toMillis(delay), waitingStrategy,
				processor);
	}

	@Override
	protected SpscArrayQueue<E> createQueue(int capacity) {
		return new SpscArrayQueue<>(capacity);
	}

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
	@SpinWaiting
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
	public void startProcessThread() {
		if (!isRunning.compareAndSet(false, true)) {
			log.error("JctSpscQueue -> {}, Error call, This queue is started", queueName);
			return;
		}
		Threads.startNewMaxPriorityThread(queueName + "-ProcessThread", () -> {
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
		});
	}

	public static void main(String[] args) {

		JctSPSCQueue<Integer> spscQueue = JctSPSCQueue.autoStartQueue(6, WaitingStrategy.SleepWaiting, (value) -> {
			System.out.println(value);
			Threads.sleep(500);
		});

		int i = 0;

		System.out.println(spscQueue.queueName());
		for (;;) {
			spscQueue.enqueue(++i);
			System.out.println("enqueue ->" + i);
			System.out.println("size -> " + spscQueue.queue.size());
			System.out.println("capacity -> " + spscQueue.queue.capacity());
		}

	}

}
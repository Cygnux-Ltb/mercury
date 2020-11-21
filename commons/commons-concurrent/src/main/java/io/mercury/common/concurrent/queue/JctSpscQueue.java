package io.mercury.common.concurrent.queue;

import java.util.concurrent.TimeUnit;

import org.jctools.queues.SpscArrayQueue;
import org.slf4j.Logger;

import io.mercury.common.annotation.thread.SpinWaiting;
import io.mercury.common.concurrent.disruptor.SpscQueue;
import io.mercury.common.concurrent.queue.base.JctScQueue;
import io.mercury.common.functional.Processor;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.Threads;
import io.mercury.common.util.StringUtil;

public class JctSpscQueue<E> extends JctScQueue<SpscArrayQueue<E>, E> {

	private static final Logger log = CommonLoggerFactory.getLogger(SpscQueue.class);

	private final WaitingStrategy strategy;

	private JctSpscQueue(String queueName, int capacity, StartMode mode, long delayMillis, WaitingStrategy strategy,
			Processor<E> processor) {
		super(processor, Math.max(capacity, 16));
		super.queueName = StringUtil.isNullOrEmpty(queueName)
				? this.getClass().getSimpleName() + "-" + Threads.currentThreadName()
				: queueName;
		this.strategy = strategy;
		switch (mode) {
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
			log.info("JctSpscQueue :: Run mode is [Manual], wating start...");
			break;
		}
	}

	public static <E> JctSpscQueue<E> autoStartQueue(WaitingStrategy waitingStrategy, Processor<E> processor) {
		return new JctSpscQueue<>(null, 8, StartMode.Auto, 0L, waitingStrategy, processor);
	}

	public static <E> JctSpscQueue<E> autoStartQueue(int capacity, WaitingStrategy waitingStrategy,
			Processor<E> processor) {
		return new JctSpscQueue<>(null, capacity, StartMode.Auto, 0L, waitingStrategy, processor);
	}

	public static <E> JctSpscQueue<E> autoStartQueue(String queueName, int capacity, WaitingStrategy waitingStrategy,
			Processor<E> processor) {
		return new JctSpscQueue<>(queueName, capacity, StartMode.Auto, 0L, waitingStrategy, processor);
	}

	public static <E> JctSpscQueue<E> manualStartQueue(WaitingStrategy waitingStrategy, Processor<E> processor) {
		return new JctSpscQueue<>(null, 8, StartMode.Manual, 0L, waitingStrategy, processor);
	}

	public static <E> JctSpscQueue<E> manualStartQueue(int capacity, WaitingStrategy waitingStrategy,
			Processor<E> processor) {
		return new JctSpscQueue<>(null, capacity, StartMode.Manual, 0L, waitingStrategy, processor);
	}

	public static <E> JctSpscQueue<E> manualStartQueue(String queueName, int capacity, WaitingStrategy waitingStrategy,
			Processor<E> processor) {
		return new JctSpscQueue<>(queueName, capacity, StartMode.Manual, 0L, waitingStrategy, processor);
	}

	public static <E> JctSpscQueue<E> delayStartQueue(long delay, TimeUnit timeUnit, WaitingStrategy waitingStrategy,
			Processor<E> processor) {
		return new JctSpscQueue<>(null, 8, StartMode.Delay, timeUnit.toMillis(delay), waitingStrategy, processor);
	}

	public static <E> JctSpscQueue<E> delayStartQueue(int capacity, long delay, TimeUnit timeUnit,
			WaitingStrategy waitingStrategy, Processor<E> processor) {
		return new JctSpscQueue<>(null, capacity, StartMode.Delay, timeUnit.toMillis(delay), waitingStrategy, processor);
	}

	public static <E> JctSpscQueue<E> delayStartQueue(String queueName, int capacity, long delay, TimeUnit timeUnit,
			WaitingStrategy waitingStrategy, Processor<E> processor) {
		return new JctSpscQueue<>(queueName, capacity, StartMode.Delay, timeUnit.toMillis(delay), waitingStrategy,
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
			Threads.sleep(20);
			break;
		default:
			break;
		}
	}

	@Override
	@SpinWaiting
	public boolean enqueue(E e) {
		if (!isClose.get()) {
			log.error("JctSpscQueue -> {}, enqueue failure, This queue is closed", queueName);
			return false;
		}
		if (e == null)
			log.error("JctSpscQueue -> {}, enqueue element is null", queueName);
		while (!innerQueue.offer(e))
			waiting();
		return true;
	}

	@Override
	public void startProcessThread() {
		if (!isRunning.compareAndSet(false, true)) {
			log.error("JctSpscQueue -> {}, Error call, This queue is started", queueName);
			return;
		}
		Threads.startNewMaxPriorityThread(() -> {
			try {
				while (isRunning.get() || !innerQueue.isEmpty()) {
					@SpinWaiting
					E e = innerQueue.poll();
					if (e != null)
						processor.process(e);
					else
						waiting();
				}
			} catch (Exception e) {
				throw new QueueWorkingException(queueName + " process thread throw exception", e);
			}
		}, queueName + "-ProcessThread");
	}

	public static void main(String[] args) {

		JctSpscQueue<Integer> queue = JctSpscQueue.autoStartQueue(6, WaitingStrategy.SleepWaiting, (value) -> {
			System.out.println(value);
			Threads.sleep(500);
		});

		int i = 0;

		System.out.println(queue.queueName());
		for (;;) {
			queue.enqueue(++i);
			System.out.println("enqueue ->" + i);
			System.out.println("size -> " + queue.innerQueue.size());
			System.out.println("capacity -> " + queue.innerQueue.capacity());
		}

	}

}
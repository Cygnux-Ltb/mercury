package io.mercury.common.concurrent.queue;

import java.util.concurrent.TimeUnit;

import org.jctools.queues.MpscArrayQueue;
import org.slf4j.Logger;

import io.mercury.common.annotation.thread.SpinWaiting;
import io.mercury.common.concurrent.queue.base.JctScQueue;
import io.mercury.common.functional.Processor;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.Threads;
import io.mercury.common.util.StringUtil;

public class JctMpscQueue<E> extends JctScQueue<MpscArrayQueue<E>, E> {

	private static final Logger log = CommonLoggerFactory.getLogger(JctMpscQueue.class);

	private JctMpscQueue(String queueName, int capacity, StartMode mode, long delayMillis, Processor<E> processor) {
		super(processor, Math.max(capacity, 16));
		this.queueName = StringUtil.isNullOrEmpty(queueName)
				? this.getClass().getSimpleName() + "-" + Threads.currentThreadName()
				: queueName;
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
			log.info("JctMpscQueue :: Run mode is [Manual], wating start...");
			break;
		}
	}

	public static <E> JctMpscQueue<E> autoStartQueue(Processor<E> processor) {
		return new JctMpscQueue<>(null, 64, StartMode.Auto, 0L, processor);
	}

	public static <E> JctMpscQueue<E> autoStartQueue(int capacity, Processor<E> processor) {
		return new JctMpscQueue<>(null, capacity, StartMode.Auto, 0L, processor);
	}

	public static <E> JctMpscQueue<E> autoStartQueue(String queueName, int capacity, Processor<E> processor) {
		return new JctMpscQueue<>(queueName, capacity, StartMode.Auto, 0L, processor);
	}

	public static <E> JctMpscQueue<E> manualStartQueue(Processor<E> processor) {
		return new JctMpscQueue<>(null, 64, StartMode.Manual, 0L, processor);
	}

	public static <E> JctMpscQueue<E> manualStartQueue(int capacity, Processor<E> processor) {
		return new JctMpscQueue<>(null, capacity, StartMode.Manual, 0L, processor);
	}

	public static <E> JctMpscQueue<E> manualStartQueue(String queueName, int capacity, Processor<E> processor) {
		return new JctMpscQueue<>(queueName, capacity, StartMode.Manual, 0L, processor);
	}

	public static <E> JctMpscQueue<E> delayStartQueue(long delay, TimeUnit timeUnit, Processor<E> processor) {
		return new JctMpscQueue<>(null, 64, StartMode.Delay, timeUnit.toMillis(delay), processor);
	}

	public static <E> JctMpscQueue<E> delayStartQueue(int capacity, long delay, TimeUnit timeUnit,
			Processor<E> processor) {
		return new JctMpscQueue<>(null, capacity, StartMode.Delay, timeUnit.toMillis(delay), processor);
	}

	public static <E> JctMpscQueue<E> delayStartQueue(String queueName, int capacity, long delay, TimeUnit timeUnit,
			Processor<E> processor) {
		return new JctMpscQueue<>(queueName, capacity, StartMode.Delay, timeUnit.toMillis(delay), processor);
	}

	@Override
	protected MpscArrayQueue<E> createQueue(int capacity) {
		return new MpscArrayQueue<>(capacity);
	}

	@Override
	@SpinWaiting
	public boolean enqueue(E e) {
		if (!isClose.get()) {
			log.error("JctMpscQueue -> {}, enqueue failure, This queue is closed", queueName);
			return false;
		}
		if (e == null)
			log.error("JctMpscQueue -> {}, enqueue element is null", queueName);
		while (!innerQueue.offer(e))
			;
		return true;
	}

	@Override
	public void startProcessThread() {
		if (!isRunning.compareAndSet(false, true)) {
			log.error("JctMpscQueue -> {}, Error call, This queue is started", queueName);
			return;
		}
		Threads.startNewThread(() -> {
			try {
				while (isRunning.get() || !innerQueue.isEmpty()) {
					@SpinWaiting
					E e = innerQueue.poll();
					if (e != null)
						processor.process(e);
				}
			} catch (Exception e) {
				throw new QueueWorkingException(queueName + " process thread throw exception", e);
			}
		}, queueName + "-ProcessThread");
	}

	@Override
	public void stop() {
		this.isRunning.set(false);
		this.isClose.set(true);
	}

	public static void main(String[] args) {
		JctMpscQueue<Integer> queue = JctMpscQueue.autoStartQueue(100, (value) -> {
			System.out.println(value);
			Threads.sleep(500);
		});
		int i = 0;
		System.out.println(queue.queueName());
		for (;;) {
			queue.enqueue(++i);
			System.out.println("enqueue ->" + i);
		}
	}

}

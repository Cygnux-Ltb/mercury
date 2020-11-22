package io.mercury.common.concurrent.queue;

import java.util.concurrent.TimeUnit;

import org.jctools.queues.MpscArrayQueue;
import org.slf4j.Logger;

import io.mercury.common.annotation.thread.SpinWaiting;
import io.mercury.common.concurrent.queue.base.JctSCQueue;
import io.mercury.common.functional.Processor;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.Threads;
import io.mercury.common.util.StringUtil;

public class JctMPSCQueue<E> extends JctSCQueue<MpscArrayQueue<E>, E> {

	private static final Logger log = CommonLoggerFactory.getLogger(JctMPSCQueue.class);

	private JctMPSCQueue(String queueName, int capacity, StartMode startMode, long delayMillis,
			Processor<E> processor) {
		super(processor, Math.max(capacity, 16));
		this.queueName = StringUtil.isNullOrEmpty(queueName)
				? this.getClass().getSimpleName() + "-" + Threads.currentThreadName()
				: queueName;
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
			log.info("JctMPSCQueue :: Run mode is [Manual], wating start...");
			break;
		}
	}

	public static <E> JctMPSCQueue<E> autoStartQueue(Processor<E> processor) {
		return new JctMPSCQueue<>(null, 64, StartMode.Auto, 0L, processor);
	}

	public static <E> JctMPSCQueue<E> autoStartQueue(int capacity, Processor<E> processor) {
		return new JctMPSCQueue<>(null, capacity, StartMode.Auto, 0L, processor);
	}

	public static <E> JctMPSCQueue<E> autoStartQueue(String queueName, int capacity, Processor<E> processor) {
		return new JctMPSCQueue<>(queueName, capacity, StartMode.Auto, 0L, processor);
	}

	public static <E> JctMPSCQueue<E> manualStartQueue(Processor<E> processor) {
		return new JctMPSCQueue<>(null, 64, StartMode.Manual, 0L, processor);
	}

	public static <E> JctMPSCQueue<E> manualStartQueue(int capacity, Processor<E> processor) {
		return new JctMPSCQueue<>(null, capacity, StartMode.Manual, 0L, processor);
	}

	public static <E> JctMPSCQueue<E> manualStartQueue(String queueName, int capacity, Processor<E> processor) {
		return new JctMPSCQueue<>(queueName, capacity, StartMode.Manual, 0L, processor);
	}

	public static <E> JctMPSCQueue<E> delayStartQueue(long delay, TimeUnit timeUnit, Processor<E> processor) {
		return new JctMPSCQueue<>(null, 64, StartMode.Delay, timeUnit.toMillis(delay), processor);
	}

	public static <E> JctMPSCQueue<E> delayStartQueue(int capacity, long delay, TimeUnit timeUnit,
			Processor<E> processor) {
		return new JctMPSCQueue<>(null, capacity, StartMode.Delay, timeUnit.toMillis(delay), processor);
	}

	public static <E> JctMPSCQueue<E> delayStartQueue(String queueName, int capacity, long delay, TimeUnit timeUnit,
			Processor<E> processor) {
		return new JctMPSCQueue<>(queueName, capacity, StartMode.Delay, timeUnit.toMillis(delay), processor);
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
		JctMPSCQueue<Integer> queue = JctMPSCQueue.autoStartQueue(100, (value) -> {
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

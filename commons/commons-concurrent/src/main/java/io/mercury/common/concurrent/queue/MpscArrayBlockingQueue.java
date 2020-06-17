package io.mercury.common.concurrent.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;

import io.mercury.common.annotation.thread.SpinWaiting;
import io.mercury.common.collections.queue.RunMode;
import io.mercury.common.collections.queue.api.SCQueue;
import io.mercury.common.functional.Processor;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.ThreadTool;
import io.mercury.common.util.StringUtil;

public class MpscArrayBlockingQueue<E> extends SCQueue<E> {

	private ArrayBlockingQueue<E> innerQueue;

	private Logger log = CommonLoggerFactory.getLogger(MpscArrayBlockingQueue.class);

	private AtomicBoolean isRun = new AtomicBoolean(false);

	private AtomicBoolean isClose = new AtomicBoolean(true);

	private MpscArrayBlockingQueue(String queueName, int capacity, RunMode mode, long delayMillis,
			Processor<E> processor) {
		super(processor);
		this.innerQueue = new ArrayBlockingQueue<>(Math.max(capacity, 64));
		this.queueName = StringUtil.isNullOrEmpty(queueName)
				? MpscArrayBlockingQueue.class.getSimpleName() + "-" + Thread.currentThread().getName()
				: queueName;
		switch (mode) {
		case Auto:
			start();
			break;
		case Delay:
			ThreadTool.sleep(delayMillis);
			start();
			break;
		default:
			break;
		}
	}

	public static <E> MpscArrayBlockingQueue<E> autoStartQueue(Processor<E> processor) {
		return new MpscArrayBlockingQueue<>(null, 64, RunMode.Auto, 0L, processor);
	}

	public static <E> MpscArrayBlockingQueue<E> autoStartQueue(int capacity, Processor<E> processor) {
		return new MpscArrayBlockingQueue<>(null, capacity, RunMode.Auto, 0L, processor);
	}

	public static <E> MpscArrayBlockingQueue<E> autoStartQueue(String queueName, int capacity, Processor<E> processor) {
		return new MpscArrayBlockingQueue<>(queueName, capacity, RunMode.Auto, 0L, processor);
	}

	public static <E> MpscArrayBlockingQueue<E> manualStartQueue(Processor<E> processor) {
		return new MpscArrayBlockingQueue<>(null, 64, RunMode.Manual, 0L, processor);
	}

	public static <E> MpscArrayBlockingQueue<E> manualStartQueue(int capacity, Processor<E> processor) {
		return new MpscArrayBlockingQueue<>(null, capacity, RunMode.Manual, 0L, processor);
	}

	public static <E> MpscArrayBlockingQueue<E> manualStartQueue(String queueName, int capacity,
			Processor<E> processor) {
		return new MpscArrayBlockingQueue<>(queueName, capacity, RunMode.Manual, 0L, processor);
	}

	public static <E> MpscArrayBlockingQueue<E> delayStartQueue(long delay, TimeUnit timeUnit, Processor<E> processor) {
		return new MpscArrayBlockingQueue<>(null, 64, RunMode.Delay, timeUnit.toMillis(delay), processor);
	}

	public static <E> MpscArrayBlockingQueue<E> delayStartQueue(int capacity, long delay, TimeUnit timeUnit,
			Processor<E> processor) {
		return new MpscArrayBlockingQueue<>(null, capacity, RunMode.Delay, timeUnit.toMillis(delay), processor);
	}

	public static <E> MpscArrayBlockingQueue<E> delayStartQueue(String queueName, int capacity, long delay,
			TimeUnit timeUnit, Processor<E> processor) {
		return new MpscArrayBlockingQueue<>(queueName, capacity, RunMode.Delay, timeUnit.toMillis(delay), processor);
	}

	@Override
	@SpinWaiting
	public boolean enqueue(E e) {
		if (!isClose.get()) {
			log.error("enqueue(t) failure, This queue is closed...");
			return false;
		}
		try {
			while (!innerQueue.offer(e, 100, TimeUnit.MILLISECONDS))
				;
			return true;
		} catch (InterruptedException exception) {
			log.error("innerQueue.offer(t, 500, TimeUnit.MILLISECONDS) throws InterruptedException!", exception);
			return false;
		}
	}

	@Override
	public void startProcessThread() {
		if (!isRun.compareAndSet(false, true)) {
			log.error("Error call ->  This queue is started.");
			return;
		}
		ThreadTool.startNewThread(() -> {
			try {
				while (isRun.get() || !innerQueue.isEmpty()) {
					@SpinWaiting
					E e = innerQueue.poll(500, TimeUnit.MILLISECONDS);
					if (e != null)
						processor.process(e);
				}
			} catch (InterruptedException e) {
				log.error("innerQueue.poll(500, TimeUnit.MILLISECONDS) throws InterruptedException!", e);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}, queueName);
	}

	@Override
	public void stop() {
		this.isRun.set(false);
		this.isClose.set(true);
	}

	public static void main(String[] args) {

		MpscArrayBlockingQueue<Integer> queue = MpscArrayBlockingQueue.autoStartQueue(100, (value) -> {
			System.out.println(value);
			ThreadTool.sleep(500);
		});

		int i = 0;

		System.out.println(queue.name());
		for (;;) {
			queue.enqueue(++i);
			System.out.println("enqueue ->" + i);
		}

	}

}

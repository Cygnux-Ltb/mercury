package io.mercury.common.concurrent.queue;

import org.jctools.queues.SpscArrayQueue;

import io.mercury.common.annotation.thread.OnlySingleThreadCall;
import io.mercury.common.annotation.thread.SpinWaiting;
import io.mercury.common.collections.Capacity;
import io.mercury.common.thread.Threads;
import io.mercury.common.util.StringUtil;

public class SPSCQueueDefault<E> implements Queue<E> {

	private final SpscArrayQueue<E> queue;

	private final WaitingStrategy strategy;

	private final String queueName;

	public SPSCQueueDefault(String queueName, Capacity capacity, WaitingStrategy strategy) {
		this.queue = new SpscArrayQueue<>(Math.max(capacity.size(), 64));
		this.queueName = StringUtil.isNullOrEmpty(queueName)
				? this.getClass().getSimpleName() + "-" + Threads.currentThreadName()
				: queueName;
		this.strategy = strategy == null ? WaitingStrategy.SleepWaiting : strategy;
	}

	@Override
	@SpinWaiting
	@OnlySingleThreadCall
	public boolean enqueue(E e) {
		while (!queue.offer(e))
			waiting();
		return true;
	}

	@OnlySingleThreadCall
	public E poll() {
		do {
			E e = queue.poll();
			if (e != null)
				return e;
			waiting();
		} while (true);
	}

	@Override
	public String queueName() {
		return queueName;
	}

	private void waiting() {
		switch (strategy) {
		case SpinWaiting:
			break;
		case SleepWaiting:
			Threads.sleep(10);
			break;
		default:
			break;
		}
	}

}

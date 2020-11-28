package io.mercury.common.concurrent.queue;

import org.jctools.queues.MpmcArrayQueue;

import io.mercury.common.annotation.thread.SpinWaiting;
import io.mercury.common.collections.Capacity;
import io.mercury.common.thread.Threads;
import io.mercury.common.util.StringUtil;

public class ConcurrentQueue<E> implements MCQueue<E> {

	private final MpmcArrayQueue<E> queue;

	private final WaitingStrategy strategy;

	private final String queueName;

	public ConcurrentQueue(String queueName, Capacity capacity, WaitingStrategy strategy) {
		this.queue = new MpmcArrayQueue<>(Math.max(capacity.size(), 64));
		this.queueName = StringUtil.isNullOrEmpty(queueName) 
				? "ConcurrentQueue-" + Threads.currentThreadName()
				: queueName;
		this.strategy = strategy == null ? WaitingStrategy.SleepWaiting : strategy;
	}

	@Override
	@SpinWaiting
	public boolean enqueue(E e) {
		while (!queue.offer(e))
			waiting();
		return true;
	}

	@Override
	public E dequeue() {
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

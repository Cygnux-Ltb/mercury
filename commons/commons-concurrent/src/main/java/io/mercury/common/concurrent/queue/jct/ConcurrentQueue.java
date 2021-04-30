package io.mercury.common.concurrent.queue.jct;

import javax.annotation.concurrent.ThreadSafe;

import org.jctools.queues.MpmcArrayQueue;

import io.mercury.common.annotation.thread.SpinLock;
import io.mercury.common.collections.Capacity;
import io.mercury.common.concurrent.queue.MultiConsumerQueue;
import io.mercury.common.concurrent.queue.QueueStyle;
import io.mercury.common.concurrent.queue.WaitingStrategy;
import io.mercury.common.thread.Threads;
import io.mercury.common.util.StringUtil;

@ThreadSafe
public final class ConcurrentQueue<E> implements MultiConsumerQueue<E> {

	private final MpmcArrayQueue<E> queue;

	private final WaitingStrategy strategy;

	private final String queueName;

	public ConcurrentQueue(String queueName, Capacity capacity, WaitingStrategy strategy) {
		this.queue = new MpmcArrayQueue<>(Math.max(capacity.value(), 64));
		this.queueName = StringUtil.isNullOrEmpty(queueName) ? "ConcurrentQueue-" + Threads.currentThreadName()
				: queueName;
		this.strategy = strategy == null ? WaitingStrategy.Sleep : strategy;
	}

	@Override
	@SpinLock
	public boolean enqueue(E e) {
		while (!queue.offer(e))
			waiting();
		return true;
	}

	@Override
	@SpinLock
	public E dequeue() {
		do {
			E e = queue.poll();
			if (e != null)
				return e;
			waiting();
		} while (true);
	}

	@Override
	public String getQueueName() {
		return queueName;
	}

	private void waiting() {
		switch (strategy) {
		case Spin:
			break;
		case Sleep:
			Threads.sleep(10);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	public QueueStyle getQueueStyle() {
		return QueueStyle.MPMC;
	}

}

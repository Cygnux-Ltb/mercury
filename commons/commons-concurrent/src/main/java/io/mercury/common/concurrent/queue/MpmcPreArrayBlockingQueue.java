package io.mercury.common.concurrent.queue;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;

import io.mercury.common.annotation.thread.LockHeld;
import io.mercury.common.collections.queue.api.MCQueue;
import io.mercury.common.collections.queue.base.LoadContainer;
import io.mercury.common.log.CommonLoggerFactory;

@ThreadSafe
public class MpmcPreArrayBlockingQueue<E> implements MCQueue<E> {

	private LoadContainer<E>[] containers;

	private final int size;
	private AtomicInteger count = new AtomicInteger();

	private volatile int readOffset;
	private volatile int writeOffset;

	private ReentrantLock lock;

	private Condition notEmpty;
	private Condition notFull;

	private Logger log = CommonLoggerFactory.getLogger(MpmcPreArrayBlockingQueue.class);

	@SuppressWarnings("unchecked")
	public MpmcPreArrayBlockingQueue(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("size is too big.");
		}
		this.containers = new LoadContainer[size];
		for (int i = 0; i < size; i++)
			containers[i] = new LoadContainer<>();
		this.size = size;
		this.lock = new ReentrantLock();
		this.notEmpty = lock.newCondition();
		this.notFull = lock.newCondition();
	}

	@Override
	@LockHeld
	public boolean enqueue(E e) {
		try {
			lock.lockInterruptibly();
			while (count.get() == size)
				notFull.await();
			containers[writeOffset].loading(e);
			if (++writeOffset == size)
				writeOffset = 0;
			count.incrementAndGet();
			notEmpty.signal();
			return true;
		} catch (InterruptedException exception) {
			log.error("PreloadingArrayBlockingQueue.enQueue(t)", exception);
			return false;
		} finally {
			lock.unlock();
		}
	}

	@Override
	@LockHeld
	public E dequeue() {
		try {
			lock.lockInterruptibly();
			while (count.get() == 0)
				notEmpty.await();
			E e = containers[readOffset].unloading();
			if (++readOffset == size)
				readOffset = 0;
			count.decrementAndGet();
			notFull.signal();
			return e;
		} catch (InterruptedException e) {
			log.error("PreloadingArrayBlockingQueue.deQueue() : " + e.getMessage());
			return null;
		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) {

	}

	@Override
	public String name() {
		return "MpmcPreloadingArrayBlockingQueue";
	}

}

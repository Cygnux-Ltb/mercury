package io.mercury.common.concurrent.queue;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;

import io.mercury.common.annotation.thread.LockHeld;
import io.mercury.common.collections.queue.LoadContainer;
import io.mercury.common.log.Log4j2LoggerFactory;

@ThreadSafe
public class PreloadingQueue<E> implements MultiConsumerQueue<E> {

	private static final Logger log = Log4j2LoggerFactory.getLogger(PreloadingQueue.class);

	private LoadContainer<E>[] containers;

	private final int size;
	private AtomicInteger count = new AtomicInteger();

	private volatile int readOffset;
	private volatile int writeOffset;

	private ReentrantLock lock;

	private Condition notEmpty;
	private Condition notFull;

	@SuppressWarnings("unchecked")
	public PreloadingQueue(int size) {
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
		} catch (InterruptedException ie) {
			log.error("PreloadingQueue.enqueue() -> {}", ie.getMessage());
			return false;
		} finally {
			lock.unlock();
		}
	}

	@Override
	@LockHeld
	@Nullable
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
		} catch (InterruptedException ie) {
			log.error("PreloadingQueue.dequeue() : {}", ie.getMessage());
			return null;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public String getQueueName() {
		return "PreloadingQueue";
	}

	@Override
	public boolean isEmpty() {
		return count.get() == 0;
	}

	@Override
	public QueueType getQueueType() {
		return QueueType.MPMC;
	}

}

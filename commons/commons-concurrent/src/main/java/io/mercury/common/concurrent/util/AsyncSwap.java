package io.mercury.common.concurrent.util;

import java.util.concurrent.SynchronousQueue;
import java.util.function.Consumer;

public final class AsyncSwap<E> implements Runnable {

	private final SynchronousQueue<E> swap = new SynchronousQueue<>();

	private final Consumer<E> consumer;
	
	private AsyncSwap(Consumer<E> consumer) {
		this.consumer = consumer;
	}
	
	public boolean put(E e) {
		return swap.offer(e);
	}

	@Override
	public void run() {
		try {
			E e = swap.take();
			consumer.accept(e);
		} catch (InterruptedException e) {
			
		}

	}

}

package io.mercury.common.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadFactoryImpl implements ThreadFactory {

	private final String name;

	private final AtomicInteger incr = new AtomicInteger();

	public ThreadFactoryImpl(String name) {
		this.name = name;
	}

	@Override
	public Thread newThread(Runnable runnable) {
		Thread thread = new Thread(runnable, name + "-" + incr.getAndIncrement());
		return thread;
	}

}

package io.mercury.common.thread;

import static io.mercury.common.thread.ThreadSupport.newMaxPriorityThread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class MaxPriorityThreadFactory implements ThreadFactory {

	private final String name;

	private final AtomicInteger incr = new AtomicInteger();

	public MaxPriorityThreadFactory(String name) {
		this.name = name;
	}

	@Override
	public Thread newThread(Runnable runnable) {
		Thread thread = newMaxPriorityThread(name + "-" + incr.getAndIncrement(), runnable);
		return thread;
	}

}

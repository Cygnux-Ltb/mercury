package io.mercury.common.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static io.mercury.common.thread.ThreadSupport.newMaxPriorityThread;

public class MaxPriorityThreadFactory implements ThreadFactory {

	private final String name;

	private final AtomicInteger incr = new AtomicInteger();

	public MaxPriorityThreadFactory(String name) {
		this.name = name;
	}

	@Override
	public Thread newThread(Runnable runnable) {
		return newMaxPriorityThread(name + "-" + incr.getAndIncrement(), runnable);
	}

}

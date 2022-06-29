package io.mercury.common.thread;

import javax.annotation.Nonnull;

public class MaxPriorityDaemonThreadFactory extends MaxPriorityThreadFactory {

	public MaxPriorityDaemonThreadFactory(String name) {
		super(name);
	}

	@Override
	public Thread newThread(@Nonnull Runnable runnable) {
		Thread thread = super.newThread(runnable);
		thread.setDaemon(true);
		return thread;
	}

}

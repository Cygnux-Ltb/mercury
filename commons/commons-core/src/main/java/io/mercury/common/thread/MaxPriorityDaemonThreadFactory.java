package io.mercury.common.thread;

public class MaxPriorityDaemonThreadFactory extends MaxPriorityThreadFactory {

	public MaxPriorityDaemonThreadFactory(String name) {
		super(name);
	}

	@Override
	public Thread newThread(Runnable runnable) {
		Thread thread = super.newThread(runnable);
		thread.setDaemon(true);
		return thread;
	}

}

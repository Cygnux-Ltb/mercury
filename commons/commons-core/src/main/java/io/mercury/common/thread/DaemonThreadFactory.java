package io.mercury.common.thread;

public class DaemonThreadFactory extends CommonThreadFactory {

	public DaemonThreadFactory(String name) {
		super(name);
	}

	@Override
	public Thread newThread(Runnable runnable) {
		Thread thread = super.newThread(runnable);
		thread.setDaemon(true);
		return thread;
	}

}

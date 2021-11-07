package io.mercury.common.thread;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

import org.eclipse.collections.api.list.MutableList;

import io.mercury.common.collections.MutableLists;
import io.mercury.common.number.ThreadSafeRandoms;

public final class ShutdownHooks {

	private final MutableList<Runnable> shutdownTasks = MutableLists.newFastList(64);

	private static final ShutdownHooks INSTANCE = new ShutdownHooks();

	private ShutdownHooks() {
		Runtime.getRuntime().addShutdownHook(new Thread(this::executeShutdownHook, "ShutdownHooksQueueHandleThread"));
	}

	private void executeShutdownHook() {
		System.out.println("start execution all shutdown hook");
		ThreadPoolExecutor executor = CommonThreadPool.newBuilder().build();
		for (Runnable shutdownTask : shutdownTasks)
			executor.execute(shutdownTask);
		executor.shutdown();
		while (!executor.isTerminated())
			SleepSupport.sleepIgnoreInterrupts(100);
		System.out.println("all shutdown hook execution completed");
	}

	/**
	 * 
	 * @param task
	 */
	public static synchronized void addSubTask(Runnable task) {
		INSTANCE.shutdownTasks.add(task);
	}

	/**
	 * 
	 * @param hook
	 * @return
	 */
	public static Thread addShutdownHook(Runnable hook) {
		return addShutdownHook("ShutdownHooksSubThread-" + ThreadSafeRandoms.randomUnsignedInt(), hook);
	}

	public static Thread addShutdownHook(String threadName, Runnable hook) {
		var thread = Threads.newThread(threadName, hook);
		Runtime.getRuntime().addShutdownHook(thread);
		return thread;
	}

	/**
	 * 
	 * @param closeable
	 */
	public static void closeResourcesWhenShutdown(Closeable closeable) {
		addSubTask(() -> {
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public static void main(String[] args) {

		ShutdownHooks.addSubTask(new Thread(() -> System.out.println("关闭钩子1")));
		ShutdownHooks.addSubTask(new Thread(() -> System.out.println("关闭钩子2")));
		ShutdownHooks.addSubTask(new Thread(() -> System.out.println("关闭钩子3")));
		ShutdownHooks.addSubTask(new Thread(() -> System.out.println("关闭钩子4")));
		ShutdownHooks.addSubTask(new Thread(() -> System.out.println("关闭钩子5")));
		ShutdownHooks.addSubTask(new Thread(() -> System.out.println("关闭钩子6")));
		ShutdownHooks.addSubTask(new Thread(() -> System.out.println("关闭钩子7")));
		ShutdownHooks.addSubTask(new Thread(() -> System.out.println("关闭钩子8")));

	}

}

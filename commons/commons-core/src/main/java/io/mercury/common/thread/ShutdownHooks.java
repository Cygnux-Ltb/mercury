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
			Threads.sleepIgnoreInterrupts(100);
		System.out.println("all shutdown hook execution completed");
	}

	/**
	 * 
	 * @param task
	 */
	public static synchronized void addShutdownHookSubTask(Runnable task) {
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
		Thread hookThread = Threads.newThread(threadName, hook);
		Runtime.getRuntime().addShutdownHook(hookThread);
		return hookThread;
	}

	/**
	 * 
	 * @param closeable
	 */
	public static void closeResourcesWhenShutdown(Closeable closeable) {
		addShutdownHookSubTask(() -> {
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public static void main(String[] args) {

		ShutdownHooks.addShutdownHookSubTask(new Thread(() -> System.out.println("关闭钩子1")));
		ShutdownHooks.addShutdownHookSubTask(new Thread(() -> System.out.println("关闭钩子2")));
		ShutdownHooks.addShutdownHookSubTask(new Thread(() -> System.out.println("关闭钩子3")));
		ShutdownHooks.addShutdownHookSubTask(new Thread(() -> System.out.println("关闭钩子4")));
		ShutdownHooks.addShutdownHookSubTask(new Thread(() -> System.out.println("关闭钩子5")));
		ShutdownHooks.addShutdownHookSubTask(new Thread(() -> System.out.println("关闭钩子6")));
		ShutdownHooks.addShutdownHookSubTask(new Thread(() -> System.out.println("关闭钩子7")));
		ShutdownHooks.addShutdownHookSubTask(new Thread(() -> System.out.println("关闭钩子8")));

	}

}

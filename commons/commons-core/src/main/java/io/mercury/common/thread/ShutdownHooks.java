package io.mercury.common.thread;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

import org.eclipse.collections.api.list.MutableList;

import io.mercury.common.collections.MutableLists;
import io.mercury.common.number.RandomNumber;

public final class ShutdownHooks {

	private MutableList<Runnable> runnables = MutableLists.newFastList(64);

	private static final ShutdownHooks INSTANCE = new ShutdownHooks();

	private ShutdownHooks() {
		Runtime.getRuntime().addShutdownHook(new Thread(this::executeShutdownHook, "ShutdownHooksQueueHandleThread"));
	}

	private void executeShutdownHook() {
		System.out.println("start execution all shutdown hook");
		ThreadPoolExecutor executor = CommonThreadPool.newBuilder().build();
		for (Runnable runnable : runnables)
			executor.execute(runnable);
		executor.shutdown();
		while (!executor.isTerminated())
			Threads.sleepIgnoreInterrupts(100);
		System.out.println("all shutdown hook execution completed");
	}

	public static synchronized void addShutdownHookTask(Runnable runnable) {
		INSTANCE.runnables.add(runnable);
	}

	public static Thread addShutdownHookThread(Runnable runnable) {
		return addShutdownHookThread("ShutdownHooksSubThread-" + RandomNumber.randomUnsignedInt(), runnable);
	}

	public static Thread addShutdownHookThread(String threadName, Runnable runnable) {
		Thread thread = new Thread(runnable, threadName);
		Runtime.getRuntime().addShutdownHook(thread);
		return thread;
	}

	public static void closeResourcesWhenShutdown(Closeable closeable) {
		addShutdownHookTask(() -> {
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public static void main(String[] args) {

		ShutdownHooks.addShutdownHookTask(new Thread(() -> System.out.println("关闭钩子1")));
		ShutdownHooks.addShutdownHookTask(new Thread(() -> System.out.println("关闭钩子2")));
		ShutdownHooks.addShutdownHookTask(new Thread(() -> System.out.println("关闭钩子3")));
		ShutdownHooks.addShutdownHookTask(new Thread(() -> System.out.println("关闭钩子4")));
		ShutdownHooks.addShutdownHookTask(new Thread(() -> System.out.println("关闭钩子5")));
		ShutdownHooks.addShutdownHookTask(new Thread(() -> System.out.println("关闭钩子6")));
		ShutdownHooks.addShutdownHookTask(new Thread(() -> System.out.println("关闭钩子7")));
		ShutdownHooks.addShutdownHookTask(new Thread(() -> System.out.println("关闭钩子8")));

	}

}

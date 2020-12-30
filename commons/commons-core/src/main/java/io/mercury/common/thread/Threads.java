package io.mercury.common.thread;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import io.mercury.common.log.CommonLoggerFactory;

public final class Threads {

	private Threads() {
	}

	private static final Logger log = CommonLoggerFactory.getLogger(Threads.class);

	/**
	 * 
	 * @return
	 */
	public static final ThreadPoolExecutor newCommonThreadPool() {
		return CommonThreadPool.newBuilder().build();
	}

	/**
	 * 
	 * @param runnable
	 * @return
	 */
	public static final Thread newThread(Runnable runnable) {
		return new Thread(runnable);
	}

	/**
	 * 
	 * @param runnable
	 * @param threadName
	 * @return
	 */
	public static final Thread newThread(String threadName, Runnable runnable) {
		return new Thread(runnable, threadName);
	}

	/**
	 * 
	 * @param runnable
	 * @return
	 */
	public static final Thread newMaxPriorityThread(Runnable runnable) {
		return setThreadPriority(new Thread(runnable), Thread.MAX_PRIORITY);
	}

	/**
	 * 
	 * @param runnable
	 * @param threadName
	 * @return
	 */
	public static final Thread newMaxPriorityThread(String threadName, Runnable runnable) {
		return setThreadPriority(new Thread(runnable, threadName), Thread.MAX_PRIORITY);
	}

	/**
	 * 
	 * @param runnable
	 * @return
	 */
	public static final Thread newMinPriorityThread(Runnable runnable) {
		return setThreadPriority(new Thread(runnable), Thread.MIN_PRIORITY);
	}

	/**
	 * 
	 * @param runnable
	 * @param threadName
	 * @return
	 */
	public static final Thread newMinPriorityThread(String threadName, Runnable runnable) {
		return setThreadPriority(new Thread(runnable, threadName), Thread.MIN_PRIORITY);
	}

	/**
	 * 
	 * @param thread
	 * @param priority
	 * @return
	 */
	private static Thread setThreadPriority(Thread thread, int priority) {
		thread.setPriority(priority);
		return thread;
	}

	/**
	 * 
	 * @param runnable
	 * @return
	 */
	public static final Thread startNewThread(Runnable runnable) {
		return startThread(newThread(runnable));
	}

	/**
	 * 
	 * @param runnable
	 * @param threadName
	 * @return
	 */
	public static final Thread startNewThread(String threadName, Runnable runnable) {
		return startThread(new Thread(runnable, threadName));
	}

	/**
	 * 
	 * @param runnable
	 * @return
	 */
	public static final Thread startNewMaxPriorityThread(Runnable runnable) {
		return startThread(newMaxPriorityThread(runnable));
	}

	/**
	 * 
	 * @param runnable
	 * @param threadName
	 * @return
	 */
	public static final Thread startNewMaxPriorityThread(String threadName, Runnable runnable) {
		return startThread(newMaxPriorityThread(threadName, runnable));
	}

	/**
	 * 
	 * @param runnable
	 * @return
	 */
	public static final Thread startNewMinPriorityThread(Runnable runnable) {
		return startThread(newMinPriorityThread(runnable));
	}

	/**
	 * 
	 * @param runnable
	 * @param threadName
	 * @return
	 */
	public static final Thread startNewMinPriorityThread(String threadName, Runnable runnable) {
		return startThread(newMinPriorityThread(threadName, runnable));
	}

	/**
	 * 
	 * @param thread
	 * @return
	 */
	private static final Thread startThread(Thread thread) {
		thread.start();
		return thread;
	}

	/**
	 * 
	 * @param millis
	 */
	public static final void sleepIgnoreInterrupts(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			log.error("Threads::sleepIgnoreInterrupts(millis==[{}]) throw InterruptedException -> {}", millis,
					e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param millis
	 * @param nanos
	 */
	public static final void sleepIgnoreInterrupts(long millis, int nanos) {
		try {
			Thread.sleep(millis, nanos);
		} catch (InterruptedException e) {
			log.error("Threads::sleepIgnoreInterrupts(millis==[{}]) throw InterruptedException -> {}", millis,
					e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param timeUnit
	 * @param time
	 */
	public static final void sleepIgnoreInterrupts(@Nonnull TimeUnit timeUnit, long time) {
		try {
			timeUnit.sleep(time);
		} catch (InterruptedException e) {
			log.error("Threads::sleep(time==[{}], timeUnit==[{}]) throw InterruptedException -> {}", time, timeUnit,
					e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param millis
	 * @throws RuntimeInterruptedException
	 */
	public static final void sleep(long millis) throws RuntimeInterruptedException {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			log.error("Threads::sleep(millis==[{}]) throw InterruptedException -> {}", millis, e.getMessage(), e);
			throw new RuntimeInterruptedException(e);
		}
	}

	/**
	 * 
	 * @param millis
	 * @param nanos
	 * @throws RuntimeInterruptedException
	 */
	public static final void sleep(long millis, int nanos) throws RuntimeInterruptedException {
		try {
			Thread.sleep(millis, nanos);
		} catch (InterruptedException e) {
			log.error("Threads::sleep(millis==[{}], nanos==[{}]) throw InterruptedException -> {}", millis, nanos,
					e.getMessage(), e);
			throw new RuntimeInterruptedException(e);
		}
	}

	/**
	 * 
	 * @param timeUnit
	 * @param time
	 * @throws RuntimeInterruptedException
	 */
	public static final void sleep(@Nonnull TimeUnit timeUnit, long time) throws RuntimeInterruptedException {
		try {
			timeUnit.sleep(time);
		} catch (InterruptedException e) {
			log.error("Threads::sleep(time==[{}], timeUnit==[{}]) throw InterruptedException -> {}", time, timeUnit,
					e.getMessage(), e);
			throw new RuntimeInterruptedException(e);
		}
	}

	/**
	 * 
	 */
	public static final void join() {
		join(Thread.currentThread());
	}

	/**
	 * 
	 * @param thread
	 */
	public static final void join(Thread thread) {
		try {
			thread.join();
		} catch (InterruptedException e) {
			log.error("Thread join throw InterruptedException from thread -> id==[{}], name==[{}]", thread.getId(),
					thread.getName(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @return
	 */
	public static final String currentThreadName() {
		return Thread.currentThread().getName();
	}

	/**
	 * Gets the "root" thread group for the entire JVM. Internally, first get the
	 * current thread and its thread group. Then get its parent group, then its
	 * parent group, and on up until you find a group with a null parent. That's the
	 * root ThreadGroup. Since the same root thread group is used for the life of
	 * the JVM, you can safely cache it for faster future use.
	 * 
	 * @return The root thread group for the JVM
	 */
	static public ThreadGroup getRootThreadGroup() {
		ThreadGroup tg = Thread.currentThread().getThreadGroup();
		ThreadGroup ptg;
		while ((ptg = tg.getParent()) != null)
			tg = ptg;
		return tg;
	}

	/**
	 * Gets all threads in the JVM. This is really a snapshot of all threads at the
	 * time this method is called.
	 * 
	 * @return An array of all threads currently running in the JVM.
	 */
	static public Thread[] getAllThreads() {
		final ThreadGroup root = getRootThreadGroup();
		final ThreadMXBean thbean = ManagementFactory.getThreadMXBean();
		int nAlloc = thbean.getThreadCount();
		int n = 0;
		Thread[] threads;
		do {
			nAlloc *= 2;
			threads = new Thread[nAlloc];
			n = root.enumerate(threads, true);
		} while (n == nAlloc);
		return Arrays.copyOf(threads, n);
	}

	/**
	 * Gets a thread by its assigned ID. Internally, this method calls
	 * getAllThreads() so be careful to only call this method once and cache your
	 * result.
	 * 
	 * @param id The thread ID
	 * @return The thread with this ID or null if none were found
	 */
	public static Thread getThread(final long id) {
		final Thread[] threads = getAllThreads();
		for (Thread thread : threads)
			if (thread.getId() == id)
				return thread;
		return null;
	}

	/**
	 * Gets all threads if its name matches a regular expression. For example, using
	 * a regex of "main" will execute a case-sensitive match for threads with the
	 * exact name of "main". A regex of ".*main.*" will execute a case sensitive
	 * match for threads with "main" anywhere in their name. A regex of
	 * "(?i).*main.*" will execute a case insensitive match of any thread that has
	 * "main" in its name.
	 * 
	 * @param regex The regular expression to use when matching a threads name. Same
	 *              rules apply as String.matches() method.
	 * @return An array (will not be null) of all matching threads. An empty array
	 *         will be returned if no threads match.
	 */
	public static Thread[] getAllThreadsMatching(final String regex) {
		if (regex == null)
			throw new NullPointerException("Null thread name regex");
		final Thread[] threads = getAllThreads();
		ArrayList<Thread> matchingThreads = new ArrayList<Thread>();
		for (Thread thread : threads) {
			if (thread.getName().matches(regex)) {
				matchingThreads.add(thread);
			}
		}
		return matchingThreads.toArray(new Thread[0]);
	}

	public static void main(String[] args) {
		System.out.println(currentThreadName());
		startNewThread("Test0", () -> System.out.println(currentThreadName()));
		sleep(2000);
	}

}

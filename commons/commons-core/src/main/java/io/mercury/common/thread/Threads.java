package io.mercury.common.thread;

import static java.lang.Thread.MAX_PRIORITY;
import static java.lang.Thread.MIN_PRIORITY;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadPoolExecutor;

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
	 * @param r
	 * @return
	 */
	public static final Thread newThread(Runnable r) {
		return new Thread(r);
	}

	/**
	 * 
	 * @param r
	 * @param name
	 * @return
	 */
	public static final Thread newThread(String name, Runnable r) {
		return new Thread(r, name);
	}

	/**
	 * 
	 * @param r
	 * @return
	 */
	public static final Thread newMaxPriorityThread(Runnable r) {
		return setThreadPriority(new Thread(r), MAX_PRIORITY);
	}

	/**
	 * 
	 * @param r
	 * @param name
	 * @return
	 */
	public static final Thread newMaxPriorityThread(String name, Runnable r) {
		return setThreadPriority(new Thread(r, name), MAX_PRIORITY);
	}

	/**
	 * 
	 * @param r
	 * @return
	 */
	public static final Thread newMinPriorityThread(Runnable r) {
		return setThreadPriority(new Thread(r), MIN_PRIORITY);
	}

	/**
	 * 
	 * @param runnable
	 * @param threadName
	 * @return
	 */
	public static final Thread newMinPriorityThread(String threadName, Runnable runnable) {
		return setThreadPriority(new Thread(runnable, threadName), MIN_PRIORITY);
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
	 * @param r
	 * @return
	 */
	public static final Thread startNewThread(Runnable r) {
		return startThread(newThread(r));
	}

	/**
	 * 
	 * @param r
	 * @param name
	 * @return
	 */
	public static final Thread startNewThread(String name, Runnable r) {
		return startThread(new Thread(r, name));
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
	public static Thread[] getAllThreads() {
		final ThreadGroup root = getRootThreadGroup();
		final ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();
		int count = mxBean.getThreadCount();
		int n = 0;
		Thread[] threads;
		do {
			count *= 2;
			threads = new Thread[count];
			n = root.enumerate(threads, true);
		} while (n == count);
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
		SleepSupport.sleep(2000);
	}

}

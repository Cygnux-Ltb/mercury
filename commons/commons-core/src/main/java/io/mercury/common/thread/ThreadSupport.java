package io.mercury.common.thread;

import static java.lang.Thread.MAX_PRIORITY;
import static java.lang.Thread.MIN_PRIORITY;
import static java.lang.Thread.NORM_PRIORITY;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.map.MutableMap;
import org.slf4j.Logger;

import io.mercury.common.collections.MutableMaps;
import io.mercury.common.log.Log4j2LoggerFactory;
import io.mercury.common.util.StringSupport;

public final class ThreadSupport {

	private static final Logger log = Log4j2LoggerFactory.getLogger(ThreadSupport.class);

	private ThreadSupport() {
	}

	public enum ThreadPriority {
		/**
		 * The minimum priority that a thread can have.
		 */
		MIN(MIN_PRIORITY),

		/**
		 * BELOW NORM_PRIORITY
		 */
		BELOW_NORM(NORM_PRIORITY - 2),

		/**
		 * The default priority that is assigned to a thread.
		 */
		NORM(NORM_PRIORITY),

		/**
		 * ABOVE NORM_PRIORITY
		 */
		ABOVE_NORM(NORM_PRIORITY + 2),
		/**
		 * The maximum priority that a thread can have.
		 */
		MAX(MAX_PRIORITY),

		;

		private final int value;

		ThreadPriority(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	/**
	 * 
	 * @return
	 */
	public static ThreadPoolExecutor newCommonThreadPool() {
		return CommonThreadPool.newBuilder().build();
	}

	/**
	 * 
	 * @param threadName
	 * @return
	 */
	public static ThreadFactory newThreadFactory(String threadName) {
		return newThreadFactory(threadName, ThreadPriority.NORM);
	}

	/**
	 * 
	 * @param threadName
	 * @param priority
	 * @return
	 */
	public static ThreadFactory newThreadFactory(String threadName, ThreadPriority priority) {
		return runnable -> newThread(threadName, runnable, priority);
	}

	/**
	 * 
	 * @param threadName
	 * @return
	 */
	public static ThreadFactory newDaemonThreadFactory(String threadName) {
		return newDaemonThreadFactory(threadName, ThreadPriority.NORM);
	}

	/**
	 * 
	 * @param threadName
	 * @param priority
	 * @return
	 */
	public static ThreadFactory newDaemonThreadFactory(String threadName, ThreadPriority priority) {
		return runnable -> setDaemonThread(newThread(threadName, runnable, priority));
	}

	/**
	 * 
	 * @param thread
	 * @param priority
	 * @return
	 */
	public static Thread setThreadPriority(Thread thread, ThreadPriority priority) {
		thread.setPriority(priority != null ? priority.getValue() : ThreadPriority.NORM.getValue());
		return thread;
	}

	/**
	 * 
	 * @param thread
	 * @return
	 */
	public static Thread setDaemonThread(Thread thread) {
		thread.setDaemon(true);
		return thread;
	}

	/**
	 * 
	 * @param thread
	 * @param handler
	 * @return
	 */
	public static Thread setThreadExceptionHandler(Thread thread, UncaughtExceptionHandler handler) {
		thread.setUncaughtExceptionHandler(handler);
		return thread;
	}

	/**
	 * 
	 * @param runnable
	 * @return
	 */
	public static Thread newThread(Runnable runnable) {
		return new Thread(runnable);
	}

	/**
	 * 
	 * @param runnable
	 * @param name
	 * @return
	 */
	public static Thread newThread(String name, Runnable runnable) {
		return new Thread(runnable, name);
	}

	/**
	 * 
	 * @param runnable
	 * @param priority
	 * @return
	 */
	public static Thread newThread(Runnable runnable, ThreadPriority priority) {
		return setThreadPriority(new Thread(runnable), priority);
	}

	/**
	 * 
	 * @param name
	 * @param runnable
	 * @param priority
	 * @return
	 */
	public static Thread newThread(String name, Runnable runnable, ThreadPriority priority) {
		return setThreadPriority(new Thread(runnable, name), priority);
	}

	/**
	 * 
	 * @param runnable
	 * @return
	 */
	public static Thread newMaxPriorityThread(Runnable runnable) {
		return setThreadPriority(new Thread(runnable), ThreadPriority.MAX);
	}

	/**
	 * 
	 * @param runnable
	 * @param name
	 * @return
	 */
	public static Thread newMaxPriorityThread(String name, Runnable runnable) {
		return setThreadPriority(new Thread(runnable, name), ThreadPriority.MAX);
	}

	/**
	 * 
	 * @param runnable
	 * @return
	 */
	public static Thread newMinPriorityThread(Runnable runnable) {
		return setThreadPriority(new Thread(runnable), ThreadPriority.MIN);
	}

	/**
	 * 
	 * @param runnable
	 * @param name
	 * @return
	 */
	public static Thread newMinPriorityThread(String name, Runnable runnable) {
		return setThreadPriority(new Thread(runnable, name), ThreadPriority.MIN);
	}

	/**
	 * 
	 * @param runnable
	 * @return
	 */
	public static Thread startNewThread(Runnable runnable) {
		return startThread(newThread(runnable));
	}

	/**
	 * 
	 * @param runnable
	 * @param name
	 * @return
	 */
	public static Thread startNewThread(String name, Runnable runnable) {
		return startThread(new Thread(runnable, name));
	}

	/**
	 * 
	 * @param runnable
	 * @return
	 */
	public static Thread startNewMaxPriorityThread(Runnable runnable) {
		return startThread(newMaxPriorityThread(runnable));
	}

	/**
	 * 
	 * @param runnable
	 * @param name
	 * @return
	 */
	public static Thread startNewMaxPriorityThread(String name, Runnable runnable) {
		return startThread(newMaxPriorityThread(name, runnable));
	}

	/**
	 * 
	 * @param runnable
	 * @return
	 */
	public static Thread startNewMinPriorityThread(Runnable runnable) {
		return startThread(newMinPriorityThread(runnable));
	}

	/**
	 * 
	 * @param runnable
	 * @param name
	 * @return
	 */
	public static Thread startNewMinPriorityThread(String name, Runnable runnable) {
		return startThread(newMinPriorityThread(name, runnable));
	}

	/**
	 * 
	 * @param thread
	 * @return
	 */
	public static Thread startThread(Thread thread) {
		thread.start();
		return thread;
	}

	/**
	 * 
	 */
	public static void join() {
		join(Thread.currentThread());
	}

	/**
	 * 
	 * @param thread
	 */
	public static void join(Thread thread) {
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
	public static Thread getCurrentThread() {
		return Thread.currentThread();
	}

	/**
	 * 
	 * @return
	 */
	public static String getCurrentThreadName() {
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
		int n;
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
	public static Thread[] getThreadsWithMatched(final String regex) {
		if (regex == null)
			throw new NullPointerException("Null thread name regex");
		final Thread[] threads = getAllThreads();
		final List<Thread> matched = new ArrayList<>();
		for (Thread thread : threads) {
			if (thread.getName().matches(regex))
				matched.add(thread);
		}
		return matched.toArray(new Thread[0]);
	}

	/**
	 * 
	 * @return String
	 */
	public static String dumpThreadInfo() {
		return dumpThreadInfo(getCurrentThread());
	}

	/**
	 * 
	 * @param thread
	 * @return String
	 */
	public static String dumpThreadInfo(@Nonnull final Thread thread) {
		final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
		final MutableMap<String, String> map = MutableMaps.newUnifiedMap();
		final ThreadInfo threadInfo = threadMXBean.getThreadInfo(thread.getId());
		map.put("threadId", StringSupport.toString(threadInfo.getThreadId()));
		map.put("threadName", threadInfo.getThreadName());
		map.put("threadState", StringSupport.toString(threadInfo.getThreadState()));
		//map.put("priority", StringSupport.toString(threadInfo.getPriority()));
		map.put("lockName", threadInfo.getLockName());
		map.put("lockInfo", StringSupport.toString(threadInfo.getLockInfo()));
		map.put("lockOwnerId", StringSupport.toString(threadInfo.getLockOwnerId()));
		map.put("lockOwnerName", threadInfo.getLockOwnerName());
		map.put("waitedCount", StringSupport.toString(threadInfo.getWaitedCount()));
		map.put("waitedTime", StringSupport.toString(threadInfo.getWaitedTime()));
		map.put("blockedCount", StringSupport.toString(threadInfo.getBlockedCount()));
		map.put("blockedTime", StringSupport.toString(threadInfo.getBlockedTime()));
		return map.toString();
	}

	public static void main(String[] args) {
		System.out.println(getCurrentThreadName());
		startNewThread("Test0", () -> System.out.println(getCurrentThreadName()));
		SleepSupport.sleep(2000);
		startNewThread("Test1", () -> System.out.println(dumpThreadInfo()));
		SleepSupport.sleep(2000);
	}

}

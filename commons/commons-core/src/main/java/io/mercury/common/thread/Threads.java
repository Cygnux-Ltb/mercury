package io.mercury.common.thread;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
	public static final Thread newThread(Runnable runnable, String threadName) {
		return new Thread(runnable, threadName);
	}

	/**
	 * 
	 * @param runnable
	 * @return
	 */
	public static final Thread newMaxPriorityThread(Runnable runnable) {
		return setThreadPriority(newThread(runnable), Thread.MAX_PRIORITY);
	}

	/**
	 * 
	 * @param runnable
	 * @param threadName
	 * @return
	 */
	public static final Thread newMaxPriorityThread(Runnable runnable, String threadName) {
		return setThreadPriority(newThread(runnable, threadName), Thread.MAX_PRIORITY);
	}

	/**
	 * 
	 * @param runnable
	 * @return
	 */
	public static final Thread newMinPriorityThread(Runnable runnable) {
		return setThreadPriority(newThread(runnable), Thread.MIN_PRIORITY);
	}

	/**
	 * 
	 * @param runnable
	 * @param threadName
	 * @return
	 */
	public static final Thread newMinPriorityThread(Runnable runnable, String threadName) {
		return setThreadPriority(newThread(runnable, threadName), Thread.MIN_PRIORITY);
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
	public static final Thread startNewThread(Runnable runnable, String threadName) {
		return startThread(newThread(runnable, threadName));
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
	public static final Thread startNewMaxPriorityThread(Runnable runnable, String threadName) {
		return startThread(newMaxPriorityThread(runnable, threadName));
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
	public static final Thread startNewMinPriorityThread(Runnable runnable, String threadName) {
		return startThread(newMinPriorityThread(runnable, threadName));
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
			// ignore exception
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
			// ignore exception
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
			log.error("ThreadUtil#sleep(millis==[{}]) throw InterruptedException -> {}", millis, e.getMessage(), e);
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
			log.error("ThreadUtil#sleep(millis==[{}], nanos==[{}]) throw InterruptedException -> {}", millis, nanos,
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
	public static final void sleep(TimeUnit timeUnit, long time) throws RuntimeInterruptedException {
		try {
			timeUnit.sleep(time);
		} catch (InterruptedException e) {
			log.error("ThreadUtil#sleep(time==[{}], timeUnit==[{}]) throw InterruptedException -> {}", time, timeUnit,
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

	public static void main(String[] args) {

		System.out.println(currentThreadName());
		startNewThread(() -> System.out.println(currentThreadName()), "Test0");
		sleep(2000);
	}

}

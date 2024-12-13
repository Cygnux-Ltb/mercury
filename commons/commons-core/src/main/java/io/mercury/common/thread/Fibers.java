package io.mercury.common.thread;

import io.mercury.common.collections.MutableMaps;
import io.mercury.common.log4j2.Log4j2LoggerFactory;
import io.mercury.common.util.StringSupport;
import org.eclipse.collections.api.map.MutableMap;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.mercury.common.thread.ThreadPriority.MAX;
import static io.mercury.common.thread.ThreadPriority.MIN;
import static io.mercury.common.thread.ThreadPriority.getOrDefault;
import static java.lang.Thread.ofVirtual;

public final class Fiber {

    private static final Logger log = Log4j2LoggerFactory.getLogger(Fiber.class);

    private Fiber() {
    }

    /**
     * @param task Runnable
     * @return java.lang.Thread
     */
    public static Thread newFiber(Runnable task) {
        return ofVirtual()
                .unstarted(task);
    }

    /**
     * @param name String
     * @param task Runnable
     * @return java.lang.Thread
     */
    public static Thread newFiber(String name, Runnable task) {
        return ofVirtual()
                .name(name)
                .unstarted(task);
    }



    /**
     * @param task Runnable
     * @return java.lang.Thread
     */
    public static Thread newMaxPriorityThread(Runnable task) {
        return newFiber(MAX, task);
    }

    /**
     * @param name String
     * @param task Runnable
     * @return java.lang.Thread
     */
    public static Thread newMaxPriorityThread(String name, Runnable task) {
        return newFiber(name, MAX, task);
    }

    /**
     * @param task Runnable
     * @return java.lang.Thread
     */
    public static Thread newMinPriorityThread(Runnable task) {
        return newFiber(MIN, task);
    }

    /**
     * @param name String
     * @param task Runnable
     * @return java.lang.Thread
     */
    public static Thread newMinPriorityThread(String name, Runnable task) {
        return newFiber(name, MIN, task);
    }

    /// ################################################################################ ///

    /**
     * @param task Runnable
     * @return java.lang.Thread
     */
    public static Thread startNewThread(Runnable task) {
        return ofPlatform()
                .start(task);
    }

    /**
     * @param name String
     * @param task Runnable
     * @return java.lang.Thread
     */
    public static Thread startNewThread(String name, Runnable task) {
        return ofPlatform()
                .name(name)
                .start(task);
    }

    /**
     * @param priority ThreadPriority
     * @param task     Runnable
     * @return java.lang.Thread
     */
    public static Thread startNewThread(ThreadPriority priority, Runnable task) {
        return ofPlatform()
                .priority(getOrDefault(priority))
                .start(task);
    }

    /**
     * @param name     String
     * @param priority ThreadPriority
     * @param task     Runnable
     * @return java.lang.Thread
     */
    public static Thread startNewThread(String name, ThreadPriority priority, Runnable task) {
        return ofPlatform()
                .name(name)
                .priority(getOrDefault(priority))
                .start(task);
    }

    /**
     * @param task Runnable
     * @return java.lang.Thread
     */
    public static Thread startNewMaxPriorityThread(Runnable task) {
        return startNewThread(MAX, task);
    }

    /**
     * @param name String
     * @param task Runnable
     * @return java.lang.Thread
     */
    public static Thread startNewMaxPriorityThread(String name, Runnable task) {
        return startNewThread(name, MAX, task);
    }

    /**
     * @param task Runnable
     * @return java.lang.Thread
     */
    public static Thread startNewMinPriorityThread(Runnable task) {
        return startNewThread(MIN, task);
    }

    /**
     * @param name String
     * @param task Runnable
     * @return java.lang.Thread
     */
    public static Thread startNewMinPriorityThread(String name, Runnable task) {
        return startNewThread(name, MIN, task);
    }

    /// ################################################################################ ///

    /**
     * @throws RuntimeInterruptedException re
     */
    public static void join() throws RuntimeInterruptedException {
        join(Thread.currentThread());
    }

    /**
     * @param thread Thread
     * @throws RuntimeInterruptedException re
     */
    public static void join(Thread thread) throws RuntimeInterruptedException {
        try {
            thread.join();
        } catch (InterruptedException e) {
            log.error("Thread join throw InterruptedException from thread -> id==[{}], name==[{}]", thread.threadId(),
                    thread.getName(), e);
            throw new RuntimeInterruptedException(e.getMessage(), e);
        }
    }

    /**
     * @return java.lang.Thread
     */
    public static Thread getCurrentThread() {
        return Thread.currentThread();
    }

    /**
     * @return String
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
            if (thread.threadId() == id)
                return thread;
        return null;
    }

    /**
     * Gets all threads if its name matches a regular expression. For example, using
     * a regex of "main" will execute a case-sensitive match for threads with the
     * exact name of "main". A regex of ".*main.*" will execute a case-sensitive
     * match for threads with "main" anywhere in their name. A regex of
     * "(?i).*main.*" will execute a case-insensitive match of any thread that has
     * "main" in its name.
     *
     * @param regex The regular expression to use when matching a threads name. Same
     *              rules apply as String.matches() method.
     * @return An array (will not be null) of all matching threads. An empty array
     * will be returned if no threads match.
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
     * @return String
     */
    public static String dumpThreadInfo() {
        return dumpThreadInfo(getCurrentThread());
    }

    /**
     * @param thread Thread
     * @return String
     */
    public static String dumpThreadInfo(@Nonnull final Thread thread) {
        final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        final MutableMap<String, String> map = MutableMaps.newUnifiedMap();
        final ThreadInfo threadInfo = threadMXBean.getThreadInfo(thread.threadId());
        map.put("threadId", StringSupport.toString(threadInfo.getThreadId()));
        map.put("threadName", threadInfo.getThreadName());
        map.put("threadState", StringSupport.toString(threadInfo.getThreadState()));
        map.put("priority", StringSupport.toString(threadInfo.getPriority()));
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
        Sleep.millis(2000);
        startNewThread("Test1", () -> System.out.println(dumpThreadInfo()));
        Sleep.millis(2000);
    }

}

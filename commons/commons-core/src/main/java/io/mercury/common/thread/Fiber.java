package io.mercury.common.thread;

public class Fiber extends Thread {

    /**
     * Constructor
     *
     * @param task Runnable
     */
    public Fiber(Runnable task) {
        super(task);
    }

    /**
     * Constructor
     *
     * @param name String
     * @param task Runnable
     */
    public Fiber(String name, Runnable task) {
        super(task, name);
    }

    /**
     * Constructor
     *
     * @param group ThreadGroup
     * @param task  Runnable
     */
    public Fiber(ThreadGroup group, Runnable task) {
        super(group, task);
    }

    /**
     * Constructor
     *
     * @param name  String
     * @param group ThreadGroup
     * @param task  Runnable
     */
    public Fiber(String name, ThreadGroup group, Runnable task) {
        super(group, task, name);
    }

    /**
     * Schedules this thread to begin execution. The thread will execute
     * independently of the current thread.
     *
     * <p> A thread can be started at most once. In particular, a thread can not
     * be restarted after it has terminated.
     *
     * @throws IllegalThreadStateException if the thread was already started
     */
    @Override
    public void start() {
        Thread.ofVirtual().start(this);
    }

}

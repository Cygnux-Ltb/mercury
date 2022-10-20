package io.mercury.actors.impl;

import io.mercury.actors.IActorScheduler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Scheduler that processed all the actors messages sequentially in a single user-supplied thread. Run {@link #start()} from a thread that should become the message processor. The thread will block waiting for messages; once the scheduler is disposed
 * by calling {@link #close()}, the {@link #start()} method returns and thread continues.
 */
public class BlockingThreadScheduler implements IActorScheduler {

    private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
    private Thread thread;

    @Override
    public void schedule(Runnable task, Object actorId) {
        queue.add(task);
    }

    @Override
    public void close() {
        thread.interrupt();
    }

    /**
     * Starts message processing loop in the current thread. This method does not return until the scheduler is disposed by calling {@link #close()}. If {@link #schedule(Runnable, Object)} is called before start(), the message will be kept
     * in the queue.
     */
    public void start() {
        this.thread = Thread.currentThread();
        try {
            while (!thread.isInterrupted()) {
                Runnable job = queue.take();
                job.run();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}

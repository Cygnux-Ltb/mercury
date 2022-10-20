package io.mercury.actors.impl;

import io.mercury.actors.IActorScheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Scheduler that processed all the actors messages sequentially in a
 * single-thread executor service.
 */
public class SingleThreadScheduler implements IActorScheduler {

    private final ExecutorService executor;

    public SingleThreadScheduler() {
        this.executor = Executors.newSingleThreadExecutor(runnable -> new Thread(runnable, "actor:single"));
    }

    @Override
    public void schedule(Runnable task, Object actorId) {
        if (!executor.isShutdown() && !executor.isTerminated()) {
            executor.submit(task);
        }
    }

    @Override
    public void close() {
        executor.shutdown();
    }

}

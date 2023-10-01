package io.mercury.common.thread;

import static java.lang.Thread.MAX_PRIORITY;
import static java.lang.Thread.MIN_PRIORITY;
import static java.lang.Thread.NORM_PRIORITY;
import static java.lang.Thread.ofPlatform;

public enum ThreadPriority {

    /**
     * The minimum priority that a thread can have.
     */
    MIN(MIN_PRIORITY),

    /**
     * The default priority that is assigned to a thread.
     */
    NORM(NORM_PRIORITY),

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

    public Thread newThread(Runnable runnable) {
        return ofPlatform().priority(value).unstarted(runnable);
    }

    public Thread startThread(Runnable runnable) {
        return ofPlatform().priority(value).start(runnable);
    }

}

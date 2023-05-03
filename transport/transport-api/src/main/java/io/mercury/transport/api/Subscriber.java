package io.mercury.transport.api;

import io.mercury.common.annotation.thread.AsyncFunction;

public interface Subscriber extends Transport, Runnable {

    /**
     * Start subscribe
     */
    @AsyncFunction
    void subscribe();

    /**
     * Reconnect
     */
    void reconnect();

}

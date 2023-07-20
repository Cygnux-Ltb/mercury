package io.mercury.transport.api;

import io.mercury.transport.exception.PublishFailedException;

/**
 * @param <T> target type
 * @param <M> message type
 * @author yellow013
 */
public interface Publisher<T, M> extends Transport {

    /**
     * Publish to default location
     *
     * @param msg M
     * @throws PublishFailedException e
     */
    void publish(M msg) throws PublishFailedException;

    /**
     * Publish to target location
     *
     * @param target T
     * @param msg    M
     * @throws PublishFailedException e
     */
    void publish(T target, M msg) throws PublishFailedException;

}

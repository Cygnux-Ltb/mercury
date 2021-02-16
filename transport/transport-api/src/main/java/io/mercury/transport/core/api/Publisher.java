package io.mercury.transport.core.api;

import java.io.Closeable;

import io.mercury.transport.core.exception.PublishFailedException;

public interface Publisher<T> extends TransportModule, Closeable {

	/**
	 * Publish to default location
	 * 
	 * @param msg
	 * @throws PublishFailedException
	 */
	void publish(T msg) throws PublishFailedException;

	/**
	 * Publish to target location
	 * 
	 * @param target
	 * @param msg
	 * @throws PublishFailedException
	 */
	void publish(String target, T msg) throws PublishFailedException;

}

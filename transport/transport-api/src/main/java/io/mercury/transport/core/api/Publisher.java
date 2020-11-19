package io.mercury.transport.core.api;

import io.mercury.transport.core.exception.PublishFailedException;

public interface Publisher<T> extends TransportModule {

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

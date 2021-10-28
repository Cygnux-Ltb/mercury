package io.mercury.transport.api;

import io.mercury.transport.exception.PublishFailedException;

public interface Publisher<T> extends Transport {

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

package io.mercury.transport.api;

import java.io.Closeable;

import io.mercury.transport.exception.PublishFailedException;

public interface Publisher<T> extends Transport, Closeable, FanoutModule {

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

	/**
	 * 
	 * @return
	 */
	default String getPublisherName() {
		return getName();
	}

}

package io.mercury.transport.api;

import java.io.Closeable;

import io.mercury.common.annotation.thread.AsyncFunction;

public interface Subscriber extends Transport, Closeable {

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

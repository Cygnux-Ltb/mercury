package io.mercury.transport.core.api;

import java.io.Closeable;

public interface Subscriber extends Transport, Closeable {

	/**
	 * Start subscribe
	 */
	void subscribe();

	/**
	 * Reconnect
	 */
	void reconnect();

}

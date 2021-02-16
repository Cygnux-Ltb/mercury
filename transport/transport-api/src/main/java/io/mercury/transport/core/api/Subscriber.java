package io.mercury.transport.core.api;

import java.io.Closeable;

public interface Subscriber extends TransportModule, Closeable {

	/**
	 * Start subscribe
	 */
	void subscribe();

	/**
	 * Reconnect
	 */
	void reconnect();

}

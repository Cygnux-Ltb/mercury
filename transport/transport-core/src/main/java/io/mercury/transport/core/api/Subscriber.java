package io.mercury.transport.core.api;

public interface Subscriber extends TransportModule {

	/**
	 * Start subscribe
	 */
	void subscribe();

	/**
	 * Reconnect
	 */
	void reconnect();

}

package io.mercury.transport.core.api;

import java.io.Closeable;

import io.mercury.transport.core.exception.ConnectionBreakException;
import io.mercury.transport.core.exception.ReceiverStartException;

public interface Receiver extends Transport, Closeable, Runnable {

	/**
	 * Start receive
	 */
	void receive() throws ReceiverStartException;

	/**
	 * Reconnect
	 */
	void reconnect() throws ConnectionBreakException, ReceiverStartException;

	@Override
	default void run() {
		receive();
	}

}

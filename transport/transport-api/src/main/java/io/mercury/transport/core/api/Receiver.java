package io.mercury.transport.core.api;

import java.io.Closeable;

import io.mercury.transport.core.exception.ConnectionBreakException;
import io.mercury.transport.core.exception.ReceiverStartException;

public interface Receiver extends TransportModule, Closeable {

	/**
	 * Start receive
	 */
	void receive() throws ReceiverStartException;

	/**
	 * Reconnect
	 */
	void reconnect() throws ConnectionBreakException, ReceiverStartException;

}

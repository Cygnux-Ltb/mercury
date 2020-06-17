package io.mercury.transport.core.api;

import io.mercury.transport.core.TransportModule;
import io.mercury.transport.core.exception.ReceiverStartException;

public interface Receiver extends TransportModule {

	/**
	 * Start receive
	 */
	void receive() throws ReceiverStartException;

	/**
	 * 
	 */
	void reconnect() throws ReceiverStartException;

}

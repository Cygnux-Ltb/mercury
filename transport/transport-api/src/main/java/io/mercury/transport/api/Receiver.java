package io.mercury.transport.api;

import io.mercury.common.annotation.thread.AsyncFunction;
import io.mercury.transport.exception.ConnectionBreakException;
import io.mercury.transport.exception.ReceiverStartException;

public interface Receiver extends Transport, Runnable {

	/**
	 * Start receive
	 * 
	 * @throws ReceiverStartException
	 */
	@AsyncFunction
	void receive() throws ReceiverStartException;

	/**
	 * Reconnect
	 * 
	 * @throws ConnectionBreakException
	 * @throws ReceiverStartException
	 */
	void reconnect() throws ConnectionBreakException, ReceiverStartException;

	@Override
	default void run() {
		receive();
	}

}

package io.mercury.transport.api;

import java.io.Closeable;

import io.mercury.common.annotation.thread.AsyncFunction;
import io.mercury.transport.exception.ConnectionBreakException;
import io.mercury.transport.exception.ReceiverStartException;

public interface Receiver extends Transport, Closeable, Runnable, FaninModule {

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

	/**
	 * 
	 * @return
	 */
	default String getReceiverName() {
		return getName();
	}

	@Override
	default void run() {
		receive();
	}

}

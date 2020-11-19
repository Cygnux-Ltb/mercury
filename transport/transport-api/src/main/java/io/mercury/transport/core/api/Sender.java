package io.mercury.transport.core.api;

public interface Sender<T> extends TransportModule {

	/**
	 * 
	 * @param msg
	 */
	void send(T msg);

}

package io.mercury.transport.core.api;

public interface Requester<T> extends TransportModule {

	/**
	 * 
	 * @return <T> T
	 */
	T request();

}

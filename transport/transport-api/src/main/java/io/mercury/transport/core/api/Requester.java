package io.mercury.transport.core.api;

import io.mercury.transport.core.exception.RequestException;

public interface Requester<T> extends TransportModule {

	/**
	 * 
	 * @return <T> T
	 */
	T request() throws RequestException;

}

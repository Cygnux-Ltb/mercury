package io.mercury.transport.api;

import io.mercury.transport.exception.RequestException;

public interface Requester<T> extends Transport {

	/**
	 * 
	 * @return <T> T
	 */
	T request() throws RequestException;

	/**
	 * 
	 * @return
	 */
	default String getRequesterName() {
		return getName();
	}

}

package io.mercury.transport.api;

public interface TransportClient extends Transport {

	/**
	 * 
	 */
	void connect();

	default String getClientName() {
		return getName();
	}

}

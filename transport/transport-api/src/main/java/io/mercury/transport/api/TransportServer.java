package io.mercury.transport.api;

public interface TransportServer extends Transport {

	/**
	 * 
	 */
	void startup();

	default String getServerName() {
		return getName();
	}

}

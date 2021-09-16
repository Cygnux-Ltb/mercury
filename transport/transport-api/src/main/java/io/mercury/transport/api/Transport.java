package io.mercury.transport.api;

public interface Transport {

	/**
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 
	 * @return
	 */
	boolean isConnected();

	/**
	 * 
	 * @return
	 */
	boolean destroy();

}

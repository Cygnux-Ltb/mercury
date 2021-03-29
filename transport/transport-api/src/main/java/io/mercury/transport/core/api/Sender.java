package io.mercury.transport.core.api;

public interface Sender<T> extends Transport {

	/**
	 * 
	 * @param msg
	 */
	void sent(T msg);

}

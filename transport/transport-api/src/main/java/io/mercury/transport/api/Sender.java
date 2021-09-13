package io.mercury.transport.api;

public interface Sender<T> extends Transport {

	/**
	 * 
	 * @param msg
	 */
	void sent(T msg);

	/**
	 * 
	 * @return
	 */
	default String getSenderName() {
		return getName();
	}

}

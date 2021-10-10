package io.mercury.configuration.nacos;

public class NacosConnectionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7392466235739011687L;

	public NacosConnectionException(String message, Throwable e) {
		super(message, e);
	}

}

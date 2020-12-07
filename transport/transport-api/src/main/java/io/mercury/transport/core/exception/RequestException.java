package io.mercury.transport.core.exception;

public final class RequestException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1535413038727671000L;

	public RequestException(String message, Throwable throwable) {
		super(message, throwable);
	}

}

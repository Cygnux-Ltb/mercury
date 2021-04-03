package io.mercury.transport.exception;

public final class RequestException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1535413038727671000L;

	/**
	 * 
	 * @param message
	 */
	public RequestException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param throwable
	 */
	public RequestException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * 
	 * @param message
	 * @param throwable
	 */
	public RequestException(String message, Throwable throwable) {
		super(message, throwable);
	}

}

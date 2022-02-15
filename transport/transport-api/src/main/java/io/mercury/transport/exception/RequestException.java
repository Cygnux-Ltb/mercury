package io.mercury.transport.exception;

public final class RequestException extends RuntimeException {

	private static final long serialVersionUID = -1535413038727671000L;

	/**
	 * 
	 * @param msg
	 */
	public RequestException(String msg) {
		super(msg);
	}

	/**
	 * 
	 * @param cause
	 */
	public RequestException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 * @param msg
	 * @param cause
	 */
	public RequestException(String msg, Throwable cause) {
		super(msg, cause);
	}

}

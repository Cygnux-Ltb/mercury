package io.mercury.transport.exception;

public final class ConnectionBreakException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1822516879154638625L;

	/**
	 * 
	 * @param message
	 */
	public ConnectionBreakException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param cause
	 */
	public ConnectionBreakException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public ConnectionBreakException(String message, Throwable cause) {
		super(message, cause);
	}

}

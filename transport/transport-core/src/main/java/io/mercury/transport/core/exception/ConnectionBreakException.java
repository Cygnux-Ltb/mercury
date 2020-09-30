package io.mercury.transport.core.exception;

public final class ConnectionBreakException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1822516879154638625L;

	public ConnectionBreakException(String message) {
		super(message);
	}

	public ConnectionBreakException(Throwable cause) {
		super(cause);
	}

	public ConnectionBreakException(String message, Throwable cause) {
		super(message, cause);
	}

}

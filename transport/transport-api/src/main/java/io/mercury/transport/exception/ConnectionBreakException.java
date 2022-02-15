package io.mercury.transport.exception;

public final class ConnectionBreakException extends RuntimeException {

	private static final long serialVersionUID = 1822516879154638625L;

	/**
	 * 
	 * @param msg
	 */
	public ConnectionBreakException(String msg) {
		super(msg);
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
	 * @param msg
	 * @param cause
	 */
	public ConnectionBreakException(String msg, Throwable cause) {
		super(msg, cause);
	}

}

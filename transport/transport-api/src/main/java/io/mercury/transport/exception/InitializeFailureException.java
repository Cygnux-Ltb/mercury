package io.mercury.transport.exception;

public final class InitializeFailureException extends RuntimeException {

	private static final long serialVersionUID = -3673823194109364790L;

	/**
	 * 
	 * @param msg
	 */
	public InitializeFailureException(String msg) {
		super(msg);
	}

	/**
	 * 
	 * @param cause
	 */
	public InitializeFailureException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 * @param msg
	 * @param cause
	 */
	public InitializeFailureException(String msg, Throwable cause) {
		super(msg, cause);
	}

}

package io.mercury.transport.exception;

public final class InitializeFailureException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3673823194109364790L;

	/**
	 * 
	 * @param message
	 */
	public InitializeFailureException(String message) {
		super(message);
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
	 * @param message
	 * @param cause
	 */
	public InitializeFailureException(String message, Throwable cause) {
		super(message, cause);
	}

}

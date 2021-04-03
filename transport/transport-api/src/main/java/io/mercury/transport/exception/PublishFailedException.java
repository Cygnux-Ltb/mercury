package io.mercury.transport.exception;

public final class PublishFailedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4306166538549739230L;

	/**
	 * 
	 * @param message
	 */
	public PublishFailedException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param cause
	 */
	public PublishFailedException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public PublishFailedException(String message, Throwable cause) {
		super(message, cause);
	}

}

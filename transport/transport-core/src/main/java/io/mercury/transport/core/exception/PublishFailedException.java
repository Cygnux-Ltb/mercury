package io.mercury.transport.core.exception;

public class PublishFailedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4306166538549739230L;

	public PublishFailedException(String message) {
		super(message);
	}

	public PublishFailedException(Throwable cause) {
		super(cause);
	}

	public PublishFailedException(String message, Throwable cause) {
		super(message, cause);
	}

}

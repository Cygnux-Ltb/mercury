package io.mercury.transport.core.exception;

public class PublishFailedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4306166538549739230L;

	public PublishFailedException(String message) {
		super(message);
	}

	public PublishFailedException(Throwable throwable) {
		super(throwable);
	}

	public PublishFailedException(String message, Throwable throwable) {
		super(message, throwable);
	}

}

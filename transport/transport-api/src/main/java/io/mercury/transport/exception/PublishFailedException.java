package io.mercury.transport.exception;

public final class PublishFailedException extends RuntimeException {

	private static final long serialVersionUID = -4306166538549739230L;

	/**
	 * 
	 * @param msg
	 */
	public PublishFailedException(String msg) {
		super(msg);
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
	 * @param msg
	 * @param cause
	 */
	public PublishFailedException(String msg, Throwable cause) {
		super(msg, cause);
	}

}

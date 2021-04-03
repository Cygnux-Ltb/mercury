package io.mercury.transport.exception;

public final class ReceiverStartException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5792361596324344884L;

	/**
	 * 
	 * @param message
	 */
	public ReceiverStartException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param cause
	 */
	public ReceiverStartException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public ReceiverStartException(String message, Throwable cause) {
		super(message, cause);
	}

}

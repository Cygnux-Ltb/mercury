package io.mercury.transport.core.exception;

public final class ReceiverStartException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5792361596324344884L;

	public ReceiverStartException(String message) {
		super(message);
	}

	public ReceiverStartException(Throwable cause) {
		super(cause);
	}

	public ReceiverStartException(String message, Throwable cause) {
		super(message, cause);
	}

}

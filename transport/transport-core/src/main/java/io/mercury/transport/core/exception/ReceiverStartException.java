package io.mercury.transport.core.exception;

public class ReceiverStartException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5792361596324344884L;

	public ReceiverStartException(Throwable throwable) {
		super(throwable);
	}

	public ReceiverStartException(Throwable throwable, String message) {
		super(message, throwable);
	}

}

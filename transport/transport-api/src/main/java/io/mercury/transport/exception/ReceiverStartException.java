package io.mercury.transport.exception;

public final class ReceiverStartException extends RuntimeException {

	private static final long serialVersionUID = 5792361596324344884L;

	/**
	 * 
	 * @param msg
	 */
	public ReceiverStartException(String msg) {
		super(msg);
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
	 * @param msg
	 * @param cause
	 */
	public ReceiverStartException(String msg, Throwable cause) {
		super(msg, cause);
	}

}

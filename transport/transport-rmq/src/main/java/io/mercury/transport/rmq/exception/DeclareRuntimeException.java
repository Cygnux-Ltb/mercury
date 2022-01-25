package io.mercury.transport.rmq.exception;

public final class DeclareRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -3951956735581216086L;

	/**
	 * 
	 * @param cause
	 */
	public DeclareRuntimeException(DeclareException cause) {
		super(cause);
	}

}

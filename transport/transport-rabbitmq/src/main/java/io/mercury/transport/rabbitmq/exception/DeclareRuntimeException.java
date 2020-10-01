package io.mercury.transport.rabbitmq.exception;

public final class DeclareRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3951956735581216086L;

	/**
	 * 
	 * @param declareException
	 */
	public DeclareRuntimeException(DeclareException declareException) {
		super(declareException);
	}

}

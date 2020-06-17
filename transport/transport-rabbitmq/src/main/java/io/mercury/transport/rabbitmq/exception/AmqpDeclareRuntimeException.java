package io.mercury.transport.rabbitmq.exception;

public final class AmqpDeclareRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3951956735581216086L;

	public AmqpDeclareRuntimeException(AmqpDeclareException e) {
		super(e);
	}

}

package io.mercury.transport.rabbitmq.exception;

public class MsgHandleException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8006184062312891950L;

	/**
	 * 
	 * @param message
	 * @param throwable
	 */
	public MsgHandleException(String message, Throwable throwable) {
		super(message, throwable);
	}

}

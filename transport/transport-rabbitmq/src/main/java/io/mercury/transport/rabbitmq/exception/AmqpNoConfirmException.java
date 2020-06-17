package io.mercury.transport.rabbitmq.exception;

public class AmqpNoConfirmException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -197190157920481972L;

	public AmqpNoConfirmException(String exchange, String routingKey, int confirmRetry, long confirmTimeout, byte[] msg) {
		super("Call confirmPublish failure -> exchange==[" + exchange + "], routingKey==[" + routingKey
				+ "], confirmRetry==[" + confirmRetry + "], confirmTimeout==[" + confirmTimeout + "], msg==["
				+ new String(msg) + "]");
	}

	public AmqpNoConfirmException(String exchange, String routingKey, int confirmRetry, long confirmTimeout) {
		super("Call confirmPublish failure -> exchange==[" + exchange + "], routingKey==[" + routingKey
				+ "], confirmRetry==[" + confirmRetry + "], confirmTimeout==[" + confirmTimeout + "]");
	}

}

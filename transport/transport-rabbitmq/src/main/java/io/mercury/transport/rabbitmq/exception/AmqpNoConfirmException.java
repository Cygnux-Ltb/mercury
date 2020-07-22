package io.mercury.transport.rabbitmq.exception;

public class AmqpNoConfirmException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -197190157920481972L;

	/**
	 * 
	 * @param exchange
	 * @param routingKey
	 * @param confirmRetry
	 * @param confirmTimeout
	 * @param msg
	 */
	public AmqpNoConfirmException(String exchange, String routingKey, int confirmRetry, long confirmTimeout,
			byte[] msg) {
		super("Call confirmPublish failure -> exchange==[" + exchange + "], routingKey==[" + routingKey
				+ "], confirmRetry==[" + confirmRetry + "], confirmTimeout==[" + confirmTimeout + "], msg==["
				+ new String(msg) + "]");
	}

	/**
	 * 
	 * @param exchange
	 * @param routingKey
	 * @param confirmRetry
	 * @param confirmTimeout
	 */
	public AmqpNoConfirmException(String exchange, String routingKey, int confirmRetry, long confirmTimeout) {
		super("Call confirmPublish failure -> exchange==[" + exchange + "], routingKey==[" + routingKey
				+ "], confirmRetry==[" + confirmRetry + "], confirmTimeout==[" + confirmTimeout + "]");
	}

}

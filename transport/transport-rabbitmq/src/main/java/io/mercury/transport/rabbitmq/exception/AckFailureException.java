package io.mercury.transport.rabbitmq.exception;

public class AmqpAckFailureException extends Exception {

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
	 */
	public AmqpAckFailureException(String exchange, String routingKey, int confirmRetry, long confirmTimeout) {
		super("Call confirmPublish failure -> exchange==[" + exchange + "], routingKey==[" + routingKey
				+ "], confirmRetry==[" + confirmRetry + "], confirmTimeout==[" + confirmTimeout + "]");
	}

	/**
	 * 
	 * @param exchange
	 * @param routingKey
	 * @param confirmRetry
	 * @param confirmTimeout
	 * @param throwable
	 */
	public AmqpAckFailureException(String exchange, String routingKey, int confirmRetry, long confirmTimeout,
			Throwable throwable) {
		super("Call confirmPublish failure -> exchange==[" + exchange + "], routingKey==[" + routingKey
				+ "], confirmRetry==[" + confirmRetry + "], confirmTimeout==[" + confirmTimeout + "]", throwable);
	}

	/**
	 * 
	 * @param exchange
	 * @param routingKey
	 * @param confirmRetry
	 * @param confirmTimeout
	 * @param msg
	 */
	public AmqpAckFailureException(String exchange, String routingKey, int confirmRetry, long confirmTimeout,
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
	 * @param msg
	 * @param throwable
	 */
	public AmqpAckFailureException(String exchange, String routingKey, int confirmRetry, long confirmTimeout, byte[] msg,
			Throwable throwable) {
		super("Call confirmPublish failure -> exchange==[" + exchange + "], routingKey==[" + routingKey
				+ "], confirmRetry==[" + confirmRetry + "], confirmTimeout==[" + confirmTimeout + "], msg==["
				+ new String(msg) + "]", throwable);
	}

}

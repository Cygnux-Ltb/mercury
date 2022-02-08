package io.mercury.transport.rmq.exception;

public class MsgConfirmFailureException extends Exception {

	private static final long serialVersionUID = -197190157920481972L;

	/**
	 * 
	 * @param exchange
	 * @param routingKey
	 * @param confirmRetry
	 * @param confirmTimeout
	 */
	public MsgConfirmFailureException(String exchange, String routingKey, int confirmRetry, long confirmTimeout) {
		super("Call confirmPublish failure -> exchange==[" + exchange + "], routingKey==[" + routingKey
				+ "], confirmRetry==[" + confirmRetry + "], confirmTimeout==[" + confirmTimeout + "]");
	}

	/**
	 * 
	 * @param exchange
	 * @param routingKey
	 * @param confirmRetry
	 * @param confirmTimeout
	 * @param cause
	 */
	public MsgConfirmFailureException(String exchange, String routingKey, int confirmRetry, long confirmTimeout,
			Throwable cause) {
		super("Call confirmPublish failure -> exchange==[" + exchange + "], routingKey==[" + routingKey
				+ "], confirmRetry==[" + confirmRetry + "], confirmTimeout==[" + confirmTimeout + "]", cause);
	}

	/**
	 * 
	 * @param exchange
	 * @param routingKey
	 * @param confirmRetry
	 * @param confirmTimeout
	 * @param msg
	 */
	public MsgConfirmFailureException(String exchange, String routingKey, int confirmRetry, long confirmTimeout,
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
	 * @param cause
	 */
	public MsgConfirmFailureException(String exchange, String routingKey, int confirmRetry, long confirmTimeout,
			byte[] msg, Throwable cause) {
		super("Call confirmPublish failure -> exchange==[" + exchange + "], routingKey==[" + routingKey
				+ "], confirmRetry==[" + confirmRetry + "], confirmTimeout==[" + confirmTimeout + "], msg==["
				+ new String(msg) + "]", cause);
	}

}

package io.mercury.transport.rmq.exception;

import java.util.Map;

import com.rabbitmq.client.BuiltinExchangeType;

public final class DeclareException extends Exception {

	private static final long serialVersionUID = -7352101640279556300L;

	/**
	 * 
	 * @param cause
	 */
	private DeclareException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	private DeclareException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * 
	 * @param queue
	 * @param durable
	 * @param exclusive
	 * @param autoDelete
	 * @param args
	 * @param cause
	 * @return
	 */
	public static DeclareException declareQueueError(String queue, boolean durable, boolean exclusive,
			boolean autoDelete, Map<String, Object> args, Throwable cause) {
		return new DeclareException("Declare queue error -> queue==[" + queue + "], durable==[" + durable
				+ "], exclusive==[" + exclusive + "], autoDelete==[" + autoDelete + "], args==[" + args + "]", cause);
	}

	/**
	 * 
	 * @param exchange
	 * @param type
	 * @param durable
	 * @param autoDelete
	 * @param internal
	 * @param args
	 * @param cause
	 * @return
	 */
	public static DeclareException declareExchangeError(String exchange, BuiltinExchangeType type, boolean durable,
			boolean autoDelete, boolean internal, Map<String, Object> args, Throwable cause) {
		return new DeclareException(
				"Declare exchange error -> exchange==[" + exchange + "], type==[" + type + "], durable==[" + durable
						+ "], autoDelete==[" + autoDelete + "], internal==[" + internal + "], args==[" + args + "]",
				cause);
	}

	/**
	 * 
	 * @param queue
	 * @param exchange
	 * @param routingKey
	 * @param cause
	 * @return
	 */
	public static DeclareException bindQueueError(String queue, String exchange, String routingKey,
			Throwable cause) {
		return new DeclareException("Bind queue error -> queue==[" + queue + "], exchange==[" + exchange
				+ "], routingKey==[" + routingKey + "]", cause);
	}

	/**
	 * 
	 * @param destExchange
	 * @param sourceExchange
	 * @param routingKey
	 * @param cause
	 * @return
	 */
	public static DeclareException bindExchangeError(String destExchange, String sourceExchange, String routingKey,
			Throwable cause) {
		return new DeclareException("Bind exchange error -> destExchange==[" + destExchange + "], sourceExchange==["
				+ sourceExchange + "], routingKey==[" + routingKey + "]", cause);
	}

	/**
	 * 
	 * @param cause
	 * @return
	 */
	public static DeclareException with(Throwable cause) {
		return new DeclareException(cause);
	}

}

package io.mercury.transport.rabbitmq.exception;

import java.util.Map;

import com.rabbitmq.client.BuiltinExchangeType;

public final class AmqpDeclareException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7352101640279556300L;

	/**
	 * 
	 * @param cause
	 */
	private AmqpDeclareException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	private AmqpDeclareException(String message, Throwable cause) {
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
	public static AmqpDeclareException declareQueueError(String queue, boolean durable, boolean exclusive,
			boolean autoDelete, Map<String, Object> args, Throwable cause) {
		return new AmqpDeclareException("Declare queue error -> queue==[" + queue + "], durable==[" + durable
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
	public static AmqpDeclareException declareExchangeError(String exchange, BuiltinExchangeType type, boolean durable,
			boolean autoDelete, boolean internal, Map<String, Object> args, Throwable cause) {
		return new AmqpDeclareException(
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
	public static AmqpDeclareException bindQueueError(String queue, String exchange, String routingKey,
			Throwable cause) {
		return new AmqpDeclareException("Bind queue error -> queue==[" + queue + "], exchange==[" + exchange
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
	public static AmqpDeclareException bindExchangeError(String destExchange, String sourceExchange, String routingKey,
			Throwable cause) {
		return new AmqpDeclareException("Bind exchange error -> destExchange==[" + destExchange + "], sourceExchange==["
				+ sourceExchange + "], routingKey==[" + routingKey + "]", cause);
	}

	/**
	 * 
	 * @param cause
	 * @return
	 */
	public static AmqpDeclareException with(Throwable cause) {
		return new AmqpDeclareException(cause);
	}

}

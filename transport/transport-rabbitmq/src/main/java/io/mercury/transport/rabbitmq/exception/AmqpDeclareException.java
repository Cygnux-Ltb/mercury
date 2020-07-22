package io.mercury.transport.rabbitmq.exception;

import java.util.Map;

import com.rabbitmq.client.BuiltinExchangeType;

public final class AmqpDeclareException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7352101640279556300L;

	private AmqpDeclareException(Throwable cause) {
		super(cause);
	}

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
		return new AmqpDeclareException(new StringBuilder(200).append("Declare queue error -> ").append("queue==[")
				.append(queue).append("], ").append("durable==[").append(durable).append("], ").append("exclusive==[")
				.append(exclusive).append("], ").append("autoDelete==[").append(autoDelete).append("], ")
				.append("args==[").append(args).append("]").toString(), cause);
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
				new StringBuilder(200).append("Declare exchange error -> ").append("exchange==[").append(exchange)
						.append("], ").append("type==[").append(type).append("], ").append("durable==[").append(durable)
						.append("], ").append("autoDelete==[").append(autoDelete).append("], ").append("internal==[")
						.append(internal).append("], ").append("args==[").append(args).append("]").toString(),
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
		return new AmqpDeclareException(new StringBuilder(150).append("Bind queue error -> ").append("queue==[")
				.append(queue).append("], ").append("exchange==[").append(exchange).append("], ")
				.append("routingKey==[").append(routingKey).append("]").toString(), cause);
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
		return new AmqpDeclareException(new StringBuilder(150).append("Bind exchange error -> ")
				.append("destExchange==[").append(destExchange).append("], ").append("sourceExchange==[")
				.append(sourceExchange).append("], ").append("routingKey==[").append(routingKey).append("]").toString(),
				cause);
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

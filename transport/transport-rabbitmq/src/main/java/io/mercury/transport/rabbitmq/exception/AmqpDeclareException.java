package io.mercury.transport.rabbitmq.exception;

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

	public static AmqpDeclareException declareQueueError(String queue, boolean durable, boolean exclusive,
			boolean autoDelete, Throwable cause) {
		return new AmqpDeclareException(new StringBuilder(150).append("Declare queue error -> queue==[").append(queue)
				.append("], durable==[").append(durable).append("], exclusive==[").append(exclusive)
				.append("], autoDelete==[").append(autoDelete).append("]").toString(), cause);
	}

	public static AmqpDeclareException declareExchangeError(String exchange, BuiltinExchangeType type, boolean durable,
			boolean autoDelete, boolean internal, Throwable cause) {
		return new AmqpDeclareException(new StringBuilder(150).append("Declare exchange error -> exchange==[")
				.append(exchange).append("], type==[").append(type).append("], durable==[").append(durable)
				.append("], autoDelete==[").append(autoDelete).append("], internal==[").append(internal).append("]")
				.toString(), cause);
	}

	public static AmqpDeclareException bindQueueError(String queue, String exchange, String routingKey,
			Throwable cause) {
		return new AmqpDeclareException(
				new StringBuilder(120).append("Bind queue error -> queue==[").append(queue).append("], exchange==[")
						.append(exchange).append("], routingKey==[").append(routingKey).append("]").toString(),
				cause);
	}

	public static AmqpDeclareException bindExchangeError(String destExchange, String sourceExchange, String routingKey,
			Throwable cause) {
		return new AmqpDeclareException(new StringBuilder(120).append("Bind exchange error -> destExchange==[")
				.append(destExchange).append("], sourceExchange==[").append(sourceExchange).append("], routingKey==[")
				.append(routingKey).append("]").toString(), cause);
	}

	public static AmqpDeclareException with(Throwable cause) {
		return new AmqpDeclareException(cause);
	}

}

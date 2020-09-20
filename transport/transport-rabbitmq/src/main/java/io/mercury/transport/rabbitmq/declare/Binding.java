package io.mercury.transport.rabbitmq.declare;

import static io.mercury.common.util.Assertor.nonNull;

public final class Binding {

	static enum DestType {
		Exchange, Queue
	}

	private AmqpExchange source;
	private AmqpExchange destExchange;
	private AmqpQueue destQueue;
	private String routingKey;
	private DestType destType;

	/**
	 * 
	 * @param source
	 * @param destExchange
	 */
	Binding(AmqpExchange source, AmqpExchange destExchange) {
		this(source, destExchange, "");
	}

	/**
	 * 
	 * @param source
	 * @param destExchange
	 * @param routingKey
	 */
	Binding(AmqpExchange source, AmqpExchange destExchange, String routingKey) {
		nonNull(source, "source");
		nonNull(destExchange, "destExchange");
		nonNull(routingKey, "routingKey");
		this.source = source;
		this.destExchange = destExchange;
		this.routingKey = routingKey;
		this.destType = DestType.Exchange;
	}

	/**
	 * 
	 * @param source
	 * @param destQueue
	 */
	Binding(AmqpExchange source, AmqpQueue destQueue) {
		this(source, destQueue, "");
	}

	/**
	 * 
	 * @param source
	 * @param destQueue
	 * @param routingKey
	 */
	Binding(AmqpExchange source, AmqpQueue destQueue, String routingKey) {
		nonNull(source, "source");
		nonNull(destQueue, "destQueue");
		nonNull(routingKey, "routingKey");
		this.source = source;
		this.destQueue = destQueue;
		this.routingKey = routingKey;
		this.destType = DestType.Queue;
	}

	/**
	 * @return the source
	 */
	AmqpExchange source() {
		return source;
	}

	/**
	 * @return the routingKey
	 */
	String routingKey() {
		return routingKey;
	}

	/**
	 * @return the destExchange
	 */
	AmqpExchange destExchange() {
		return destExchange;
	}

	/**
	 * @return the destQueue
	 */
	AmqpQueue destQueue() {
		return destQueue;
	}

	/**
	 * @return the destinationType
	 */
	DestType destType() {
		return destType;
	}

	public static void main(String[] args) {

		AmqpExchange exchange0 = AmqpExchange.direct("ABC");
		AmqpExchange exchange1 = AmqpExchange.direct("ABC");

		System.out.println(exchange0);
		System.out.println(exchange1);

		System.out.println(exchange0 == exchange1);
		System.out.println(exchange0.idempotent(exchange1));

		System.out.println(AmqpExchange.direct("ABC"));
		System.out.println(AmqpQueue.named("ABC"));

	}

}

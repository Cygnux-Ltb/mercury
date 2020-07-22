package io.mercury.transport.rabbitmq.declare;

import io.mercury.common.util.Assertor;

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
		this.source = Assertor.nonNull(source, "source");
		this.destExchange = Assertor.nonNull(destExchange, "destExchange");
		this.routingKey = Assertor.nonNull(routingKey, "routingKey");
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
		this.source = Assertor.nonNull(source, "source");
		this.destQueue = Assertor.nonNull(destQueue, "destQueue");
		this.routingKey = Assertor.nonNull(routingKey, "routingKey");
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

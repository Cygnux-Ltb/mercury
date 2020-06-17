package io.mercury.transport.rabbitmq.declare;

import static java.lang.String.valueOf;

import io.mercury.common.util.Assertor;

public final class AmqpExchange {

	// ExchangeType
	private ExchangeType type;
	// name
	private String name;
	// 是否持久化
	private boolean durable = true;
	// 没有使用时自动删除
	private boolean autoDelete = false;
	// 是否为内部Exchange
	private boolean internal = false;

	public static AmqpExchange fanout(String name) {
		return new AmqpExchange(ExchangeType.Fanout, Assertor.nonEmpty(name, "name"));
	}

	public static AmqpExchange direct(String name) {
		return new AmqpExchange(ExchangeType.Direct, Assertor.nonEmpty(name, "name"));
	}

	public static AmqpExchange topic(String name) {
		return new AmqpExchange(ExchangeType.Topic, Assertor.nonEmpty(name, "name"));
	}

	/**
	 * The Anonymous Exchange
	 */
	public static final AmqpExchange Anonymous = new AmqpExchange(ExchangeType.Anonymous, "");

	/**
	 * 
	 * @param type
	 * @param name
	 */
	private AmqpExchange(ExchangeType type, String name) {
		this.type = type;
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public ExchangeType type() {
		return type;
	}

	/**
	 * @return the name
	 */
	public String name() {
		return name;
	}

	/**
	 * @return the durable
	 */
	public boolean durable() {
		return durable;
	}

	/**
	 * @return the autoDelete
	 */
	public boolean autoDelete() {
		return autoDelete;
	}

	/**
	 * @return the internal
	 */
	public boolean internal() {
		return internal;
	}

	/**
	 * @param durable the durable to set
	 */
	public AmqpExchange durable(boolean durable) {
		this.durable = durable;
		return this;
	}

	/**
	 * @param autoDelete the autoDelete to set
	 */
	public AmqpExchange autoDelete(boolean autoDelete) {
		this.autoDelete = autoDelete;
		return this;
	}

	/**
	 * @param internal the internal to set
	 */
	public AmqpExchange internal(boolean internal) {
		this.internal = internal;
		return this;
	}

	private final static String Template = "Exchange([name==$name], [type==$type], [durable==$durable], "
			+ "[autoDelete==$autoDelete], [internal==$internal])";

	@Override
	public String toString() {
		return Template.replace("$name", name).replace("$type", valueOf(type)).replace("$durable", valueOf(durable))
				.replace("$autoDelete", valueOf(autoDelete)).replace("$internal", valueOf(internal));
	}

	public boolean idempotent(AmqpExchange another) {
		return name.equals(another.name) && type == another.type && durable == another.durable
				&& autoDelete == another.autoDelete && internal == another.internal;
	}

	public static enum ExchangeType {
		Direct, Fanout, Topic, Anonymous
	}

}

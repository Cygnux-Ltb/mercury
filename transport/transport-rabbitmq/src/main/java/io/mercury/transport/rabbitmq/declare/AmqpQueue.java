package io.mercury.transport.rabbitmq.declare;

import static java.lang.String.valueOf;

import io.mercury.common.util.Assertor;

public final class AmqpQueue {

	private String name;
	// 是否持久化
	private boolean durable = true;
	// 连接独占此队列
	private boolean exclusive = false;
	// channel关闭后自动删除队列
	private boolean autoDelete = false;

	public static AmqpQueue named(String name) {
		return new AmqpQueue(Assertor.nonEmpty(name, "name"));
	}

	private AmqpQueue(String name) {
		this.name = name;
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
	 * @return the exclusive
	 */
	public boolean exclusive() {
		return exclusive;
	}

	/**
	 * @return the autoDelete
	 */
	public boolean autoDelete() {
		return autoDelete;
	}

	/**
	 * @param durable the durable to set
	 */
	public AmqpQueue durable(boolean durable) {
		this.durable = durable;
		return this;
	}

	/**
	 * @param exclusive the exclusive to set
	 */
	public AmqpQueue exclusive(boolean exclusive) {
		this.exclusive = exclusive;
		return this;
	}

	/**
	 * @param autoDelete the autoDelete to set
	 */
	public AmqpQueue autoDelete(boolean autoDelete) {
		this.autoDelete = autoDelete;
		return this;
	}

	private final static String Template = "Queue([name==$name],[durable==$durable],[exclusive==$exclusive],[autoDelete==$autoDelete])";

	@Override
	public String toString() {
		return Template.replace("$name", name).replace("$durable", valueOf(durable))
				.replace("$exclusive", valueOf(exclusive)).replace("$autoDelete", valueOf(autoDelete));
	}

}

package io.mercury.transport.rmq.declare;

import java.util.Map;

import javax.annotation.Nonnull;

import io.mercury.common.collections.MapUtil;
import io.mercury.common.lang.Assertor;
import io.mercury.serialization.json.JsonWrapper;

public final class AmqpExchange {

	/**
	 * The <b> Anonymous </b> Exchange
	 */
	public static final AmqpExchange Anonymous = new AmqpExchange(ExchangeType.Anonymous, "");

	// 交换器名称
	private final String name;

	// 交换器类型
	private final ExchangeType type;

	// 是否持久化
	private boolean durable = true;

	// 没有使用时自动删除
	private boolean autoDelete = false;

	// 是否为内部Exchange
	private boolean internal = false;

	// 交换器参数
	private Map<String, Object> args = null;

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
	 * 定义<b> [FANOUT] </b>交换器
	 * 
	 * @param name
	 * @return
	 */
	public static AmqpExchange fanout(@Nonnull String name) {
		Assertor.nonEmpty(name, "name");
		return new AmqpExchange(ExchangeType.Fanout, name);
	}

	/**
	 * 定义<b> [DIRECT] </b>交换器
	 * 
	 * @param name
	 * @return
	 */
	public static AmqpExchange direct(@Nonnull String name) {
		Assertor.nonEmpty(name, "name");
		return new AmqpExchange(ExchangeType.Direct, name);
	}

	/**
	 * 定义<b> [TOPIC] </b>交换器
	 * 
	 * @param name
	 * @return
	 */
	public static AmqpExchange topic(@Nonnull String name) {
		Assertor.nonEmpty(name, "name");
		return new AmqpExchange(ExchangeType.Topic, name);
	}

	public AmqpExchange setDurable(boolean durable) {
		this.durable = durable;
		return this;
	}

	public AmqpExchange setAutoDelete(boolean autoDelete) {
		this.autoDelete = autoDelete;
		return this;
	}

	public AmqpExchange setInternal(boolean internal) {
		this.internal = internal;
		return this;
	}

	public AmqpExchange setArgs(Map<String, Object> args) {
		this.args = args;
		return this;
	}

	public String getName() {
		return name;
	}

	public ExchangeType getType() {
		return type;
	}

	public boolean isDurable() {
		return durable;
	}

	public boolean isAutoDelete() {
		return autoDelete;
	}

	public boolean isInternal() {
		return internal;
	}

	public Map<String, Object> getArgs() {
		return args;
	}

	@Override
	public String toString() {
		return JsonWrapper.toJsonHasNulls(this);
	}

	/**
	 * 
	 * @param another
	 * @return
	 */
	public boolean isIdempotent(AmqpExchange another) {
		if (another == null)
			return false;
		return name.equals(another.name) 
				&& type == another.type 
				&& durable == another.durable
				&& autoDelete == another.autoDelete 
				&& internal == another.internal
				&& MapUtil.isEquals(args, another.args);
	}

	public static enum ExchangeType {
		Direct, Fanout, Topic, Anonymous
	}

}

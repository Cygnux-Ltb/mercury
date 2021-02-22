package io.mercury.transport.rabbitmq.declare;

import java.util.Map;

import javax.annotation.Nonnull;

import io.mercury.common.collections.MapUtil;
import io.mercury.common.util.Assertor;
import io.mercury.serialization.json.JsonWrapper;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public final class AmqpExchange {

	// 交换器名称
	@Getter
	@Setter
	private String name;

	// 交换器类型
	@Getter
	@Setter
	private ExchangeType type;

	// 是否持久化
	@Getter
	@Setter
	private boolean durable = true;

	// 没有使用时自动删除
	@Getter
	@Setter
	private boolean autoDelete = false;

	// 是否为内部Exchange
	@Getter
	@Setter
	private boolean internal = false;

	// 交换器参数
	@Getter
	@Setter
	private Map<String, Object> args = null;

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

	/**
	 * The <b> Anonymous </b> Exchange
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

	@Override
	public String toString() {
		return JsonWrapper.toJsonHasNulls(this);
	}

	/**
	 * 
	 * @param another
	 * @return
	 */
	public boolean idempotent(AmqpExchange another) {
		return name.equals(another.name) && type == another.type && durable == another.durable
				&& autoDelete == another.autoDelete && internal == another.internal
				&& MapUtil.isEquals(args, another.args);
	}

	public static enum ExchangeType {
		Direct, Fanout, Topic, Anonymous
	}

}

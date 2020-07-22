package io.mercury.transport.rabbitmq.declare;

import static java.lang.String.valueOf;

import java.util.Map;

import io.mercury.common.collections.MapUtil;
import io.mercury.common.util.Assertor;

public final class AmqpExchange {

	// 交换器名称
	private String name;
	// 交换器类型
	private ExchangeType type;
	// 是否持久化
	private boolean durable = true;
	// 没有使用时自动删除
	private boolean autoDelete = false;
	// 是否为内部Exchange
	private boolean internal = false;
	// 交换器参数
	private Map<String, Object> args;

	/**
	 * 定义 fanout 交换器
	 * 
	 * @param name
	 * @return
	 */
	public static AmqpExchange fanout(String name) {
		return new AmqpExchange(ExchangeType.Fanout, Assertor.nonEmpty(name, "name"));
	}

	/**
	 * 定义 direct 交换器
	 * 
	 * @param name
	 * @return
	 */
	public static AmqpExchange direct(String name) {
		return new AmqpExchange(ExchangeType.Direct, Assertor.nonEmpty(name, "name"));
	}

	/**
	 * 定义 topic 交换器
	 * 
	 * @param name
	 * @return
	 */
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
	 * 
	 * @return the exchange args
	 */
	public Map<String, Object> args() {
		return args;
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

	/**
	 * 
	 * @param args the args to set
	 * @return
	 */
	public AmqpExchange args(Map<String, Object> args) {
		this.args = args;
		return this;
	}

	private final static String Template = "Exchange([name==$name], [type==$type], [durable==$durable], "
			+ "[autoDelete==$autoDelete], [internal==$internal], [args==$args])";

	@Override
	public String toString() {
		return Template.replace("$name", name).replace("$type", valueOf(type)).replace("$durable", valueOf(durable))
				.replace("$autoDelete", valueOf(autoDelete)).replace("$internal", valueOf(internal))
				.replace("$args", valueOf(args));
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

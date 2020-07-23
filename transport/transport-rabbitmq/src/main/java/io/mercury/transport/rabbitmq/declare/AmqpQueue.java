package io.mercury.transport.rabbitmq.declare;

import java.util.Map;

import io.mercury.common.collections.MapUtil;
import io.mercury.common.util.Assertor;
import io.mercury.serialization.json.JsonUtil;

public final class AmqpQueue {

	// 队列名称
	private String name;
	// 是否持久化
	private boolean durable = true;
	// 连接独占此队列
	private boolean exclusive = false;
	// channel关闭后自动删除队列
	private boolean autoDelete = false;
	// 队列参数
	private Map<String, Object> args;

	/**
	 * 定义队列
	 * 
	 * @param name
	 * @return
	 */
	public static AmqpQueue named(String name) {
		Assertor.nonEmpty(name, "name");
		return new AmqpQueue(name);
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
	 * 
	 * @return the args
	 */
	public Map<String, Object> args() {
		return args;
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

	/**
	 * 
	 * @param args the args to set
	 * @return
	 */
	public AmqpQueue args(Map<String, Object> args) {
		this.args = args;
		return this;
	}

	@Override
	public String toString() {
		return JsonUtil.toJsonHasNulls(this);
	}

	public boolean idempotent(AmqpQueue another) {
		return name.equals(another.name) && durable == another.durable && exclusive == another.exclusive
				&& autoDelete == another.autoDelete && MapUtil.isEquals(args, another.args);
	}

}

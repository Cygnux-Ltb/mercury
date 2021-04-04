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
public final class AmqpQueue {

	// 队列名称
	@Getter
	private String name;

	// 是否持久化
	@Getter
	@Setter
	private boolean durable = true;

	// 连接独占此队列
	@Getter
	@Setter
	private boolean exclusive = false;

	// channel关闭后自动删除队列
	@Getter
	@Setter
	private boolean autoDelete = false;

	// 队列参数
	@Getter
	@Setter
	private Map<String, Object> args = null;

	/**
	 * 定义队列
	 * 
	 * @param name
	 * @return
	 */
	public static AmqpQueue named(@Nonnull String name) {
		Assertor.nonEmpty(name, "name");
		return new AmqpQueue(name);
	}

	private AmqpQueue(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return JsonWrapper.toJsonHasNulls(this);
	}

	public boolean isIdempotent(AmqpQueue another) {
		return name.equals(another.name) && durable == another.durable && exclusive == another.exclusive
				&& autoDelete == another.autoDelete && MapUtil.isEquals(args, another.args);
	}

}

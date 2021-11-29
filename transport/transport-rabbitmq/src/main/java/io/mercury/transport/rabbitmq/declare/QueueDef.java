package io.mercury.transport.rabbitmq.declare;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import io.mercury.common.collections.MutableLists;
import io.mercury.common.util.Assertor;
import io.mercury.serialization.json.JsonWrapper;
import io.mercury.transport.rabbitmq.RabbitMqOperator;
import io.mercury.transport.rabbitmq.exception.DeclareException;

/**
 * 定义Queue和其他实体绑定关系
 * 
 * @author yellow013
 *
 */
public final class QueueDefinition extends Relationship {

	/**
	 * queue
	 */

	private final AmqpQueue queue;

	/**
	 * 
	 * @param name
	 * @return
	 */
	public static QueueDefinition named(String name) {
		return withQueue(AmqpQueue.named(name));
	}

	/**
	 * 
	 * @param queue
	 * @return
	 */
	public static QueueDefinition withQueue(AmqpQueue queue) {
		Assertor.nonNull(queue, "queue");
		return new QueueDefinition(queue);
	}

	private QueueDefinition(AmqpQueue queue) {
		this.queue = queue;
	}

	@Override
	protected void declare0(RabbitMqOperator declarator) {
		try {
			declarator.declareQueue(queue);
		} catch (DeclareException e) {
			log.error("Declare Queue failure -> {}", queue);
			throw new RuntimeException(e);
		}
	}

	public AmqpQueue getQueue() {
		return queue;
	}

	/**
	 * 
	 * @return the queue name
	 */
	public String getQueueName() {
		return queue.getName();
	}

	/**
	 * 是否持久化, 默认true
	 * 
	 * @param durable
	 * @return
	 */
	public QueueDefinition setDurable(boolean durable) {
		queue.setDurable(durable);
		return this;
	}

	/**
	 * channel关闭后自动删除队列, 默认false
	 * 
	 * @param autoDelete
	 * @return
	 */
	public QueueDefinition setAutoDelete(boolean autoDelete) {
		queue.setAutoDelete(autoDelete);
		return this;
	}

	/**
	 * 连接独占此队列, 默认false
	 * 
	 * @param exclusive
	 * @return
	 */
	public QueueDefinition setExclusive(boolean exclusive) {
		queue.setExclusive(exclusive);
		return this;
	}

	/**
	 * 
	 * @param args
	 * @return
	 */
	public QueueDefinition setArgs(Map<String, Object> args) {
		queue.setArgs(args);
		return this;
	}

	/**
	 * 
	 * @param exchanges
	 * @return
	 */
	public QueueDefinition binding(AmqpExchange... exchanges) {
		return binding(exchanges != null ? MutableLists.newFastList(exchanges) : null, null);
	}

	/**
	 * 
	 * @param exchanges
	 * @param routingKeys
	 * @return
	 */
	public QueueDefinition binding(Collection<AmqpExchange> exchanges, Collection<String> routingKeys) {
		if (exchanges != null) {
			exchanges.forEach(exchange -> {
				if (CollectionUtils.isNotEmpty(routingKeys))
					routingKeys.forEach(routingKey -> bindings.add(new Binding(exchange, queue, routingKey)));
				else
					bindings.add(new Binding(exchange, queue));
			});
		}
		return this;
	}

	@Override
	public String toString() {
		return JsonWrapper.toJsonHasNulls(this);
	}

	public static void main(String[] args) {
		System.out.println(
				QueueDefinition.named("TEST").binding(AmqpExchange.fanout("T0")).binding(AmqpExchange.fanout("T1")));
	}

}
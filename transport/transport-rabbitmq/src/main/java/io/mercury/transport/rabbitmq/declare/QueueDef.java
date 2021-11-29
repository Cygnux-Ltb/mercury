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
public final class QueueDef extends Relationship {

	/**
	 * queue
	 */

	private final AmqpQueue queue;

	/**
	 * 
	 * @param name
	 * @return
	 */
	public static QueueDef named(String name) {
		return withQueue(AmqpQueue.named(name));
	}

	/**
	 * 
	 * @param queue
	 * @return
	 */
	public static QueueDef withQueue(AmqpQueue queue) {
		Assertor.nonNull(queue, "queue");
		return new QueueDef(queue);
	}

	private QueueDef(AmqpQueue queue) {
		this.queue = queue;
	}

	@Override
	protected void declare0(RabbitMqOperator operator) {
		try {
			operator.declareQueue(queue);
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
	public QueueDef setDurable(boolean durable) {
		queue.setDurable(durable);
		return this;
	}

	/**
	 * channel关闭后自动删除队列, 默认false
	 * 
	 * @param autoDelete
	 * @return
	 */
	public QueueDef setAutoDelete(boolean autoDelete) {
		queue.setAutoDelete(autoDelete);
		return this;
	}

	/**
	 * 连接独占此队列, 默认false
	 * 
	 * @param exclusive
	 * @return
	 */
	public QueueDef setExclusive(boolean exclusive) {
		queue.setExclusive(exclusive);
		return this;
	}

	/**
	 * 
	 * @param args
	 * @return
	 */
	public QueueDef setArgs(Map<String, Object> args) {
		queue.setArgs(args);
		return this;
	}

	/**
	 * 
	 * @param exchanges
	 * @return
	 */
	public QueueDef binding(AmqpExchange... exchanges) {
		return binding(exchanges != null ? MutableLists.newFastList(exchanges) : null, null);
	}

	/**
	 * 
	 * @param exchanges
	 * @param routingKeys
	 * @return
	 */
	public QueueDef binding(Collection<AmqpExchange> exchanges, Collection<String> routingKeys) {
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
		System.out
				.println(QueueDef.named("TEST").binding(AmqpExchange.fanout("T0")).binding(AmqpExchange.fanout("T1")));
	}

}
package io.mercury.transport.rabbitmq.declare;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import io.mercury.common.collections.MutableLists;
import io.mercury.serialization.json.JsonUtil;
import io.mercury.transport.rabbitmq.RabbitMqDeclareOperator;
import io.mercury.transport.rabbitmq.exception.DeclareException;

/**
 * 定义Queue和其他实体绑定关系
 * 
 * @author yellow013
 *
 */
public final class QueueRelationship extends Relationship {

	/**
	 * queue
	 */
	private AmqpQueue queue;

	/**
	 * 
	 * @param name
	 * @return
	 */
	public static QueueRelationship named(String name) {
		return new QueueRelationship(AmqpQueue.named(name));
	}

	/**
	 * 
	 * @param queue
	 * @return
	 */
	public static QueueRelationship withQueue(AmqpQueue queue) {
		return new QueueRelationship(queue);
	}

	/**
	 * 
	 * @param queue
	 */
	private QueueRelationship(AmqpQueue queue) {
		this.queue = queue;
	}

	@Override
	protected void declare0(RabbitMqDeclareOperator operator) {
		try {
			operator.declareQueue(queue);
		} catch (DeclareException e) {
			log.error("Declare Queue failure -> {}", queue);
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return the queue
	 */
	public AmqpQueue queue() {
		return queue;
	}

	/**
	 * <b>queue().name()<b><br>
	 * 
	 * @return the queue name
	 */
	public String queueName() {
		return queue.name();
	}

	/**
	 * 
	 * @param durable
	 * @return
	 */
	public QueueRelationship queueDurable(boolean durable) {
		queue.durable(durable);
		return this;
	}

	/**
	 * 
	 * @param autoDelete
	 * @return
	 */
	public QueueRelationship queueAutoDelete(boolean autoDelete) {
		queue.autoDelete(autoDelete);
		return this;
	}

	/**
	 * 
	 * @param exclusive
	 * @return
	 */
	public QueueRelationship queueExclusive(boolean exclusive) {
		queue.exclusive(exclusive);
		return this;
	}

	/**
	 * 
	 * @param exchanges
	 * @return
	 */
	public QueueRelationship binding(AmqpExchange... exchanges) {
		return binding(exchanges != null ? MutableLists.newFastList(exchanges) : null, null);
	}

	/**
	 * 
	 * @param exchanges
	 * @param routingKeys
	 * @return
	 */
	public QueueRelationship binding(List<AmqpExchange> exchanges, List<String> routingKeys) {
		if (exchanges != null) {
			exchanges.forEach(exchange -> {
				if (CollectionUtils.isNotEmpty(routingKeys)) {
					routingKeys.forEach(routingKey -> bindings.add(new Binding(exchange, queue, routingKey)));
				} else {
					bindings.add(new Binding(exchange, queue));
				}
			});
		}
		return this;
	}

	@Override
	public String toString() {
		return JsonUtil.toJsonHasNulls(this);
	}

	public static void main(String[] args) {
		System.out.println(
				QueueRelationship.named("TEST").binding(AmqpExchange.fanout("T0")).binding(AmqpExchange.fanout("T1")));
	}

}
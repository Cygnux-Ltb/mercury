package io.mercury.transport.rabbitmq.declare;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import io.mercury.common.collections.MutableLists;
import io.mercury.common.util.Assertor;
import io.mercury.serialization.json.JsonWrapper;
import io.mercury.transport.rabbitmq.RabbitMqDeclarator;
import io.mercury.transport.rabbitmq.exception.DeclareException;
import lombok.Getter;

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
	@Getter
	private final AmqpQueue queue;

	/**
	 * 
	 * @param name
	 * @return
	 */
	public static QueueRelationship named(String name) {
		return withQueue(AmqpQueue.named(name));
	}

	/**
	 * 
	 * @param queue
	 * @return
	 */
	public static QueueRelationship withQueue(AmqpQueue queue) {
		Assertor.nonNull(queue, "queue");
		return new QueueRelationship(queue);
	}

	private QueueRelationship(AmqpQueue queue) {
		this.queue = queue;
	}

	@Override
	protected void declare0(RabbitMqDeclarator declarator) {
		try {
			declarator.declareQueue(queue);
		} catch (DeclareException e) {
			log.error("Declare Queue failure -> {}", queue);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @return the queue name
	 */
	public String getQueueName() {
		return queue.getName();
	}

	/**
	 * 
	 * @param durable
	 * @return
	 */
	public QueueRelationship queueDurable(boolean durable) {
		queue.setDurable(durable);
		return this;
	}

	/**
	 * 
	 * @param autoDelete
	 * @return
	 */
	public QueueRelationship queueAutoDelete(boolean autoDelete) {
		queue.setAutoDelete(autoDelete);
		return this;
	}

	/**
	 * 
	 * @param exclusive
	 * @return
	 */
	public QueueRelationship queueExclusive(boolean exclusive) {
		queue.setExclusive(exclusive);
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
		return JsonWrapper.toJsonHasNulls(this);
	}

	public static void main(String[] args) {
		System.out.println(
				QueueRelationship.named("TEST").binding(AmqpExchange.fanout("T0")).binding(AmqpExchange.fanout("T1")));
	}

}
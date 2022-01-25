package io.mercury.transport.rabbitmq.declare;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import io.mercury.common.collections.MutableLists;
import io.mercury.common.lang.Assertor;
import io.mercury.serialization.json.JsonWrapper;
import io.mercury.transport.rabbitmq.RmqOperator;
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
	protected void declare0(RmqOperator operator) {
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
	public QueueRelationship setDurable(boolean durable) {
		queue.setDurable(durable);
		return this;
	}

	/**
	 * channel关闭后自动删除队列, 默认false
	 * 
	 * @param autoDelete
	 * @return
	 */
	public QueueRelationship setAutoDelete(boolean autoDelete) {
		queue.setAutoDelete(autoDelete);
		return this;
	}

	/**
	 * 连接独占此队列, 默认false
	 * 
	 * @param exclusive
	 * @return
	 */
	public QueueRelationship setExclusive(boolean exclusive) {
		queue.setExclusive(exclusive);
		return this;
	}

	/**
	 * 
	 * @param args
	 * @return
	 */
	public QueueRelationship setArgs(Map<String, Object> args) {
		queue.setArgs(args);
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
	public QueueRelationship binding(Collection<AmqpExchange> exchanges, Collection<String> routingKeys) {
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
				.println(QueueRelationship.named("TEST").binding(AmqpExchange.fanout("T0")).binding(AmqpExchange.fanout("T1")));
	}

}
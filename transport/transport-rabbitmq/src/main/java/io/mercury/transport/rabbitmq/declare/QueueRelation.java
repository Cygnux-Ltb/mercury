package io.mercury.transport.rabbitmq.declare;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import io.mercury.common.collections.MutableLists;
import io.mercury.transport.rabbitmq.RabbitMqDeclarant;
import io.mercury.transport.rabbitmq.exception.AmqpDeclareException;

/**
 * 定义Queue和其他实体绑定关系
 * 
 * @author yellow013
 *
 */
public final class QueueRelation extends Relation {

	private AmqpQueue queue;

	public static QueueRelation named(String queueName) {
		return new QueueRelation(AmqpQueue.named(queueName));
	}

	public static QueueRelation withQueue(AmqpQueue queue) {
		return new QueueRelation(queue);
	}

	private QueueRelation(AmqpQueue queue) {
		this.queue = queue;
	}

	@Override
	protected void declare0(RabbitMqDeclarant declarant) {
		try {
			declarant.declareQueue(queue);
		} catch (AmqpDeclareException e) {
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

	public QueueRelation queueDurable(boolean durable) {
		queue.durable(durable);
		return this;
	}

	public QueueRelation queueAutoDelete(boolean autoDelete) {
		queue.autoDelete(autoDelete);
		return this;
	}

	public QueueRelation queueExclusive(boolean exclusive) {
		queue.exclusive(exclusive);
		return this;
	}

	public QueueRelation binding(AmqpExchange... exchanges) {
		return binding(exchanges != null ? MutableLists.newFastList(exchanges) : null, null);
	}

	public QueueRelation binding(List<AmqpExchange> exchanges, List<String> routingKeys) {
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

	public static void main(String[] args) {
		QueueRelation.named("TEST");
	}

}
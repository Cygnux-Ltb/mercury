package io.mercury.transport.rabbitmq.declare;

import static io.mercury.common.collections.MutableLists.newFastList;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.commons.collections4.CollectionUtils;

import io.mercury.common.lang.Assertor;
import io.mercury.serialization.json.JsonWrapper;
import io.mercury.transport.rabbitmq.RabbitMqOperator;
import io.mercury.transport.rabbitmq.exception.DeclareException;

/**
 * 定义Exchange和其他实体绑定关系
 * 
 * @author yellow013
 *
 */
public final class ExchangeRelationship extends Relationship {

	public final static ExchangeRelationship Anonymous = new ExchangeRelationship(AmqpExchange.Anonymous);

	/**
	 * exchange
	 */
	private final AmqpExchange exchange;

	/**
	 * 
	 * @param exchange
	 */
	private ExchangeRelationship(AmqpExchange exchange) {
		this.exchange = exchange;
	}

	/**
	 * 
	 * @param exchangeName
	 * @return
	 */
	public static ExchangeRelationship fanout(@Nonnull String exchangeName) {
		return withExchange(AmqpExchange.fanout(exchangeName));
	}

	/**
	 * 
	 * @param exchangeName
	 * @return
	 */
	public static ExchangeRelationship direct(@Nonnull String exchangeName) {
		return withExchange(AmqpExchange.direct(exchangeName));
	}

	/**
	 * 
	 * @param exchangeName
	 * @return
	 */
	public static ExchangeRelationship topic(@Nonnull String exchangeName) {
		return withExchange(AmqpExchange.topic(exchangeName));
	}

	/**
	 * 
	 * @param exchange
	 * @return
	 */
	public static ExchangeRelationship withExchange(@Nonnull AmqpExchange exchange) {
		Assertor.nonNull(exchange, "exchange");
		return new ExchangeRelationship(exchange);
	}

	@Override
	protected void declare0(RabbitMqOperator operator) {
		try {
			operator.declareExchange(exchange);
		} catch (DeclareException e) {
			log.error("Declare Exchange failure -> {}", exchange);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @return the exchange name
	 */
	public String getExchangeName() {
		return exchange.getName();
	}

	/**
	 * 
	 * @param durable
	 * @return
	 */
	public ExchangeRelationship setDurable(boolean durable) {
		exchange.setDurable(durable);
		return this;
	}

	/**
	 * 
	 * @param autoDelete
	 * @return
	 */
	public ExchangeRelationship setAutoDelete(boolean autoDelete) {
		exchange.setAutoDelete(autoDelete);
		return this;
	}

	/**
	 * 
	 * @param internal
	 * @return
	 */
	public ExchangeRelationship setInternal(boolean internal) {
		exchange.setInternal(internal);
		return this;
	}

	/**
	 * 
	 * @param args
	 * @return
	 */
	public ExchangeRelationship setArgs(Map<String, Object> args) {
		exchange.setArgs(args);
		return this;
	}

	/**
	 * 
	 * @param exchanges
	 * @return
	 */
	public ExchangeRelationship bindingExchange(AmqpExchange... exchanges) {
		return bindingExchange(exchanges != null ? newFastList(exchanges) : null, null);
	}

	/**
	 * 
	 * @param exchanges
	 * @param routingKeys
	 * @return
	 */
	public ExchangeRelationship bindingExchange(Collection<AmqpExchange> exchanges, Collection<String> routingKeys) {
		if (exchanges != null) {
			exchanges.forEach(exchange -> {
				if (CollectionUtils.isNotEmpty(routingKeys))
					routingKeys.forEach(routingKey -> bindings.add(new Binding(this.exchange, exchange, routingKey)));
				else
					bindings.add(new Binding(this.exchange, exchange));
			});
		}
		return this;
	}

	/**
	 * 
	 * @param queues
	 * @return
	 */
	public ExchangeRelationship bindingQueue(AmqpQueue... queues) {
		return bindingQueue(queues != null ? newFastList(queues) : null, null);
	}

	/**
	 * 
	 * @param queues
	 * @param routingKeys
	 * @return
	 */
	public ExchangeRelationship bindingQueue(Collection<AmqpQueue> queues, Collection<String> routingKeys) {
		if (queues != null) {
			queues.forEach(queue -> {
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
		System.out.println(ExchangeRelationship.direct("TEST_DIRECT").setAutoDelete(true).setInternal(true));

		AmqpExchange exchange0 = AmqpExchange.direct("ABC");
		AmqpExchange exchange1 = AmqpExchange.direct("ABC");

		System.out.println(exchange0);
		System.out.println(exchange1);

		System.out.println(exchange0 == exchange1);
		System.out.println(exchange0.isIdempotent(exchange1));

		System.out.println(AmqpExchange.direct("ABC"));
		System.out.println(AmqpQueue.named("ABC"));
	}

}
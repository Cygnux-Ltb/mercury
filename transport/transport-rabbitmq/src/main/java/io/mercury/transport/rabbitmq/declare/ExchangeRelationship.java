package io.mercury.transport.rabbitmq.declare;

import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.collections4.CollectionUtils;

import io.mercury.common.collections.MutableLists;
import io.mercury.serialization.json.JsonUtil;
import io.mercury.transport.rabbitmq.RabbitMqDeclarant;
import io.mercury.transport.rabbitmq.exception.AmqpDeclareException;

/**
 * 定义Exchange和其他实体绑定关系
 * 
 * @author yellow013
 *
 */
public final class ExchangeRelationship extends Relationship {

	public final static ExchangeRelationship Anonymous = new ExchangeRelationship(AmqpExchange.Anonymous);

	/**
	 * 
	 */
	private AmqpExchange exchange;

	/**
	 * 
	 * @param exchangeName
	 * @return
	 */
	public static ExchangeRelationship fanout(@Nonnull String exchangeName) {
		return new ExchangeRelationship(AmqpExchange.fanout(exchangeName));
	}

	/**
	 * 
	 * @param exchangeName
	 * @return
	 */
	public static ExchangeRelationship direct(@Nonnull String exchangeName) {
		return new ExchangeRelationship(AmqpExchange.direct(exchangeName));
	}

	/**
	 * 
	 * @param exchangeName
	 * @return
	 */
	public static ExchangeRelationship topic(@Nonnull String exchangeName) {
		return new ExchangeRelationship(AmqpExchange.topic(exchangeName));
	}

	/**
	 * 
	 * @param exchange
	 * @return
	 */
	public static ExchangeRelationship withExchange(@Nonnull AmqpExchange exchange) {
		return new ExchangeRelationship(exchange);
	}

	private ExchangeRelationship(AmqpExchange exchange) {
		this.exchange = exchange;
	}

	@Override
	protected void declare0(RabbitMqDeclarant declarant) {
		try {
			declarant.declareExchange(exchange);
		} catch (AmqpDeclareException e) {
			log.error("Declare Exchange failure -> {}", exchange);
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return the exchange
	 */
	public AmqpExchange exchange() {
		return exchange;
	}

	/**
	 * <b>exchange().name()<b><br>
	 * 
	 * @return the exchange name
	 */
	public String exchangeName() {
		return exchange.name();
	}

	/**
	 * 
	 * @param durable
	 * @return
	 */
	public ExchangeRelationship exchangeDurable(boolean durable) {
		exchange.durable(durable);
		return this;
	}

	/**
	 * 
	 * @param autoDelete
	 * @return
	 */
	public ExchangeRelationship exchangeAutoDelete(boolean autoDelete) {
		exchange.autoDelete(autoDelete);
		return this;
	}

	/**
	 * 
	 * @param internal
	 * @return
	 */
	public ExchangeRelationship exchangeInternal(boolean internal) {
		exchange.internal(internal);
		return this;
	}

	/**
	 * 
	 * @param exchanges
	 * @return
	 */
	public ExchangeRelationship bindingExchange(AmqpExchange... exchanges) {
		return bindingExchange(exchanges != null ? MutableLists.newFastList(exchanges) : null, null);
	}

	/**
	 * 
	 * @param exchanges
	 * @param routingKeys
	 * @return
	 */
	public ExchangeRelationship bindingExchange(List<AmqpExchange> exchanges, List<String> routingKeys) {
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
		return bindingQueue(queues != null ? MutableLists.newFastList(queues) : null, null);
	}

	/**
	 * 
	 * @param queues
	 * @param routingKeys
	 * @return
	 */
	public ExchangeRelationship bindingQueue(List<AmqpQueue> queues, List<String> routingKeys) {
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
		return JsonUtil.toJsonHasNulls(this);
	}

	public static void main(String[] args) {
		System.out.println(ExchangeRelationship.direct("TEST_DIRECT").exchangeAutoDelete(true).exchangeInternal(true));
	}

}
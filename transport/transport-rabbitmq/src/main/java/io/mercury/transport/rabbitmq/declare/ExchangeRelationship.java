package io.mercury.transport.rabbitmq.declare;

import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.collections4.CollectionUtils;

import io.mercury.common.collections.MutableLists;
import io.mercury.transport.rabbitmq.RabbitMqDeclarant;
import io.mercury.transport.rabbitmq.exception.AmqpDeclareException;

/**
 * 定义Exchange和其他实体绑定关系
 * 
 * @author yellow013
 *
 */
public final class ExchangeRelation extends Relation {

	public final static ExchangeRelation Anonymous = new ExchangeRelation(AmqpExchange.Anonymous);

	private AmqpExchange exchange;

	public static ExchangeRelation fanout(@Nonnull String exchangeName) {
		return new ExchangeRelation(AmqpExchange.fanout(exchangeName));
	}

	public static ExchangeRelation direct(@Nonnull String exchangeName) {
		return new ExchangeRelation(AmqpExchange.direct(exchangeName));
	}

	public static ExchangeRelation topic(@Nonnull String exchangeName) {
		return new ExchangeRelation(AmqpExchange.topic(exchangeName));
	}

	public static ExchangeRelation withExchange(@Nonnull AmqpExchange exchange) {
		return new ExchangeRelation(exchange);
	}

	private ExchangeRelation(AmqpExchange exchange) {
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

	public ExchangeRelation exchangeDurable(boolean durable) {
		exchange.durable(durable);
		return this;
	}

	public ExchangeRelation exchangeAutoDelete(boolean autoDelete) {
		exchange.autoDelete(autoDelete);
		return this;
	}

	public ExchangeRelation exchangeInternal(boolean internal) {
		exchange.internal(internal);
		return this;
	}

	public ExchangeRelation bindingExchange(AmqpExchange... exchanges) {
		return bindingExchange(exchanges != null ? MutableLists.newFastList(exchanges) : null, null);
	}

	public ExchangeRelation bindingExchange(List<AmqpExchange> exchanges, List<String> routingKeys) {
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

	public ExchangeRelation bindingQueue(AmqpQueue... queues) {
		return bindingQueue(queues != null ? MutableLists.newFastList(queues) : null, null);
	}

	public ExchangeRelation bindingQueue(List<AmqpQueue> queues, List<String> routingKeys) {
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

	public static void main(String[] args) {
		ExchangeRelation.direct("TEST_DIRECT").exchangeAutoDelete(true).exchangeInternal(true);
	}

}
package io.mercury.transport.rabbitmq.declare;

import org.eclipse.collections.api.list.MutableList;
import org.slf4j.Logger;

import io.mercury.common.annotation.lang.AbstractFunction;
import io.mercury.common.collections.MutableLists;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.transport.rabbitmq.RabbitMqDeclareOperator;
import io.mercury.transport.rabbitmq.exception.AmqpDeclareException;

public abstract class Relationship {

	protected static final Logger log = CommonLoggerFactory.getLogger(Relationship.class);

	protected final MutableList<Binding> bindings = MutableLists.newFastList();

	/**
	 * 
	 * @param operator
	 * @throws AmqpDeclareException
	 */
	public void declare(RabbitMqDeclareOperator operator) throws AmqpDeclareException {
		declare0(operator);
		for (Binding binding : bindings) {
			declareBinding(operator, binding);
		}
	}

	/**
	 * 
	 * @param operator
	 * @param binding
	 * @throws AmqpDeclareException
	 */
	private void declareBinding(RabbitMqDeclareOperator operator, Binding binding) throws AmqpDeclareException {
		AmqpExchange source = binding.source();
		try {
			operator.declareExchange(source);
		} catch (AmqpDeclareException declareException) {
			log.error("Declare source exchange failure -> {}", source);
			throw declareException;
		}
		String routingKey = binding.routingKey();
		switch (binding.destType()) {
		case Exchange:
			AmqpExchange destExchange = binding.destExchange();
			try {
				operator.declareExchange(destExchange);
			} catch (AmqpDeclareException e) {
				log.error("Declare dest exchange failure -> destExchange==[{}]", destExchange, e);
				throw e;
			}
			try {
				operator.bindExchange(destExchange.name(), source.name(), routingKey);
			} catch (AmqpDeclareException e) {
				log.error("Declare bind exchange failure -> destExchange==[{}], source==[{}], routingKey==[{}]",
						destExchange, source, routingKey, e);
				throw e;
			}
			break;
		case Queue:
			AmqpQueue destQueue = binding.destQueue();
			try {
				operator.declareQueue(destQueue);
			} catch (AmqpDeclareException e) {
				log.error("Declare dest queue failure -> destQueue==[{}]", destQueue, e);
				throw e;
			}
			try {
				operator.bindQueue(destQueue.name(), source.name(), routingKey);
			} catch (AmqpDeclareException e) {
				log.error("Declare bind queue failure -> destQueue==[{}], source==[{}], routingKey==[{}]", destQueue,
						source, routingKey, e);
				throw e;
			}
			break;
		default:
			break;
		}
	}

	@AbstractFunction
	protected abstract void declare0(RabbitMqDeclareOperator operator);

}

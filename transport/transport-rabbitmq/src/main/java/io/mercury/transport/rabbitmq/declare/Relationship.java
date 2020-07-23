package io.mercury.transport.rabbitmq.declare;

import org.eclipse.collections.api.list.MutableList;
import org.slf4j.Logger;

import io.mercury.common.annotation.lang.AbstractFunction;
import io.mercury.common.collections.MutableLists;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.transport.rabbitmq.RabbitMqDeclarant;
import io.mercury.transport.rabbitmq.exception.AmqpDeclareException;

public abstract class Relationship {

	protected static final Logger log = CommonLoggerFactory.getLogger(Relationship.class);

	protected final MutableList<Binding> bindings = MutableLists.newFastList();

	/**
	 * 
	 * @param declarant
	 * @throws AmqpDeclareException
	 */
	public void declare(RabbitMqDeclarant declarant) throws AmqpDeclareException {
		declare0(declarant);
		for (Binding binding : bindings)
			declareBinding(declarant, binding);
	}

	/**
	 * 
	 * @param declarant
	 * @param binding
	 * @throws AmqpDeclareException
	 */
	private void declareBinding(RabbitMqDeclarant declarant, Binding binding) throws AmqpDeclareException {
		AmqpExchange source = binding.source();
		try {
			declarant.declareExchange(source);
		} catch (AmqpDeclareException declareException) {
			log.error("Declare source exchange failure -> {}", source);
			throw declareException;
		}
		String routingKey = binding.routingKey();
		switch (binding.destType()) {
		case Exchange:
			AmqpExchange destExchange = binding.destExchange();
			try {
				declarant.declareExchange(destExchange);
			} catch (AmqpDeclareException exception) {
				log.error("Declare dest exchange failure -> destExchange==[{}]", destExchange);
				throw exception;
			}
			try {
				declarant.bindExchange(destExchange.name(), source.name(), routingKey);
			} catch (AmqpDeclareException exception) {
				log.error("Declare bind exchange failure -> destExchange==[{}], source==[{}], routingKey==[{}]",
						destExchange, source, routingKey, exception);
				throw exception;
			}
			break;
		case Queue:
			AmqpQueue destQueue = binding.destQueue();
			try {
				declarant.declareQueue(destQueue);
			} catch (AmqpDeclareException exception) {
				log.error("Declare dest queue failure -> destQueue==[{}]", destQueue, exception);
				throw exception;
			}
			try {
				declarant.bindQueue(destQueue.name(), source.name(), routingKey);
			} catch (AmqpDeclareException exception) {
				log.error("Declare bind queue failure -> destQueue==[{}], source==[{}], routingKey==[{}]", destQueue,
						source, routingKey, exception);
				throw exception;
			}
			break;
		default:
			break;
		}
	}

	@AbstractFunction
	protected abstract void declare0(RabbitMqDeclarant operator);

}

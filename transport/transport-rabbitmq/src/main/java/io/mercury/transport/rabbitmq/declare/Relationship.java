package io.mercury.transport.rabbitmq.declare;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.list.MutableList;
import org.slf4j.Logger;

import io.mercury.common.annotation.lang.AbstractFunction;
import io.mercury.common.collections.MutableLists;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.util.Assertor;
import io.mercury.transport.rabbitmq.RabbitMqOperator;
import io.mercury.transport.rabbitmq.exception.DeclareException;

public abstract class Relationship {

	protected static final Logger log = CommonLoggerFactory.getLogger(Relationship.class);

	protected final MutableList<Binding> bindings = MutableLists.newFastList();

	/**
	 * 
	 * @param operator
	 * @throws DeclareException
	 */
	public void declare(@Nonnull RabbitMqOperator operator) throws DeclareException {
		Assertor.nonNull(operator, "operator");
		declare0(operator);
		for (Binding binding : bindings) {
			declareBinding(operator, binding);
		}
	}

	/**
	 * 
	 * @param operator
	 * @param binding
	 * @throws DeclareException
	 */
	private void declareBinding(RabbitMqOperator declarator, Binding binding) throws DeclareException {
		AmqpExchange source = binding.source;
		try {
			declarator.declareExchange(source);
		} catch (DeclareException declareException) {
			log.error("Declare source exchange failure -> {}", source);
			throw declareException;
		}
		String routingKey = binding.routingKey;
		switch (binding.destType) {
		case Exchange:
			AmqpExchange destExchange = binding.destExchange;
			try {
				declarator.declareExchange(destExchange);
			} catch (DeclareException e) {
				log.error("Declare dest exchange failure -> destExchange==[{}]", destExchange, e);
				throw e;
			}
			try {
				declarator.bindExchange(destExchange.getName(), source.getName(), routingKey);
			} catch (DeclareException e) {
				log.error("Declare bind exchange failure -> destExchange==[{}], source==[{}], routingKey==[{}]",
						destExchange, source, routingKey, e);
				throw e;
			}
			break;
		case Queue:
			AmqpQueue destQueue = binding.destQueue;
			try {
				declarator.declareQueue(destQueue);
			} catch (DeclareException e) {
				log.error("Declare dest queue failure -> destQueue==[{}]", destQueue, e);
				throw e;
			}
			try {
				declarator.bindQueue(destQueue.getName(), source.getName(), routingKey);
			} catch (DeclareException e) {
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
	protected abstract void declare0(RabbitMqOperator operator);

	public final class Binding {

		private final AmqpExchange source;
		private final AmqpExchange destExchange;
		private final AmqpQueue destQueue;
		private final String routingKey;
		private final DestType destType;

		/**
		 * 
		 * @param source
		 * @param destExchange
		 */
		Binding(AmqpExchange source, AmqpExchange destExchange) {
			this(source, destExchange, "");
		}

		/**
		 * 
		 * @param source
		 * @param destExchange
		 * @param routingKey
		 */
		Binding(AmqpExchange source, AmqpExchange destExchange, String routingKey) {
			this(source, destExchange, null, routingKey, DestType.Exchange);
		}

		/**
		 * 
		 * @param source
		 * @param destQueue
		 */
		Binding(AmqpExchange source, AmqpQueue destQueue) {
			this(source, destQueue, "");
		}

		/**
		 * 
		 * @param source
		 * @param destQueue
		 * @param routingKey
		 */
		Binding(AmqpExchange source, AmqpQueue destQueue, String routingKey) {
			this(source, null, destQueue, routingKey, DestType.Queue);
		}

		/**
		 * 
		 * @param source
		 * @param destExchange
		 * @param destQueue
		 * @param routingKey
		 * @param destType
		 */
		Binding(AmqpExchange source, AmqpExchange destExchange, AmqpQueue destQueue, String routingKey,
				DestType destType) {
			this.source = source;
			this.destExchange = destExchange;
			this.destQueue = destQueue;
			this.routingKey = routingKey;
			this.destType = destType;
		}

	}

	static enum DestType {
		Exchange, Queue
	}

}

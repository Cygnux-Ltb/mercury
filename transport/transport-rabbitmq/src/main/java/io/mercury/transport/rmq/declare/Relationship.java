package io.mercury.transport.rmq.declare;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.list.MutableList;
import org.slf4j.Logger;

import io.mercury.common.annotation.AbstractFunction;
import io.mercury.common.collections.MutableLists;
import io.mercury.common.lang.Asserter;
import io.mercury.common.log.Log4j2LoggerFactory;
import io.mercury.transport.rmq.RmqOperator;
import io.mercury.transport.rmq.exception.DeclareException;

public abstract class Relationship {

	protected static final Logger log = Log4j2LoggerFactory.getLogger(Relationship.class);

	protected final MutableList<Binding> bindings = MutableLists.newFastList();

	/**
	 * 
	 * @param operator
	 * @throws DeclareException
	 */
	public void declare(@Nonnull RmqOperator operator) throws DeclareException {
		Asserter.nonNull(operator, "operator");
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
	private void declareBinding(RmqOperator operator, Binding binding) throws DeclareException {
		AmqpExchange source = binding.source;
		try {
			operator.declareExchange(source);
		} catch (DeclareException declareException) {
			log.error("Declare source exchange failure -> {}", source);
			throw declareException;
		}
		String routingKey = binding.routingKey;
		switch (binding.destType) {
		case Exchange:
			AmqpExchange destExchange = binding.destExchange;
			try {
				operator.declareExchange(destExchange);
			} catch (DeclareException e) {
				log.error("Declare dest exchange failure -> destExchange==[{}]", destExchange, e);
				throw e;
			}
			try {
				operator.bindExchange(destExchange.getName(), source.getName(), routingKey);
			} catch (DeclareException e) {
				log.error("Declare bind exchange failure -> destExchange==[{}], source==[{}], routingKey==[{}]",
						destExchange, source, routingKey, e);
				throw e;
			}
			break;
		case Queue:
			AmqpQueue destQueue = binding.destQueue;
			try {
				operator.declareQueue(destQueue);
			} catch (DeclareException e) {
				log.error("Declare dest queue failure -> destQueue==[{}]", destQueue, e);
				throw e;
			}
			try {
				operator.bindQueue(destQueue.getName(), source.getName(), routingKey);
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
	protected abstract void declare0(RmqOperator operator);

	public final class Binding {

		private final AmqpExchange source;
		private final AmqpExchange destExchange;
		private final AmqpQueue destQueue;
		private final String routingKey;
		private final DestType destType;

		/**
		 * 
		 * @param source
		 * @param dest
		 */
		Binding(AmqpExchange source, AmqpExchange dest) {
			this(source, dest, "");
		}

		/**
		 * 
		 * @param source
		 * @param dest
		 * @param routingKey
		 */
		Binding(AmqpExchange source, AmqpExchange dest, String routingKey) {
			this(source, dest, null, routingKey, DestType.Exchange);
		}

		/**
		 * 
		 * @param source
		 * @param dest
		 */
		Binding(AmqpExchange source, AmqpQueue dest) {
			this(source, dest, "");
		}

		/**
		 * 
		 * @param source
		 * @param dest
		 * @param routingKey
		 */
		Binding(AmqpExchange source, AmqpQueue dest, String routingKey) {
			this(source, null, dest, routingKey, DestType.Queue);
		}

		/**
		 * 
		 * @param source
		 * @param destExchange
		 * @param destQueue
		 * @param routingKey
		 * @param destType
		 */
		private Binding(AmqpExchange source, AmqpExchange destExchange, AmqpQueue destQueue, String routingKey,
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

package io.mercury.transport.rabbitmq;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.annotation.Nonnull;

import com.rabbitmq.client.AMQP.Queue.DeleteOk;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import io.mercury.common.datetime.DateTimeUtil;
import io.mercury.common.util.Assertor;
import io.mercury.transport.rabbitmq.configurator.RabbitConnection;
import io.mercury.transport.rabbitmq.declare.AmqpExchange;
import io.mercury.transport.rabbitmq.declare.AmqpQueue;
import io.mercury.transport.rabbitmq.exception.DeclareException;

public final class RabbitMqOperator extends RabbitMqTransport {

	/**
	 * Create OperationalChannel of host, port, username and password
	 * 
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public static RabbitMqOperator newWith(String host, int port, String username, String password) {
		return newWith(RabbitConnection.configuration(host, port, username, password).build());
	}

	/**
	 * Create OperationalChannel of host, port, username, password and virtualHost
	 * 
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @param virtualHost
	 * @return
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public static RabbitMqOperator newWith(String host, int port, String username, String password,
			String virtualHost) {
		return newWith(RabbitConnection.configuration(host, port, username, password, virtualHost).build());
	}

	/**
	 * Create OperationalChannel of RmqConnection
	 * 
	 * @param configurator
	 * @return
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public static RabbitMqOperator newWith(RabbitConnection connection) {
		return new RabbitMqOperator(connection);
	}

	/**
	 * Create OperationalChannel of Channel
	 * 
	 * @param channel
	 * @return
	 */
	public static RabbitMqOperator newWith(Channel channel) {
		return new RabbitMqOperator(channel);
	}

	private RabbitMqOperator(RabbitConnection connection) {
		super("declarator-" + DateTimeUtil.datetimeOfMillisecond(), connection);
		createConnection();
	}

	private RabbitMqOperator(Channel channel) {
		super("declarator-with-channel-" + channel.getChannelNumber());
		this.channel = channel;
	}

	/**
	 * 
	 * @param String           -> queue name
	 * @param DefaultParameter -> durable == true, exclusive == false, autoDelete ==
	 *                         false<br>
	 * @throws DeclareException
	 */
	public boolean declareQueueWithDefault(@Nonnull String queue) throws DeclareException {
		return declareQueue(queue, true, false, false, null);
	}

	/**
	 * 
	 * @param queue
	 * @return
	 * @throws DeclareException
	 */
	public boolean declareQueue(@Nonnull AmqpQueue queue) throws DeclareException {
		try {
			Assertor.nonNull(queue, "queue");
		} catch (Exception e) {
			throw DeclareException.with(e);
		}
		return declareQueue(queue.getName(), queue.isDurable(), queue.isExclusive(), queue.isAutoDelete(),
				queue.getArgs());
	}

	/**
	 * 
	 * @param queue
	 * @param durable
	 * @param exclusive
	 * @param autoDelete
	 * @return
	 * @throws DeclareException
	 */
	public boolean declareQueue(@Nonnull String queue, boolean durable, boolean exclusive, boolean autoDelete,
			Map<String, Object> args) throws DeclareException {
		try {
			Assertor.nonEmpty(queue, "queue");
		} catch (Exception e) {
			throw DeclareException.with(e);
		}
		try {
			channel.queueDeclare(queue, durable, exclusive, autoDelete, args);
			return true;
		} catch (Exception e) {
			throw DeclareException.declareQueueError(queue, durable, exclusive, autoDelete, args, e);
		}
	}

	/**
	 * 
	 * @param exchange
	 * @param DefaultParameter -> durable == true, autoDelete == false, internal ==
	 *                         false
	 * @return
	 * @throws ExchangeDeclareException
	 */
	public boolean declareExchange(@Nonnull AmqpExchange exchange) throws DeclareException {
		try {
			Assertor.nonNull(exchange, "exchange");
		} catch (Exception e) {
			throw DeclareException.with(e);
		}
		switch (exchange.getType()) {
		case Direct:
			return declareDirectExchange(exchange.getName(), exchange.isDurable(), exchange.isAutoDelete(),
					exchange.isInternal(), exchange.getArgs());
		case Fanout:
			return declareFanoutExchange(exchange.getName(), exchange.isDurable(), exchange.isAutoDelete(),
					exchange.isInternal(), exchange.getArgs());
		case Topic:
			return declareTopicExchange(exchange.getName(), exchange.isDurable(), exchange.isAutoDelete(),
					exchange.isInternal(), exchange.getArgs());
		default:
			return false;
		}
	}

	/**
	 * 
	 * @param exchange
	 * @return
	 * @throws DeclareException
	 */
	public boolean declareDefaultDirectExchange(@Nonnull String exchange) throws DeclareException {
		return declareDirectExchange(exchange, true, false, false, null);
	}

	/**
	 * 
	 * @param exchange
	 * @param durable
	 * @param autoDelete
	 * @param internal
	 * @param args
	 * @return
	 * @throws DeclareException
	 */
	public boolean declareDirectExchange(@Nonnull String exchange, boolean durable, boolean autoDelete,
			boolean internal, Map<String, Object> args) throws DeclareException {
		return declareExchange(exchange, BuiltinExchangeType.DIRECT, durable, autoDelete, internal, args);
	}

	/**
	 * 
	 * @param exchange
	 * @return
	 * @throws DeclareException
	 */
	public boolean declareDefaultFanoutExchange(@Nonnull String exchange) throws DeclareException {
		return declareFanoutExchange(exchange, true, false, false, null);
	}

	/**
	 * 
	 * @param exchange
	 * @param durable
	 * @param autoDelete
	 * @param internal
	 * @param args
	 * @return
	 * @throws DeclareException
	 */
	public boolean declareFanoutExchange(@Nonnull String exchange, boolean durable, boolean autoDelete,
			boolean internal, Map<String, Object> args) throws DeclareException {
		return declareExchange(exchange, BuiltinExchangeType.FANOUT, durable, autoDelete, internal, args);
	}

	/**
	 * 
	 * @param exchange
	 * @return
	 * @throws DeclareException
	 */
	public boolean declareDefaultTopicExchange(@Nonnull String exchange) throws DeclareException {
		return declareTopicExchange(exchange, true, false, false, null);
	}

	/**
	 * 
	 * @param exchange
	 * @param durable
	 * @param autoDelete
	 * @param internal
	 * @param args
	 * @return
	 * @throws DeclareException
	 */
	public boolean declareTopicExchange(@Nonnull String exchange, boolean durable, boolean autoDelete, boolean internal,
			Map<String, Object> args) throws DeclareException {
		return declareExchange(exchange, BuiltinExchangeType.TOPIC, durable, autoDelete, internal, args);
	}

	private boolean declareExchange(String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete,
			boolean internal, Map<String, Object> arg) throws DeclareException {
		try {
			Assertor.nonEmpty(exchange, "exchange");
		} catch (Exception e) {
			throw DeclareException.with(e);
		}
		try {
			/**
			 * exchange : the name of the exchange.
			 * 
			 * type : the exchange type.
			 * 
			 * durable : true if we are declaring a durable exchange (the exchange will
			 * survive a server restart)
			 * 
			 * autoDelete : true if the server should delete the exchange when it is no
			 * longer in use.
			 * 
			 * internal : true if the exchange is internal, i.e. can't be directly published
			 * to by a client.
			 * 
			 * arguments : other properties (construction arguments) for the exchange.
			 * 
			 */
			channel.exchangeDeclare(exchange, type, durable, autoDelete, internal, arg);
			return true;
		} catch (IOException e) {
			throw DeclareException.declareExchangeError(exchange, type, durable, autoDelete, internal, arg, e);
		}
	}

	/**
	 * 
	 * @param queue
	 * @param exchange
	 * @return
	 * @throws DeclareException
	 */
	public boolean bindQueue(String queue, String exchange) throws DeclareException {
		return bindQueue(queue, exchange, "");
	}

	/**
	 * 
	 * @param queue
	 * @param exchange
	 * @param routingKey
	 * @return
	 * @throws DeclareException
	 */
	public boolean bindQueue(String queue, String exchange, String routingKey) throws DeclareException {
		try {
			Assertor.nonEmpty(queue, "queue");
			Assertor.nonEmpty(exchange, "exchange");
		} catch (Exception e) {
			throw DeclareException.with(e);
		}
		try {
			/**
			 * queue : the name of the queue.
			 * 
			 * exchange : the name of the exchange.
			 * 
			 * routingKey : the routing key to use for the binding.
			 * 
			 */
			channel.queueBind(queue, exchange, routingKey == null ? "" : routingKey);
			return true;
		} catch (IOException e) {
			throw DeclareException.bindQueueError(queue, exchange, routingKey, e);
		}
	}

	/**
	 * 
	 * @param destExchange
	 * @param sourceExchange
	 * @return
	 * @throws DeclareException
	 */
	public boolean bindExchange(String destExchange, String sourceExchange) throws DeclareException {
		return bindExchange(destExchange, sourceExchange, "");
	}

	/**
	 * 
	 * @param destExchange
	 * @param sourceExchange
	 * @param routingKey
	 * @return
	 * @throws DeclareException
	 */
	public boolean bindExchange(String destExchange, String sourceExchange, String routingKey) throws DeclareException {
		try {
			Assertor.nonEmpty(destExchange, "destExchange");
			Assertor.nonEmpty(sourceExchange, "sourceExchange");
		} catch (Exception e) {
			throw DeclareException.with(e);
		}
		try {
			/**
			 * destination : the name of the exchange to which messages flow across the
			 * binding.
			 * 
			 * source : the name of the exchange from which messages flow across the
			 * binding.
			 * 
			 * routingKey : the routing key to use for the binding.
			 * 
			 */
			channel.exchangeBind(destExchange, sourceExchange, routingKey == null ? "" : routingKey);
			return true;
		} catch (IOException e) {
			throw DeclareException.bindExchangeError(destExchange, sourceExchange, routingKey, e);
		}
	}

	/**
	 * 
	 * @param queue: the name of the queue
	 * @param force
	 * @return
	 * @throws IOException
	 */
	public int deleteQueue(String queue, boolean force) throws IOException {
		/**
		 * queue : the name of the queue.
		 * 
		 * ifUnused : true if the queue should be deleted only if not in use.
		 * 
		 * ifEmpty : true if the queue should be deleted only if empty.
		 * 
		 */
		DeleteOk delete = channel.queueDelete(queue, !force, !force);
		return delete.getMessageCount();
	}

	/**
	 * 
	 * @param exchange
	 * @param force
	 * @return
	 * @throws IOException
	 */
	public boolean deleteExchange(String exchange, boolean force) throws IOException {
		/**
		 * exchange : the name of the exchange.
		 * 
		 * ifUnused : true to indicate that the exchange is only to be deleted if it is
		 * unused.
		 */
		channel.exchangeDelete(exchange, !force);
		return true;
	}

	@Override
	public String getName() {
		return tag;
	}

	public static void main(String[] args) {
		try {
			RabbitMqOperator declarator = newWith("127.0.0.1", 5672, "guest", "guest");
			System.out.println(declarator.isConnected());
			try {
				declarator.declareFanoutExchange("MarketData", true, false, false, null);
			} catch (DeclareException e) {
				e.printStackTrace();
			}
			declarator.close();
			System.out.println(declarator.isConnected());
		} catch (Exception e) {

		}
	}

}

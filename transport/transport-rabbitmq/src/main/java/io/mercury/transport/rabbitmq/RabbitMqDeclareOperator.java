package io.mercury.transport.rabbitmq;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.annotation.Nonnull;

import com.rabbitmq.client.AMQP.Queue.DeleteOk;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import io.mercury.common.datetime.TimeZone;
import io.mercury.common.util.Assertor;
import io.mercury.transport.rabbitmq.configurator.RmqConnection;
import io.mercury.transport.rabbitmq.declare.AmqpExchange;
import io.mercury.transport.rabbitmq.declare.AmqpQueue;
import io.mercury.transport.rabbitmq.exception.AmqpDeclareException;

public final class RabbitMqDeclareOperator extends AbstractRabbitMqTransport {

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
	public static RabbitMqDeclareOperator newWith(String host, int port, String username, String password) {
		return newWith(RmqConnection.configuration(host, port, username, password).build());
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
	public static RabbitMqDeclareOperator newWith(String host, int port, String username, String password,
			String virtualHost) {
		return newWith(RmqConnection.configuration(host, port, username, password, virtualHost).build());
	}

	/**
	 * Create OperationalChannel of RmqConnection
	 * 
	 * @param configurator
	 * @return
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public static RabbitMqDeclareOperator newWith(RmqConnection connection) {
		return new RabbitMqDeclareOperator(connection);
	}

	/**
	 * Create OperationalChannel of Channel
	 * 
	 * @param channel
	 * @return
	 */
	public static RabbitMqDeclareOperator newWith(Channel channel) {
		return new RabbitMqDeclareOperator(channel);
	}

	private RabbitMqDeclareOperator(RmqConnection connection) {
		super("DeclareOperator-" + ZonedDateTime.now(TimeZone.SYS_DEFAULT), connection);
		createConnection();
	}

	private RabbitMqDeclareOperator(Channel channel) {
		super("DeclareOperator-With-Channel-" + channel.getChannelNumber());
		this.channel = channel;
	}

	/**
	 * 
	 * @param String           -> queue name
	 * @param DefaultParameter -> durable == true, exclusive == false, autoDelete ==
	 *                         false<br>
	 * @throws AmqpDeclareException
	 */
	public boolean declareQueueWithDefault(@Nonnull String queue) throws AmqpDeclareException {
		return declareQueue(queue, true, false, false, null);
	}

	/**
	 * 
	 * @param queue
	 * @return
	 * @throws AmqpDeclareException
	 */
	public boolean declareQueue(@Nonnull AmqpQueue queue) throws AmqpDeclareException {
		try {
			Assertor.nonNull(queue, "queue");
		} catch (Exception e) {
			throw AmqpDeclareException.with(e);
		}
		return declareQueue(queue.name(), queue.durable(), queue.exclusive(), queue.autoDelete(), queue.args());
	}

	/**
	 * 
	 * @param queue
	 * @param durable
	 * @param exclusive
	 * @param autoDelete
	 * @return
	 * @throws AmqpDeclareException
	 */
	public boolean declareQueue(@Nonnull String queue, boolean durable, boolean exclusive, boolean autoDelete,
			Map<String, Object> args) throws AmqpDeclareException {
		try {
			Assertor.nonEmpty(queue, "queue");
		} catch (Exception e) {
			throw AmqpDeclareException.with(e);
		}
		try {
			channel.queueDeclare(queue, durable, exclusive, autoDelete, args);
			return true;
		} catch (Exception e) {
			throw AmqpDeclareException.declareQueueError(queue, durable, exclusive, autoDelete, args, e);
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
	public boolean declareExchange(@Nonnull AmqpExchange exchange) throws AmqpDeclareException {
		try {
			Assertor.nonNull(exchange, "exchange");
		} catch (Exception e) {
			throw AmqpDeclareException.with(e);
		}
		switch (exchange.type()) {
		case Direct:
			return declareDirectExchange(exchange.name(), exchange.durable(), exchange.autoDelete(),
					exchange.internal(), exchange.args());
		case Fanout:
			return declareFanoutExchange(exchange.name(), exchange.durable(), exchange.autoDelete(),
					exchange.internal(), exchange.args());
		case Topic:
			return declareTopicExchange(exchange.name(), exchange.durable(), exchange.autoDelete(), exchange.internal(),
					exchange.args());
		default:
			return false;
		}
	}

	/**
	 * 
	 * @param exchange
	 * @return
	 * @throws AmqpDeclareException
	 */
	public boolean declareDefaultDirectExchange(@Nonnull String exchange) throws AmqpDeclareException {
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
	 * @throws AmqpDeclareException
	 */
	public boolean declareDirectExchange(@Nonnull String exchange, boolean durable, boolean autoDelete,
			boolean internal, Map<String, Object> args) throws AmqpDeclareException {
		return declareExchange(exchange, BuiltinExchangeType.DIRECT, durable, autoDelete, internal, args);
	}

	/**
	 * 
	 * @param exchange
	 * @return
	 * @throws AmqpDeclareException
	 */
	public boolean declareDefaultFanoutExchange(@Nonnull String exchange) throws AmqpDeclareException {
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
	 * @throws AmqpDeclareException
	 */
	public boolean declareFanoutExchange(@Nonnull String exchange, boolean durable, boolean autoDelete,
			boolean internal, Map<String, Object> args) throws AmqpDeclareException {
		return declareExchange(exchange, BuiltinExchangeType.FANOUT, durable, autoDelete, internal, args);
	}

	/**
	 * 
	 * @param exchange
	 * @return
	 * @throws AmqpDeclareException
	 */
	public boolean declareDefaultTopicExchange(@Nonnull String exchange) throws AmqpDeclareException {
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
	 * @throws AmqpDeclareException
	 */
	public boolean declareTopicExchange(@Nonnull String exchange, boolean durable, boolean autoDelete, boolean internal,
			Map<String, Object> args) throws AmqpDeclareException {
		return declareExchange(exchange, BuiltinExchangeType.TOPIC, durable, autoDelete, internal, args);
	}

	private boolean declareExchange(String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete,
			boolean internal, Map<String, Object> arg) throws AmqpDeclareException {
		try {
			Assertor.nonEmpty(exchange, "exchange");
		} catch (Exception e) {
			throw AmqpDeclareException.with(e);
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
			throw AmqpDeclareException.declareExchangeError(exchange, type, durable, autoDelete, internal, arg,
					e);
		}
	}

	/**
	 * 
	 * @param queue
	 * @param exchange
	 * @return
	 * @throws AmqpDeclareException
	 */
	public boolean bindQueue(String queue, String exchange) throws AmqpDeclareException {
		return bindQueue(queue, exchange, "");
	}

	/**
	 * 
	 * @param queue
	 * @param exchange
	 * @param routingKey
	 * @return
	 * @throws AmqpDeclareException
	 */
	public boolean bindQueue(String queue, String exchange, String routingKey) throws AmqpDeclareException {
		try {
			Assertor.nonEmpty(queue, "queue");
			Assertor.nonEmpty(exchange, "exchange");
		} catch (Exception e) {
			throw AmqpDeclareException.with(e);
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
			throw AmqpDeclareException.bindQueueError(queue, exchange, routingKey, e);
		}
	}

	/**
	 * 
	 * @param destExchange
	 * @param sourceExchange
	 * @return
	 * @throws AmqpDeclareException
	 */
	public boolean bindExchange(String destExchange, String sourceExchange) throws AmqpDeclareException {
		return bindExchange(destExchange, sourceExchange, "");
	}

	/**
	 * 
	 * @param destExchange
	 * @param sourceExchange
	 * @param routingKey
	 * @return
	 * @throws AmqpDeclareException
	 */
	public boolean bindExchange(String destExchange, String sourceExchange, String routingKey)
			throws AmqpDeclareException {
		try {
			Assertor.nonEmpty(destExchange, "destExchange");
			Assertor.nonEmpty(sourceExchange, "sourceExchange");
		} catch (Exception e) {
			throw AmqpDeclareException.with(e);
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
			throw AmqpDeclareException.bindExchangeError(destExchange, sourceExchange, routingKey, e);
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
	public String name() {
		return tag;
	}

	public static void main(String[] args) {
		try {
			RabbitMqDeclareOperator operator = newWith("127.0.0.1", 5672, "guest", "guest");
			System.out.println(operator.isConnected());
			try {
				operator.declareFanoutExchange("MarketData", true, false, false, null);
			} catch (AmqpDeclareException e) {
				e.printStackTrace();
			}
			operator.close();
			System.out.println(operator.isConnected());
		} catch (Exception e) {

		}
	}

}

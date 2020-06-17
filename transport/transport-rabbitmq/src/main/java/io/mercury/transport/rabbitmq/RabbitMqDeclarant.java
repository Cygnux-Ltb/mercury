package io.mercury.transport.rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.annotation.Nonnull;

import com.rabbitmq.client.AMQP.Queue.DeleteOk;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import io.mercury.common.util.Assertor;
import io.mercury.transport.rabbitmq.configurator.RmqConnection;
import io.mercury.transport.rabbitmq.declare.AmqpExchange;
import io.mercury.transport.rabbitmq.declare.AmqpQueue;
import io.mercury.transport.rabbitmq.exception.AmqpDeclareException;

public final class RabbitMqDeclarant extends AbstractRabbitMqTransport {

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
	public static RabbitMqDeclarant create(String host, int port, String username, String password) {
		return create(RmqConnection.configuration(host, port, username, password).build());
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
	public static RabbitMqDeclarant create(String host, int port, String username, String password,
			String virtualHost) {
		return create(RmqConnection.configuration(host, port, username, password, virtualHost).build());
	}

	/**
	 * Create OperationalChannel of RmqConnection
	 * 
	 * @param configurator
	 * @return
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public static RabbitMqDeclarant create(RmqConnection connection) {
		return new RabbitMqDeclarant(connection);
	}

	/**
	 * Create OperationalChannel of Channel
	 * 
	 * @param channel
	 * @return
	 */
	public static RabbitMqDeclarant withChannel(Channel channel) {
		return new RabbitMqDeclarant(channel);
	}

	private RabbitMqDeclarant(RmqConnection connection) {
		super(null, "DeclareOperator", connection);
		createConnection();
	}

	private RabbitMqDeclarant(Channel channel) {
		this.channel = channel;
	}

	/**
	 * 
	 * @param String           -> queue name
	 * @param DefaultParameter -> durable == true, exclusive == false, autoDelete ==
	 *                         false<br>
	 * @throws QueueDeclareException
	 */
	public boolean declareQueueDefault(@Nonnull String queue) throws AmqpDeclareException {
		return declareQueue(queue, true, false, false);
	}

	/**
	 * 
	 * @param queue
	 * @return
	 * @throws AmqpDeclareException
	 */
	public boolean declareQueue(@Nonnull AmqpQueue queue) throws AmqpDeclareException {
		Assertor.nonNull(queue, AmqpDeclareException.with(new NullPointerException("param queue is can't null.")));
		return declareQueue(queue.name(), queue.durable(), queue.exclusive(), queue.autoDelete());
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
	public boolean declareQueue(@Nonnull String queue, boolean durable, boolean exclusive, boolean autoDelete)
			throws AmqpDeclareException {
		try {
			Assertor.nonEmpty(queue, "queue");
		} catch (Exception e) {
			throw AmqpDeclareException.with(e);
		}
		try {
			channel.queueDeclare(queue, durable, exclusive, autoDelete, null);
			return true;
		} catch (Exception e) {
			throw AmqpDeclareException.declareQueueError(queue, durable, exclusive, autoDelete, e);
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
		Assertor.nonNull(exchange,
				AmqpDeclareException.with(new NullPointerException("param exchange is can't null.")));
		switch (exchange.type()) {
		case Direct:
			return declareDirectExchange(exchange.name(), exchange.durable(), exchange.autoDelete(),
					exchange.internal());
		case Fanout:
			return declareFanoutExchange(exchange.name(), exchange.durable(), exchange.autoDelete(),
					exchange.internal());
		case Topic:
			return declareTopicExchange(exchange.name(), exchange.durable(), exchange.autoDelete(),
					exchange.internal());
		default:
			return false;
		}
	}

	public boolean declareDefaultDirectExchange(@Nonnull String exchange) throws AmqpDeclareException {
		return declareDirectExchange(exchange, true, false, false);
	}

	public boolean declareDirectExchange(@Nonnull String exchange, boolean durable, boolean autoDelete,
			boolean internal) throws AmqpDeclareException {
		return declareExchange(exchange, BuiltinExchangeType.DIRECT, durable, autoDelete, internal);
	}

	public boolean declareDefaultFanoutExchange(@Nonnull String exchange) throws AmqpDeclareException {
		return declareFanoutExchange(exchange, true, false, false);
	}

	public boolean declareFanoutExchange(@Nonnull String exchange, boolean durable, boolean autoDelete,
			boolean internal) throws AmqpDeclareException {
		return declareExchange(exchange, BuiltinExchangeType.FANOUT, durable, autoDelete, internal);
	}

	public boolean declareDefaultTopicExchange(@Nonnull String exchange) throws AmqpDeclareException {
		return declareTopicExchange(exchange, true, false, false);
	}

	public boolean declareTopicExchange(@Nonnull String exchange, boolean durable, boolean autoDelete, boolean internal)
			throws AmqpDeclareException {
		return declareExchange(exchange, BuiltinExchangeType.TOPIC, durable, autoDelete, internal);
	}

	private boolean declareExchange(String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete,
			boolean internal) throws AmqpDeclareException {
		try {
			Assertor.nonEmpty(exchange, "exchange");
		} catch (Exception e) {
			throw AmqpDeclareException.with(e);
		}
		try {
			channel.exchangeDeclare(exchange, type, durable, autoDelete, internal, null);
			return true;
		} catch (IOException e) {
			throw AmqpDeclareException.declareExchangeError(exchange, type, durable, autoDelete, internal, e);
		}
	}

	public boolean bindQueue(String queue, String exchange) throws AmqpDeclareException {
		return bindQueue(queue, exchange, "");
	}

	public boolean bindQueue(String queue, String exchange, String routingKey) throws AmqpDeclareException {
		try {
			Assertor.nonEmpty(queue, "queue");
			Assertor.nonEmpty(exchange, "exchange");
		} catch (Exception e) {
			throw AmqpDeclareException.with(e);
		}
		try {
			channel.queueBind(queue, exchange, routingKey == null ? "" : routingKey);
			return true;
		} catch (IOException e) {
			throw AmqpDeclareException.bindQueueError(queue, exchange, routingKey, e);
		}
	}

	public boolean bindExchange(String destExchange, String sourceExchange) throws AmqpDeclareException {
		return bindExchange(destExchange, sourceExchange, "");
	}

	public boolean bindExchange(String destExchange, String sourceExchange, String routingKey)
			throws AmqpDeclareException {
		try {
			Assertor.nonEmpty(destExchange, "destExchange");
			Assertor.nonEmpty(sourceExchange, "sourceExchange");
		} catch (Exception e) {
			throw AmqpDeclareException.with(e);
		}
		try {
			channel.exchangeBind(destExchange, sourceExchange, routingKey == null ? "" : routingKey);
			return true;
		} catch (IOException e) {
			throw AmqpDeclareException.bindExchangeError(destExchange, sourceExchange, routingKey, e);
		}
	}

	public int deleteQueue(String queue, boolean force) throws IOException {
		DeleteOk delete = channel.queueDelete(queue, !force, !force);
		return delete.getMessageCount();
	}

	public boolean deleteExchange(String exchange, boolean force) throws IOException {
		channel.exchangeDelete(exchange, !force);
		return true;
	}

	@Override
	public String name() {
		return tag;
	}

	public static void main(String[] args) {

		RabbitMqDeclarant declareOperator;
		try {
			declareOperator = create("127.0.0.1", 5672, "guest", "guest");
			System.out.println(declareOperator.isConnected());
			try {
				declareOperator.declareFanoutExchange("MarketData", true, false, false);
			} catch (AmqpDeclareException e) {
				e.printStackTrace();
			}
			declareOperator.close();
			System.out.println(declareOperator.isConnected());
		} catch (Exception e) {

		}
	}

}

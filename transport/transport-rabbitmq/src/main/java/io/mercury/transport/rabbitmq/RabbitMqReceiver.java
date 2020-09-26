package io.mercury.transport.rabbitmq;

import static io.mercury.common.util.StringUtil.bytesToStr;
import static io.mercury.common.util.StringUtil.nonEmpty;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import io.mercury.common.character.Charsets;
import io.mercury.common.codec.DecodeException;
import io.mercury.common.datetime.TimeZone;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.util.Assertor;
import io.mercury.transport.core.api.Receiver;
import io.mercury.transport.core.api.Subscriber;
import io.mercury.transport.core.exception.ConnectionBreakException;
import io.mercury.transport.core.exception.ReceiverStartException;
import io.mercury.transport.rabbitmq.configurator.RmqConnection;
import io.mercury.transport.rabbitmq.configurator.RmqReceiverConfigurator;
import io.mercury.transport.rabbitmq.declare.ExchangeRelationship;
import io.mercury.transport.rabbitmq.declare.QueueRelationship;
import io.mercury.transport.rabbitmq.exception.AmqpDeclareException;
import io.mercury.transport.rabbitmq.exception.AmqpDeclareRuntimeException;
import io.mercury.transport.rabbitmq.exception.AmqpMsgHandleException;

/**
 * 
 * @author yellow013<br>
 * 
 *         [已完成]改造升级, 使用共同的构建者建立Exchange, RoutingKey, Queue的绑定关系
 *
 */
public class RabbitMqReceiver<T> extends BaseRabbitMqTransport implements Receiver, Subscriber, Runnable {

	private static final Logger log = CommonLoggerFactory.getLogger(RabbitMqReceiver.class);

	// 接收消息使用的反序列化器
	private final Function<byte[], T> deserializer;

	// 接收消息时使用的回调函数
	private final Consumer<T> consumer;

	// 接受者QueueDeclare
	private final QueueRelationship receiveQueue;

	// 接受者QueueName
	private final String queueName;

	// 消息无法处理时发送到的错误消息ExchangeDeclare
	private final ExchangeRelationship errMsgExchange;

	// 消息无法处理时发送到的错误消息Exchange使用的RoutingKey
	private final String errMsgRoutingKey;

	// 消息无法处理时发送到的错误消息QueueDeclare
	private final QueueRelationship errMsgQueue;

	// 消息无法处理时发送到的错误消息Exchange
	private String errMsgExchangeName;

	// 消息无法处理时发送到的错误消息Queue
	private String errMsgQueueName;

	// 是否有错误消息Exchange
	private boolean hasErrMsgExchange;

	// 是否有错误消息Queue
	private boolean hasErrMsgQueue;

	// 自动ACK
	private boolean autoAck;

	// 一次ACK多条
	private boolean multipleAck;

	// ACK最大自动重试次数
	private int maxAckTotal;

	// ACK最大自动重连次数
	private int maxAckReconnection;

	// QOS预取
	private int qos;

	// Receiver名称
	private final String receiverName;

	/**
	 * 
	 * @param configurator
	 * @param consumer
	 * @return
	 */
	public static final RabbitMqReceiver<byte[]> create(@Nonnull RmqReceiverConfigurator configurator,
			@Nonnull Consumer<byte[]> consumer) {
		return create(null, configurator, consumer);
	}

	/**
	 * 
	 * @param tag
	 * @param configurator
	 * @param consumer
	 * @return
	 */
	public static final RabbitMqReceiver<byte[]> create(String tag, @Nonnull RmqReceiverConfigurator configurator,
			@Nonnull Consumer<byte[]> consumer) {
		return create(tag, configurator, msg -> msg, consumer);
	}

	/**
	 * 
	 * @param <T>
	 * @param configurator
	 * @param deserializer
	 * @param consumer
	 * @return
	 */
	public static final <T> RabbitMqReceiver<T> create(@Nonnull RmqReceiverConfigurator configurator,
			@Nonnull Function<byte[], T> deserializer, @Nonnull Consumer<T> consumer) {
		return create(null, configurator, deserializer, consumer);
	}

	/**
	 * 
	 * @param <T>
	 * @param tag
	 * @param configurator
	 * @param deserializer
	 * @param consumer
	 * @return
	 */
	public static final <T> RabbitMqReceiver<T> create(String tag, @Nonnull RmqReceiverConfigurator configurator,
			@Nonnull Function<byte[], T> deserializer, @Nonnull Consumer<T> consumer) {
		return new RabbitMqReceiver<T>(tag, configurator, deserializer, consumer);
	}

	/**
	 * 
	 * @param tag
	 * @param configurator
	 * @param deserializer
	 * @param callback
	 */
	private RabbitMqReceiver(String tag, @Nonnull RmqReceiverConfigurator configurator,
			@Nonnull Function<byte[], T> deserializer, @Nonnull Consumer<T> consumer) {
		super(nonEmpty(tag) ? tag : "Receiver-" + ZonedDateTime.now(TimeZone.SYS_DEFAULT), configurator.connection());
		this.receiveQueue = configurator.receiveQueue();
		this.queueName = receiveQueue.queueName();
		this.deserializer = deserializer;
		this.consumer = consumer;
		this.errMsgExchange = configurator.errMsgExchange();
		this.errMsgRoutingKey = configurator.errMsgRoutingKey();
		this.errMsgQueue = configurator.errMsgQueue();
		this.autoAck = configurator.autoAck();
		this.multipleAck = configurator.multipleAck();
		this.maxAckTotal = configurator.maxAckTotal();
		this.maxAckReconnection = configurator.maxAckReconnection();
		this.qos = configurator.qos();
		this.receiverName = "receiver::" + rmqConnection.fullInfo() + "$" + queueName;
		createConnection();
		declare();
	}

	private void declare() {
		RabbitMqDeclarant declarant = RabbitMqDeclarant.newWith(channel);
		try {
			this.receiveQueue.declare(declarant);
		} catch (AmqpDeclareException e) {
			log.error("Queue declare throw exception -> connection configurator info : {}, error message : {}",
					rmqConnection.fullInfo(), e.getMessage(), e);
			// 在定义Queue和进行绑定时抛出任何异常都需要终止程序
			destroy();
			throw new AmqpDeclareRuntimeException(e);
		}
		if (errMsgExchange != null && errMsgQueue != null) {
			errMsgExchange.bindingQueue(errMsgQueue.queue());
			declareErrMsgExchange(declarant);
		} else if (errMsgExchange != null) {
			declareErrMsgExchange(declarant);
		} else if (errMsgQueue != null) {
			declareErrMsgQueueName(declarant);
		}
	}

	private void declareErrMsgExchange(RabbitMqDeclarant declarant) {
		try {
			this.errMsgExchange.declare(declarant);
		} catch (AmqpDeclareException e) {
			log.error(
					"ErrorMsgExchange declare throw exception -> connection configurator info : {}, error message : {}",
					rmqConnection.fullInfo(), e.getMessage(), e);
			// 在定义Queue和进行绑定时抛出任何异常都需要终止程序
			destroy();
			throw new AmqpDeclareRuntimeException(e);
		}
		this.errMsgExchangeName = errMsgExchange.exchangeName();
		this.hasErrMsgExchange = true;
	}

	private void declareErrMsgQueueName(RabbitMqDeclarant declarant) {
		try {
			this.errMsgQueue.declare(declarant);
		} catch (AmqpDeclareException e) {
			log.error("ErrorMsgQueue declare throw exception -> connection configurator info : {}, error message : {}",
					rmqConnection.fullInfo(), e.getMessage(), e);
			// 在定义Queue和进行绑定时抛出任何异常都需要终止程序
			destroy();
			throw new AmqpDeclareRuntimeException(e);
		}
		this.errMsgQueueName = errMsgQueue.queueName();
		this.hasErrMsgQueue = true;
	}

	@Override
	public void run() {
		receive();
	}

	@Override
	public void subscribe() {
		receive();
	}

	/**
	 * channel.basicConsume(queue, callback) <br>
	 * <br>
	 * channel.basicConsume(queue, autoAck, callback)<br>
	 * <br>
	 * channel.basicConsume(queue, deliverCallback, cancelCallback)<br>
	 * <br>
	 * channel.basicConsume(queue, deliverCallback, shutdownSignalCallback)<br>
	 * <br>
	 * channel.basicConsume(queue, autoAck, deliverCallback, cancelCallback)<br>
	 * <br>
	 * channel.basicConsume(queue, autoAck, deliverCallback,
	 * shutdownSignalCallback)<br>
	 * <br>
	 * channel.basicConsume(queue, autoAck, arguments, callback)<br>
	 * <br>
	 * channel.basicConsume(queue, autoAck, consumerTag, callback)<br>
	 * <br>
	 * channel.basicConsume(queue, deliverCallback, cancelCallback,
	 * shutdownSignalCallback)<br>
	 * <br>
	 * channel.basicConsume(queue, autoAck, deliverCallback, cancelCallback,
	 * shutdownSignalCallback)<br>
	 * <br>
	 * channel.basicConsume(queue, autoAck, arguments, deliverCallback,
	 * cancelCallback)<br>
	 * <br>
	 * channel.basicConsume(queue, autoAck, arguments, deliverCallback,
	 * shutdownSignalCallback)<br>
	 * <br>
	 * channel.basicConsume(queue, autoAck, consumerTag, deliverCallback,
	 * cancelCallback)<br>
	 * <br>
	 * channel.basicConsume(queue, autoAck, consumerTag, deliverCallback,
	 * shutdownSignalCallback)<br>
	 * <br>
	 * channel.basicConsume(queue, autoAck, arguments, deliverCallback,
	 * cancelCallback, shutdownSignalCallback)<br>
	 * <br>
	 * channel.basicConsume(queue, autoAck, consumerTag, deliverCallback,
	 * cancelCallback, shutdownSignalCallback)<br>
	 * <br>
	 * channel.basicConsume(queue, autoAck, consumerTag, noLocal, exclusive,
	 * arguments, callback)<br>
	 * <br>
	 * channel.basicConsume(queue, autoAck, consumerTag, noLocal, exclusive,
	 * arguments, deliverCallback, cancelCallback)<br>
	 * <br>
	 * channel.basicConsume(queue, autoAck, consumerTag, noLocal, exclusive,
	 * arguments, deliverCallback, shutdownSignalCallback)<br>
	 * <br>
	 * channel.basicConsume(queue, autoAck, consumerTag, noLocal, exclusive,
	 * arguments, deliverCallback, cancelCallback, shutdownSignalCallback)<br>
	 * 
	 */
	@Override
	public void receive() throws ReceiverStartException {
		Assertor.nonNull(deserializer, "deserializer");
		Assertor.nonNull(consumer, "consumer");
		try {

			if (!autoAck)
				channel.basicQos(qos);

			// TODO 使用新的API
//			channel.basicConsume(queueName, autoAck, tag, false, false, null, (consumerTag, delivery) -> {
//				log.info("DeliverCallback receive consumerTag -> {}", consumerTag);
//			}, consumerTag -> {
//				log.info("CancelCallback receive consumerTag -> {}", consumerTag);
//			}, (consumerTag, sig) -> {
//				log.info("Consumer received ShutdownSignalException, consumerTag==[{}]", consumerTag);
//				handleShutdownSignal(sig);
//			});

			channel.basicConsume(
					// param1: the name of the queue
					queueName,
					// param2: true if the server should consider messages acknowledged once
					// delivered; false if the server should expect explicit acknowledgement
					autoAck,
					// param3: a client-generated consumer tag to establish context
					tag,
					// param4: an interface to the consumer object
					new DefaultConsumer(channel) {
						@Override
						public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
								byte[] body) throws IOException {
							try {
								log.debug("Message handle start");
								log.debug(
										"Callback handleDelivery, consumerTag==[{}], deliveryTag==[{}] body.length==[{}]",
										consumerTag, envelope.getDeliveryTag(), body.length);
								T apply = null;
								try {
									apply = deserializer.apply(body);
								} catch (Exception e) {
									throw new DecodeException(e);
								}
								consumer.accept(apply);
								log.debug("Callback handleDelivery() end");
							} catch (Exception e) {
								log.error("Consumer accept msg==[{}] throw Exception -> {}", bytesToStr(body),
										e.getMessage(), e);
								dumpUnprocessableMsg(e, consumerTag, envelope, properties, body);
							}
							if (!autoAck) {
								if (ack(envelope.getDeliveryTag())) {
									log.debug("Message handle and ack finished");
								} else {
									log.info("Ack failure envelope.getDeliveryTag()==[{}], Reject message");
									channel.basicReject(envelope.getDeliveryTag(), true);
								}
							}
						}
					});
		} catch (IOException e) {
			log.error("Method basicConsume() IOException message -> {}", e.getMessage(), e);
			throw new ReceiverStartException(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @param cause
	 * @param consumerTag
	 * @param envelope
	 * @param properties
	 * @param body
	 * @throws IOException
	 */
	private void dumpUnprocessableMsg(Throwable cause, String consumerTag, Envelope envelope,
			BasicProperties properties, byte[] body) throws IOException {
		if (hasErrMsgExchange) {
			// Sent message to error dump exchange.
			log.error("Exception handling -> Sent to ErrMsgExchange [{}]", errMsgExchangeName);
			channel.basicPublish(errMsgExchangeName, errMsgRoutingKey, null, body);
			log.error("Exception handling -> Sent to ErrMsgExchange [{}] finished", errMsgExchangeName);
		} else if (hasErrMsgQueue) {
			// Sent message to error dump queue.
			log.error("Exception handling -> Sent to ErrMsgQueue [{}]", errMsgQueueName);
			channel.basicPublish("", errMsgQueueName, null, body);
			log.error("Exception handling -> Sent to ErrMsgQueue finished");
		} else {
			// Reject message and close connection.
			log.error("Exception handling -> Reject Msg [{}]", bytesToStr(body));
			channel.basicReject(envelope.getDeliveryTag(), true);
			log.error("Exception handling -> Reject Msg finished");
			destroy();
			log.error("RabbitMqReceiver: [{}] already closed", receiverName);
			throw new AmqpMsgHandleException(
					"The message could not handle, and could not delivered to the error dump address."
							+ "\n The connection was closed.",
					cause);
		}
	}

	/**
	 * 
	 * @param deliveryTag
	 * @return
	 */
	private boolean ack(long deliveryTag) {
		return ack0(deliveryTag, 0);
	}

	/**
	 * 
	 * @param deliveryTag
	 * @param retry
	 * @return
	 */
	private boolean ack0(long deliveryTag, int retry) {
		if (retry == maxAckTotal) {
			log.error("Has been retry ack {}, Quit ack", maxAckTotal);
			return false;
		}
		log.debug("Has been retry ack {}, Do next ack", retry);
		try {
			int reconnectionCount = 0;
			while (!isConnected()) {
				reconnectionCount++;
				log.debug("Detect connection isConnected() == false, Reconnection count {}", reconnectionCount);
				closeAndReconnection();
				if (reconnectionCount > maxAckReconnection) {
					log.debug("Reconnection count -> {}, Quit current ack", reconnectionCount);
					break;
				}
			}
			if (isConnected()) {
				log.debug("Last detect connection isConnected() == true, Reconnection count {}", reconnectionCount);
				channel.basicAck(deliveryTag, multipleAck);
				log.debug("Call function channel.basicAck() finished");
				return true;
			} else {
				log.error("Last detect connection isConnected() == false, Reconnection count {}", reconnectionCount);
				log.error("Can't call function channel.basicAck()");
				return ack0(deliveryTag, retry);
			}
		} catch (IOException e) {
			log.error("Call function channel.basicAck(deliveryTag==[{}], multiple==[{}]) throw IOException -> {}",
					deliveryTag, multipleAck, e.getMessage(), e);
			return ack0(deliveryTag, ++retry);
		}
	}

	@Override
	public boolean destroy() {
		log.info("Call function destroy() from Receiver name==[{}]", receiverName);
		return super.destroy();
	}

	@Override
	public String name() {
		return receiverName;
	}

	@Override
	public void reconnect() throws ConnectionBreakException, ReceiverStartException {
		closeAndReconnection();
		receive();
	}

	public static void main(String[] args) {
		RabbitMqReceiver<byte[]> receiver = RabbitMqReceiver.create("test",
				RmqReceiverConfigurator
						.configuration(RmqConnection.configuration("127.0.0.1", 5672, "user", "u_pass").build(),
								QueueRelationship.named("TEST"))
						.build(),
				msg -> System.out.println(new String(msg, Charsets.UTF8)));
		receiver.receive();
	}

}

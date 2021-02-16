package io.mercury.transport.rabbitmq;

import static io.mercury.common.util.StringUtil.bytesToStr;
import static io.mercury.common.util.StringUtil.nonEmpty;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.ConsumerShutdownSignalCallback;
import com.rabbitmq.client.Envelope;

import io.mercury.common.codec.DecodeException;
import io.mercury.common.datetime.DateTimeUtil;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.util.Assertor;
import io.mercury.transport.core.api.Receiver;
import io.mercury.transport.core.api.Subscriber;
import io.mercury.transport.core.exception.ConnectionBreakException;
import io.mercury.transport.core.exception.ReceiverStartException;
import io.mercury.transport.rabbitmq.configurator.RmqReceiverConfigurator;
import io.mercury.transport.rabbitmq.declare.ExchangeRelationship;
import io.mercury.transport.rabbitmq.declare.QueueRelationship;
import io.mercury.transport.rabbitmq.exception.DeclareException;
import io.mercury.transport.rabbitmq.exception.DeclareRuntimeException;
import io.mercury.transport.rabbitmq.exception.MsgHandleException;

/**
 * 
 * @author yellow013<br>
 * 
 *         [已完成]改造升级, 使用共同的构建者建立Exchange, RoutingKey, Queue的绑定关系
 *
 */
public class AdvancedRabbitMqReceiver<T> extends AbstractRabbitMqTransport implements Subscriber, Receiver, Runnable {

	private static final Logger log = CommonLoggerFactory.getLogger(AdvancedRabbitMqReceiver.class);

	// 接收消息使用的反序列化器
	private final Function<byte[], T> deserializer;

	// 接收消息时使用的回调函数
	private final Consumer<T> consumer;

	// 接受消费全部消息内容, 包括[consumerTag, 信封, 消息体, 参数]
	private final SelfAckConsumer<T> selfAckConsumer;

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

	// 是否有错误消息Exchange
	private boolean hasErrMsgExchange;

	// 消息无法处理时发送到的错误消息Queue
	private String errMsgQueueName;

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

	// QOS预取值
	private int qos;

	// Receiver名称
	private final String receiverName;

	// 消费者独占队列
	private final boolean exclusive;

	// 消费者参数, 默认为null
	private final Map<String, Object> args = null;

	// 应答代理, 用于在外部回调中控制ACK过程
	private AckDelegate ackDelegate;

	/**
	 * 
	 * @param configurator
	 * @param consumer
	 * @return
	 */
	public static AdvancedRabbitMqReceiver<byte[]> create(@Nonnull RmqReceiverConfigurator configurator,
			@Nonnull Consumer<byte[]> consumer) {
		Assertor.nonNull(configurator, "configurator");
		Assertor.nonNull(consumer, "consumer");
		return new AdvancedRabbitMqReceiver<byte[]>(null, configurator, msg -> msg, consumer, null);
	}

	/**
	 * 
	 * @param tag
	 * @param configurator
	 * @param consumer
	 * @return
	 */
	public static AdvancedRabbitMqReceiver<byte[]> create(String tag, @Nonnull RmqReceiverConfigurator configurator,
			@Nonnull Consumer<byte[]> consumer) {
		Assertor.nonNull(configurator, "configurator");
		Assertor.nonNull(consumer, "consumer");
		return new AdvancedRabbitMqReceiver<byte[]>(tag, configurator, msg -> msg, consumer, null);
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
	public static <T> AdvancedRabbitMqReceiver<T> create(String tag, @Nonnull RmqReceiverConfigurator configurator,
			@Nonnull Function<byte[], T> deserializer, @Nonnull Consumer<T> consumer) {
		Assertor.nonNull(configurator, "configurator");
		Assertor.nonNull(deserializer, "deserializer");
		Assertor.nonNull(consumer, "consumer");
		return new AdvancedRabbitMqReceiver<>(tag, configurator, deserializer, consumer, null);
	}

	/**
	 * 
	 * @param configurator
	 * @param selfAckConsumer
	 * @return
	 */
	public static AdvancedRabbitMqReceiver<byte[]> createWithSelfAck(@Nonnull RmqReceiverConfigurator configurator,
			@Nonnull SelfAckConsumer<byte[]> selfAckConsumer) {
		Assertor.nonNull(configurator, "configurator");
		Assertor.nonNull(selfAckConsumer, "selfAckConsumer");
		return new AdvancedRabbitMqReceiver<>(null, configurator, msg -> msg, null, selfAckConsumer);
	}

	/**
	 * 
	 * @param tag
	 * @param configurator
	 * @param selfAckConsumer
	 * @return
	 */
	public static AdvancedRabbitMqReceiver<byte[]> createWithSelfAck(String tag,
			@Nonnull RmqReceiverConfigurator configurator, @Nonnull SelfAckConsumer<byte[]> selfAckConsumer) {
		Assertor.nonNull(configurator, "configurator");
		Assertor.nonNull(selfAckConsumer, "selfAckConsumer");
		return new AdvancedRabbitMqReceiver<>(tag, configurator, msg -> msg, null, selfAckConsumer);
	}

	/**
	 * 
	 * @param <T>
	 * @param tag
	 * @param configurator
	 * @param deserializer
	 * @param selfAckConsumer
	 * @return
	 */
	public static <T> AdvancedRabbitMqReceiver<T> createWithSelfAck(String tag,
			@Nonnull RmqReceiverConfigurator configurator, @Nonnull Function<byte[], T> deserializer,
			@Nonnull SelfAckConsumer<T> selfAckConsumer) {
		Assertor.nonNull(configurator, "configurator");
		Assertor.nonNull(deserializer, "deserializer");
		Assertor.nonNull(selfAckConsumer, "selfAckConsumer");
		return new AdvancedRabbitMqReceiver<>(tag, configurator, deserializer, null, selfAckConsumer);
	}

	/**
	 * 
	 * @param tag
	 * @param configurator
	 * @param deserializer
	 * @param consumer
	 * @param selfAckConsumer
	 */
	private AdvancedRabbitMqReceiver(String tag, @Nonnull RmqReceiverConfigurator configurator,
			@Nonnull Function<byte[], T> deserializer, @Nullable Consumer<T> consumer,
			@Nullable SelfAckConsumer<T> selfAckConsumer) {
		super(nonEmpty(tag) ? tag : "receiver-" + DateTimeUtil.datetimeOfMillisecond(), configurator.connection());
		if (consumer == null && selfAckConsumer == null) {
			throw new NullPointerException("[Consumer] and [SelfAckConsumer] cannot all be null");
		}
		this.receiveQueue = configurator.receiveQueue();
		this.queueName = receiveQueue.queueName();
		this.deserializer = deserializer;
		this.errMsgExchange = configurator.errMsgExchange();
		this.errMsgRoutingKey = configurator.errMsgRoutingKey();
		this.errMsgQueue = configurator.errMsgQueue();
		this.autoAck = configurator.autoAck();
		this.multipleAck = configurator.multipleAck();
		this.maxAckTotal = configurator.maxAckTotal();
		this.maxAckReconnection = configurator.maxAckReconnection();
		this.qos = configurator.qos();
		this.exclusive = configurator.exclusive();
		this.consumer = consumer;
		this.selfAckConsumer = selfAckConsumer;
		this.receiverName = "receiver::[" + rmqConnection.getConnectionInfo() + "$" + queueName + "]";
		createConnection();
		declareQueue();
		if (selfAckConsumer != null) {
			createAckDelegate();
		}
	}

	/**
	 * 创建ACK委托
	 */
	private void createAckDelegate() {
		this.ackDelegate = new AckDelegate(this);
	}

	/**
	 * 定义相关队列组件
	 * 
	 * @throws DeclareRuntimeException
	 */
	private void declareQueue() throws DeclareRuntimeException {
		RabbitMqDeclarator operator = RabbitMqDeclarator.newWith(channel);
		try {
			this.receiveQueue.declare(operator);
		} catch (DeclareException e) {
			log.error("Queue declare throw exception -> connection info : {}, error message : {}",
					rmqConnection.getConfiguratorInfo(), e.getMessage(), e);
			// 在定义Queue和进行绑定时抛出任何异常都需要终止程序
			destroy();
			throw new DeclareRuntimeException(e);
		}
		if (errMsgExchange != null && errMsgQueue != null) {
			errMsgExchange.bindingQueue(errMsgQueue.queue());
			declareErrMsgExchange(operator);
		} else if (errMsgExchange != null) {
			declareErrMsgExchange(operator);
		} else if (errMsgQueue != null) {
			declareErrMsgQueueName(operator);
		}
	}

	private void declareErrMsgExchange(RabbitMqDeclarator operator) {
		try {
			this.errMsgExchange.declare(operator);
		} catch (DeclareException e) {
			log.error(
					"ErrorMsgExchange declare throw exception -> connection configurator info : {}, error message : {}",
					rmqConnection.getConfiguratorInfo(), e.getMessage(), e);
			// 在定义Queue和进行绑定时抛出任何异常都需要终止程序
			destroy();
			throw new DeclareRuntimeException(e);
		}
		this.errMsgExchangeName = errMsgExchange.exchangeName();
		this.hasErrMsgExchange = true;
	}

	private void declareErrMsgQueueName(RabbitMqDeclarator operator) {
		try {
			this.errMsgQueue.declare(operator);
		} catch (DeclareException e) {
			log.error("ErrorMsgQueue declare throw exception -> connection configurator info : {}, error message : {}",
					rmqConnection.getConfiguratorInfo(), e.getMessage(), e);
			// 在定义Queue和进行绑定时抛出任何异常都需要终止程序
			destroy();
			throw new DeclareRuntimeException(e);
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

	private final CancelCallback defCancelCallback = consumerTag -> {
		log.info("CancelCallback receive consumerTag==[{}]", consumerTag);
	};

	private final ConsumerShutdownSignalCallback defShutdownSignalCallback = (consumerTag, sig) -> {
		log.info("Consumer received ShutdownSignalException, consumerTag==[{}]", consumerTag);
		handleShutdownSignal(sig);
	};

	@Override
	public void receive() throws ReceiverStartException {
		Assertor.nonNull(deserializer, "deserializer");
		// 检测Consumer或SelfAckConsumer, 必须有一个不为null
		if (consumer == null && selfAckConsumer == null) {
			throw new NullPointerException("[Consumer] or [SelfAckConsumer] cannot be all null");
		}
		// 如果[autoAck]为[false], 设置QOS数值
		if (!autoAck) {
			// # Set QOS parameter start *****
			try {
				channel.basicQos(qos);
			} catch (IOException e) {
				log.error("Function basicQos() qos==[{}] throw IOException message -> {}", qos, e.getMessage(), e);
				throw new ReceiverStartException(e.getMessage(), e);
			}
			// # Set QOS parameter end *****
		}

		// 如果[selfAckConsumer]不为null, 设置selfAckConsumer
		if (selfAckConsumer != null) {
			// # Set SelfAckConsumer start *****
			try {
				channel.basicConsume(
						// queue : the name of the queue
						queueName,
						// autoAck :
						// true if the server should consider messages acknowledged once delivered;
						// false if the server should expect explicit acknowledgements
						autoAck,
						// consumerTag :
						// consumerTag a client-generated consumer tag to establish context
						tag,
						// noLocal :
						// true if the server should not deliver to this consumer messages published on
						// this channel's connection.
						// Note! that the RabbitMQ server does not support this flag.
						false,
						// exclusive : true if this is an exclusive consumer.
						exclusive,
						// arguments : a set of arguments for the consume.
						args,
						// deliverCallback : callback when a message is delivered.
						(consumerTag, delivery) -> {
							// 消息处理开始
							Envelope envelope = delivery.getEnvelope();
							BasicProperties properties = delivery.getProperties();
							byte[] body = delivery.getBody();
							try {
								log.debug("Message [{}] handle start", envelope.getDeliveryTag());
								log.debug(
										"Callback handleDelivery, consumerTag==[{}], deliveryTag==[{}], body.length==[{}]",
										consumerTag, envelope.getDeliveryTag(), body.length);
								T t = null;
								try {
									t = deserializer.apply(body);
								} catch (Exception e) {
									throw new DecodeException(e);
								}
								selfAckConsumer.handleMessage(
										// 传入ACK委托者
										ackDelegate,
										// 传入消费者标识
										consumerTag,
										// 包装Message对象
										new Message<>(envelope, properties, t));
							} catch (Exception e) {
								log.error("SelfAckConsumer accept msg==[{}] throw Exception -> {}", bytesToStr(body),
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
							// 消息处理结束
							log.debug("Message [{}] handle end", envelope.getDeliveryTag());
						},
						// cancelCallback : callback when the consumer is cancelled.
						defCancelCallback,
						// shutdownSignalCallback : callback when the channel/connection is shut down.
						defShutdownSignalCallback);
			} catch (IOException e) {
				log.error("Function basicConsume() with SelfAckConsumer throw IOException message -> {}",
						e.getMessage(), e);
				throw new ReceiverStartException(e.getMessage(), e);
			}
			return;
			// # Set SelfAckConsumer end *****
		}
		// 如果[consumer]不为null, 设置consumer
		if (consumer != null) {
			// # Set Consume start *****
			try {
				channel.basicConsume(
						// queue : the name of the queue
						queueName,
						// autoAck :
						// true if the server should consider messages acknowledged once delivered;
						// false if the server should expect explicit acknowledgements
						autoAck,
						// consumerTag :
						// consumerTag a client-generated consumer tag to establish context
						tag,
						// noLocal :
						// true if the server should not deliver to this consumer messages published on
						// this channel's connection.
						// Note! that the RabbitMQ server does not support this flag.
						false,
						// exclusive : true if this is an exclusive consumer.
						exclusive,
						// arguments : a set of arguments for the consume.
						args,
						// deliverCallback : callback when a message is delivered.
						(consumerTag, delivery) -> {
							// 消息处理开始
							Envelope envelope = delivery.getEnvelope();
							byte[] body = delivery.getBody();
							try {
								log.debug("Message [{}] handle start", envelope.getDeliveryTag());
								log.debug(
										"Callback handleDelivery, consumerTag==[{}], deliveryTag==[{}], body.length==[{}]",
										consumerTag, envelope.getDeliveryTag(), body.length);
								T t = null;
								try {
									t = deserializer.apply(body);
								} catch (Exception e) {
									throw new DecodeException(e);
								}
								consumer.accept(t);
							} catch (Exception e) {
								log.error("Consumer accept msg==[{}] throw Exception -> {}", bytesToStr(body),
										e.getMessage(), e);
								dumpUnprocessableMsg(e, consumerTag, envelope, delivery.getProperties(), body);
							}
							if (!autoAck) {
								if (ack(envelope.getDeliveryTag())) {
									log.debug("Message handle and ack finished");
								} else {
									log.info("Ack failure envelope.getDeliveryTag()==[{}], Reject message");
									channel.basicReject(envelope.getDeliveryTag(), true);
								}
							}
							log.debug("Message [{}] handle end", envelope.getDeliveryTag());
							// 消息处理结束
						},
						// cancelCallback : callback when the consumer is cancelled.
						defCancelCallback,
						// shutdownSignalCallback : callback when the channel/connection is shut down.
						defShutdownSignalCallback);
			} catch (IOException e) {
				log.error("Function basicConsume() with Consumer throw IOException message -> {}", e.getMessage(), e);
				throw new ReceiverStartException(e.getMessage(), e);
			}
			// # Set Consume end *****
			return;
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
			channel.basicPublish(errMsgExchangeName, errMsgRoutingKey, properties, body);
			log.error("Exception handling -> Sent to ErrMsgExchange [{}] finished", errMsgExchangeName);
		} else if (hasErrMsgQueue) {
			// Sent message to error dump queue.
			log.error("Exception handling -> Sent to ErrMsgQueue [{}]", errMsgQueueName);
			channel.basicPublish("", errMsgQueueName, properties, body);
			log.error("Exception handling -> Sent to ErrMsgQueue finished");
		} else {
			// Reject message and close connection.
			log.error("Exception handling -> Reject Msg [{}]", bytesToStr(body));
			channel.basicReject(envelope.getDeliveryTag(), true);
			log.error("Exception handling -> Reject Msg finished");
			destroy();
			log.error("RabbitMqReceiver: [{}] already closed", receiverName);
			throw new MsgHandleException(
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

	/**
	 * 
	 * @author yellow013
	 *
	 * @param <T>
	 */
	public static class AckDelegate {

		private AdvancedRabbitMqReceiver<?> receiver;

		private AckDelegate(AdvancedRabbitMqReceiver<?> receiver) {
			this.receiver = receiver;
		}

		public boolean ack(long deliveryTag) {
			return receiver.ack(deliveryTag);
		}

	}

	@FunctionalInterface
	public static interface SelfAckConsumer<T> {

		void handleMessage(final AckDelegate ackDelegate, final String consumerTag, Message<T> msg);

	}

	public static class Message<T> {

		private Envelope envelope;
		private BasicProperties properties;
		private T body;

		private Message(Envelope envelope, BasicProperties properties, T body) {
			this.envelope = envelope;
			this.properties = properties;
			this.body = body;
		}

		public Envelope getEnvelope() {
			return envelope;
		}

		public BasicProperties getProperties() {
			return properties;
		}

		public T getBody() {
			return body;
		}

	}

	public static void main(String[] args) {
//		AdvancedRabbitMqReceiver<byte[]> receiver = new AdvancedRabbitMqReceiver("test",
//				RmqReceiverConfigurator
//						.configuration(RmqConnection.configuration("127.0.0.1", 5672, "user", "u_pass").build(),
//								QueueRelationship.named("TEST"))
//						.build(),
//				msg -> System.out.println(new String(msg, Charsets.UTF8)));
//		receiver.receive();
	}

}

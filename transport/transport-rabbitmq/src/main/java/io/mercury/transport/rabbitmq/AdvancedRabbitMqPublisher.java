package io.mercury.transport.rabbitmq;

import static io.mercury.common.util.StringUtil.nonEmpty;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.ConfirmCallback;

import io.mercury.common.character.Charsets;
import io.mercury.common.datetime.TimeZone;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.Threads;
import io.mercury.common.util.Assertor;
import io.mercury.transport.core.api.Publisher;
import io.mercury.transport.core.api.Sender;
import io.mercury.transport.core.exception.InitializeFailureException;
import io.mercury.transport.core.exception.PublishFailedException;
import io.mercury.transport.rabbitmq.configurator.RmqConnection;
import io.mercury.transport.rabbitmq.configurator.RmqPublisherConfigurator;
import io.mercury.transport.rabbitmq.declare.ExchangeRelationship;
import io.mercury.transport.rabbitmq.exception.DeclareException;
import io.mercury.transport.rabbitmq.exception.DeclareRuntimeException;
import io.mercury.transport.rabbitmq.exception.MsgConfirmFailureException;
import io.mercury.transport.rabbitmq.exception.NoAckException;

/**
 * 
 * @author yellow013
 * 
 *         添加消息序列化器
 *
 */
@ThreadSafe
public class AdvancedRabbitMqPublisher<T> extends AbstractRabbitMqTransport implements Publisher<T>, Sender<T> {

	private static final Logger log = CommonLoggerFactory.getLogger(AdvancedRabbitMqPublisher.class);

	/*
	 * 发布消息使用的[ExchangeDeclare]
	 */
	private final ExchangeRelationship publishExchange;
	/*
	 * 发布消息使用的[Exchange]
	 */
	private final String exchangeName;

	/*
	 * 发布消息使用的默认[RoutingKey]
	 */
	private final String defaultRoutingKey;

	/*
	 * 发布消息使用的默认[MessageProperties]
	 */
	private final BasicProperties defaultMsgProps;

	/*
	 * [MessageProperties]的提供者
	 */
	private final Supplier<BasicProperties> msgPropsSupplier;

	/*
	 * 是否有[MessageProperties]的提供者
	 */
	private final boolean hasPropsSupplier;

	/*
	 * 是否执行发布确认
	 */
	private final boolean confirm;

	/*
	 * 发布确认超时毫秒数
	 */
	private final long confirmTimeout;

	/*
	 * 发布确认重试次数
	 */
	private final int confirmRetry;

	/*
	 * 发布者名称
	 */
	private final String publisherName;

	/**
	 * 接收消息使用的反序列化器
	 */
	private final Function<T, byte[]> serializer;

	/**
	 * 是否存在ACK成功回调
	 */
	private final boolean hasAckCallback;

	/**
	 * 是否存在ACK未成功回调
	 */
	private final boolean hasNoAckCallback;

	/**
	 * 
	 * @param configurator
	 * @return
	 */
	public final static AdvancedRabbitMqPublisher<byte[]> createWithBytes(
			@Nonnull RmqPublisherConfigurator configurator) {
		return createWithBytes(null, configurator, null, null);
	}

	/**
	 * 
	 * @param tag
	 * @param configurator
	 * @return
	 */
	public final static AdvancedRabbitMqPublisher<byte[]> createWithBytes(String tag,
			@Nonnull RmqPublisherConfigurator configurator) {
		return createWithBytes(tag, configurator, null, null);
	}

	/**
	 * 
	 * @param tag
	 * @param configurator
	 * @param ackCallback
	 * @param noAckCallback
	 * @return
	 */
	public final static AdvancedRabbitMqPublisher<byte[]> createWithBytes(String tag,
			@Nonnull RmqPublisherConfigurator configurator, @Nonnull AckCallback ackCallback,
			@Nonnull NoAckCallback noAckCallback) {
		return new AdvancedRabbitMqPublisher<>(tag, configurator, msg -> msg, ackCallback, noAckCallback);
	}

	/**
	 * 
	 * @param configurator
	 * @return
	 */
	public final static AdvancedRabbitMqPublisher<String> createWithString(
			@Nonnull RmqPublisherConfigurator configurator) {
		return createWithString(null, configurator, Charsets.UTF8, null, null);
	}

	/**
	 * 
	 * @param configurator
	 * @param charset
	 * @return
	 */
	public final static AdvancedRabbitMqPublisher<String> createWithString(
			@Nonnull RmqPublisherConfigurator configurator, @Nonnull Charset charset) {
		return createWithString(null, configurator, charset, null, null);
	}

	/**
	 * 
	 * @param tag
	 * @param configurator
	 * @return
	 */
	public final static AdvancedRabbitMqPublisher<String> createWithString(String tag,
			@Nonnull RmqPublisherConfigurator configurator) {
		return createWithString(tag, configurator, Charsets.UTF8, null, null);
	}

	/**
	 * 
	 * @param tag
	 * @param configurator
	 * @param charset
	 * @return
	 */
	public final static AdvancedRabbitMqPublisher<String> createWithString(String tag,
			@Nonnull RmqPublisherConfigurator configurator, @Nonnull Charset charset) {
		return createWithString(tag, configurator, charset, null, null);
	}

	/**
	 * 
	 * @param tag
	 * @param configurator
	 * @param charset
	 * @param ackCallback
	 * @param noAckCallback
	 * @return
	 */
	public final static AdvancedRabbitMqPublisher<String> createWithString(String tag,
			@Nonnull RmqPublisherConfigurator configurator, @Nonnull Charset charset, @Nonnull AckCallback ackCallback,
			@Nonnull NoAckCallback noAckCallback) {
		return new AdvancedRabbitMqPublisher<>(tag, configurator, msg -> msg.getBytes(charset), ackCallback,
				noAckCallback);
	}

	/**
	 * 
	 * @param <T>
	 * @param configurator
	 * @param serializer
	 * @return
	 */
	public final static <T> AdvancedRabbitMqPublisher<T> create(@Nonnull RmqPublisherConfigurator configurator,
			@Nonnull Function<T, byte[]> serializer) {
		return new AdvancedRabbitMqPublisher<>(null, configurator, serializer, null, null);
	}

	/**
	 * 
	 * @param <T>
	 * @param tag
	 * @param configurator
	 * @param serializer
	 * @return
	 */
	public final static <T> AdvancedRabbitMqPublisher<T> create(String tag,
			@Nonnull RmqPublisherConfigurator configurator, @Nonnull Function<T, byte[]> serializer) {
		return new AdvancedRabbitMqPublisher<>(tag, configurator, serializer, null, null);
	}

	/**
	 * 
	 * @param <T>
	 * @param tag
	 * @param configurator
	 * @param serializer
	 * @param ackCallback
	 * @param noAckCallback
	 * @return
	 */
	public final static <T> AdvancedRabbitMqPublisher<T> create(String tag,
			@Nonnull RmqPublisherConfigurator configurator, @Nonnull Function<T, byte[]> serializer,
			@Nonnull AckCallback ackCallback, @Nonnull NoAckCallback noAckCallback) {
		return new AdvancedRabbitMqPublisher<>(tag, configurator, serializer, ackCallback, noAckCallback);
	}

	/**
	 * 
	 * @param tag           标签
	 * @param configurator  配置器
	 * @param serializer    序列化器
	 * @param ackCallback   ACK成功回调
	 * @param noAckCallback ACK未成功回调
	 */
	private AdvancedRabbitMqPublisher(String tag, @Nonnull RmqPublisherConfigurator configurator,
			@Nonnull Function<T, byte[]> serializer, AckCallback ackCallback, NoAckCallback noAckCallback) {
		super(nonEmpty(tag) ? tag : "Publisher-" + ZonedDateTime.now(TimeZone.SYS_DEFAULT), configurator.connection());
		Assertor.nonNull(configurator.publishExchange(), "exchangeRelation");
		this.publishExchange = configurator.publishExchange();
		this.exchangeName = publishExchange.exchangeName();
		this.defaultRoutingKey = configurator.defaultRoutingKey();
		this.defaultMsgProps = configurator.defaultMsgProps();
		this.msgPropsSupplier = configurator.msgPropsSupplier();
		this.confirm = configurator.confirm();
		this.confirmTimeout = configurator.confirmTimeout();
		this.confirmRetry = configurator.confirmRetry();
		this.serializer = serializer;
		this.hasAckCallback = ackCallback != null;
		this.hasNoAckCallback = noAckCallback != null;
		this.hasPropsSupplier = msgPropsSupplier != null;
		this.publisherName = "publisher::[" + rmqConnection.connectionInfo() + "$" + exchangeName + "]";
		createConnection();
		declare();
		/*
		 * 如果设置为需要应答确认, 则进行相关设置
		 */
		if (confirm) {
			/*
			 * 添加Ack & NoAck回调
			 */
			channel.addConfirmListener(
					/*
					 * Ack Callback
					 */
					(deliveryTag, multiple) -> {
						if (hasAckCallback) {
							ackCallback.handle(deliveryTag, multiple);
						} else {
							log.warn("Undefined AckCallback function. Publisher -> {}", publisherName);
						}
					},
					/*
					 * NoAck Callback
					 */
					(deliveryTag, multiple) -> {
						if (hasNoAckCallback) {
							ackCallback.handle(deliveryTag, multiple);
						} else {
							log.warn("Undefined NoAckCallback function. Publisher -> {}", publisherName);
						}
					});
			try {
				/*
				 * Enables publisher acknowledgements on this channel.
				 */
				channel.confirmSelect();
			} catch (IOException ioe) {
				log.error("Enables publisher acknowledgements failure, publisherName==[{}]", publisherName, ioe);
				throw new InitializeFailureException(
						"Enables publisher acknowledgements failure. From publisher -> " + publisherName, ioe);
			}
		}
	}

	private void declare() throws DeclareRuntimeException {
		try {
			if (publishExchange == ExchangeRelationship.Anonymous) {
				log.warn("Publisher -> {} use anonymous exchange, Please specify [queue name] "
						+ "as the [routing key] when publish", tag);
			} else {
				this.publishExchange.declare(RabbitMqDeclareOperator.newWith(channel));
			}
		} catch (DeclareException e) {
			// 在定义Exchange和进行绑定时抛出任何异常都需要终止程序
			log.error("Exchange declare throw exception -> connection configurator info : {}, " + "error message : {}",
					rmqConnection.fullInfo(), e.getMessage(), e);
			destroy();
			throw new DeclareRuntimeException(e);
		}

	}

	@Override
	public void send(@Nonnull T msg) throws PublishFailedException {
		publish(msg);
	}

	@Override
	public void publish(@Nonnull T msg) throws PublishFailedException {
		publish(defaultRoutingKey, msg, defaultMsgProps);
	}

	@Override
	public void publish(@Nonnull String target, @Nonnull T msg) throws PublishFailedException {
		publish(target, msg, hasPropsSupplier ? msgPropsSupplier.get() : defaultMsgProps);
	}

	/**
	 * 
	 * @param target
	 * @param msg
	 * @param props
	 * @throws PublishFailedException
	 */
	public void publish(@Nonnull String target, @Nonnull T msg, @Nonnull BasicProperties props)
			throws PublishFailedException {
		// 记录重试次数
		int retry = 0;
		// 调用isConnected(), 检查channel和connection是否打开, 如果没有打开, 先销毁连接, 再重新创建连接.
		while (!isConnected()) {
			log.error("Detect connection isConnected() == false, retry {}", (++retry));
			destroy();
			Threads.sleep(rmqConnection.recoveryInterval());
			createConnection();
		}
		if (confirm) {
			try {
				confirmPublish(target, msg, props);
			} catch (IOException e) {
				log.error("Function publish throw IOException -> {}, isConfirm==[true], msg==[{}]", e.getMessage(), msg,
						e);
				destroy();
				throw new PublishFailedException(e);
			} catch (MsgConfirmFailureException e) {
				log.error("Function publish throw NoConfirmException -> {}, isConfirm==[true], msg==[{}]",
						e.getMessage(), msg, e);
				throw new PublishFailedException(e);
			}
		} else {
			try {
				basicPublish(target, msg, props);
			} catch (IOException e) {
				log.error("Function publish throw IOException -> {}, isConfirm==[false], msg==[{}]", e.getMessage(),
						msg, e);
				destroy();
				throw new PublishFailedException(e);
			}
		}
	}

	/**
	 * 
	 * @param routingKey
	 * @param msg
	 * @param props
	 * @throws IOException
	 * @throws NoAckException
	 */
	private void confirmPublish(String routingKey, T msg, BasicProperties props)
			throws IOException, MsgConfirmFailureException {
		confirmPublish0(routingKey, msg, props, 0);
	}

	/**
	 * TODO 优化异常处理逻辑
	 * 
	 * @param routingKey
	 * @param msg
	 * @param props
	 * @param retry
	 * @throws IOException
	 * @throws NoAckException
	 */
	private void confirmPublish0(String routingKey, T msg, BasicProperties props, int retry)
			throws IOException, MsgConfirmFailureException {
		try {
			basicPublish(routingKey, msg, props);
			// 启用发布确认
			if (channel.waitForConfirms(confirmTimeout))
				return;
			log.error("Call method channel.waitForConfirms(confirmTimeout==[{}]) retry==[{}]", confirmTimeout, retry);
			if (++retry == confirmRetry)
				throw new MsgConfirmFailureException(exchangeName, routingKey, retry, confirmTimeout);
			confirmPublish0(routingKey, msg, props, retry);
		} catch (IOException e) {
			log.error("Function basicPublish() throw IOException from publisherName -> {}, routingKey -> {}",
					publisherName, routingKey, e);
			throw e;
		} catch (InterruptedException e) {
			log.error("Function channel.waitForConfirms() throw InterruptedException from publisherName -> {}, "
					+ "routingKey -> {}", publisherName, routingKey, e);
		} catch (TimeoutException e) {
			log.error("Function channel.waitForConfirms() throw TimeoutException from publisherName -> {}, "
					+ "routingKey -> {}", publisherName, routingKey, e);
		}
	}

	/**
	 * 
	 * @param routingKey
	 * @param msg
	 * @param props
	 * @throws IOException
	 */
	private void basicPublish(String routingKey, T msg, BasicProperties props) throws IOException {
		try {
			byte[] bytes = serializer.apply(msg);
			if (bytes != null) {
				channel.basicPublish(
						// param1: the exchange to publish the message to
						exchangeName,
						// param2: the routing key
						routingKey,
						// param3: other properties for the message - routing headers etc
						props,
						// param4: the message body
						bytes);
			}
		} catch (IOException ioe) {
			StringBuilder sb = new StringBuilder(500);
			props.appendPropertyDebugStringTo(sb);
			log.error(
					"Function channel.basicPublish() throw IOException, exchange==[{}], routingKey==[{}], properties==[{}] -> {}",
					exchangeName, routingKey, sb.toString(), ioe.getMessage(), ioe);
			throw ioe;
		}
	}

	@Override
	public boolean destroy() {
		log.info("Call method destroy() from Publisher name==[{}]", publisherName);
		return super.destroy();
	}

	@Override
	public String name() {
		return publisherName;
	}

	// TODO 重试计数器
	public class ResendCounter {

	}

	@FunctionalInterface
	public static interface AckCallback extends ConfirmCallback {

	}

	@FunctionalInterface
	public static interface NoAckCallback extends ConfirmCallback {

	}

	public static void main(String[] args) {

		RmqConnection connection = RmqConnection.configuration("127.0.0.1", 5672, "guest", "guest").build();

		ExchangeRelationship fanoutExchange = ExchangeRelationship.fanout("fanout-test");

		try (AdvancedRabbitMqPublisher<String> publisher = AdvancedRabbitMqPublisher
				.createWithString(RmqPublisherConfigurator.configuration(connection, fanoutExchange).build())) {
			Threads.startNewThread(() -> {
				int count = 0;
				while (true) {
					Threads.sleep(5000);
					publisher.publish(String.valueOf(++count));
					System.out.println(count);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

package io.mercury.transport.rabbitmq;

import static io.mercury.common.datetime.DateTimeUtil.datetimeOfMillisecond;
import static io.mercury.common.util.StringSupport.nonEmpty;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.ConfirmCallback;

import io.mercury.common.character.Charsets;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.serialization.BytesSerializer;
import io.mercury.common.thread.SleepSupport;
import io.mercury.common.thread.Threads;
import io.mercury.common.util.Assertor;
import io.mercury.transport.api.Publisher;
import io.mercury.transport.api.Sender;
import io.mercury.transport.exception.InitializeFailureException;
import io.mercury.transport.exception.PublishFailedException;
import io.mercury.transport.rabbitmq.configurator.RabbitConnection;
import io.mercury.transport.rabbitmq.configurator.RabbitPublisherCfg;
import io.mercury.transport.rabbitmq.declare.ExchangeDef;
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
public class AdvancedRabbitMqPublisher<T> extends RabbitMqTransport implements Publisher<String, T>, Sender<T> {

	private static final Logger log = CommonLoggerFactory.getLogger(AdvancedRabbitMqPublisher.class);

	// 发布消息使用的[ExchangeDeclare]
	private final ExchangeDef publishExchange;

	// 发布消息使用的[Exchange]
	private final String exchangeName;

	// 发布消息使用的默认[RoutingKey]
	private final String defaultRoutingKey;

	// 发布消息使用的默认[MessageProperties]
	private final BasicProperties defaultMsgProps;

	// [MessageProperties]的提供者
	private final Supplier<BasicProperties> msgPropsSupplier;

	// 是否有[MessageProperties]的提供者
	private final boolean hasPropsSupplier;

	// 是否执行发布确认
	private final boolean confirm;

	// 发布确认超时毫秒数
	private final long confirmTimeout;

	// 发布确认重试次数
	private final int confirmRetry;

	// 发布者名称
	private final String publisherName;

	// 发布消息使用的序列化器
	private final BytesSerializer<T> serializer;

	/**
	 * 
	 * @param <T>
	 * @param cfg
	 * @param serializer
	 * @return
	 */
	public final static <T> AdvancedRabbitMqPublisher<T> create(@Nonnull RabbitPublisherCfg cfg,
			@Nonnull BytesSerializer<T> serializer) {
		return new AdvancedRabbitMqPublisher<>(null, cfg, serializer, null, null);
	}

	/**
	 * 
	 * @param <T>
	 * @param tag
	 * @param cfg
	 * @param serializer
	 * @return
	 */
	public final static <T> AdvancedRabbitMqPublisher<T> create(String tag, @Nonnull RabbitPublisherCfg cfg,
			@Nonnull BytesSerializer<T> serializer) {
		return new AdvancedRabbitMqPublisher<>(tag, cfg, serializer, null, null);
	}

	/**
	 * 
	 * @param <T>
	 * @param tag
	 * @param cfg
	 * @param serializer
	 * @param ackCallback
	 * @param noAckCallback
	 * @return
	 */
	public final static <T> AdvancedRabbitMqPublisher<T> create(String tag, @Nonnull RabbitPublisherCfg cfg,
			@Nonnull BytesSerializer<T> serializer, @Nonnull AckCallback ackCallback,
			@Nonnull NoAckCallback noAckCallback) {
		return new AdvancedRabbitMqPublisher<>(tag, cfg, serializer, ackCallback, noAckCallback);
	}

	/**
	 * 
	 * @param cfg
	 * @return
	 */
	public final static AdvancedRabbitMqPublisher<byte[]> createWithBytes(@Nonnull RabbitPublisherCfg cfg) {
		return createWithBytes(null, cfg, null, null);
	}

	/**
	 * 
	 * @param tag
	 * @param cfg
	 * @return
	 */
	public final static AdvancedRabbitMqPublisher<byte[]> createWithBytes(String tag, @Nonnull RabbitPublisherCfg cfg) {
		return createWithBytes(tag, cfg, null, null);
	}

	/**
	 * 
	 * @param tag
	 * @param cfg
	 * @param ackCallback
	 * @param noAckCallback
	 * @return
	 */
	public final static AdvancedRabbitMqPublisher<byte[]> createWithBytes(String tag, @Nonnull RabbitPublisherCfg cfg,
			@Nonnull AckCallback ackCallback, @Nonnull NoAckCallback noAckCallback) {
		return new AdvancedRabbitMqPublisher<>(tag, cfg, msg -> msg, ackCallback, noAckCallback);
	}

	/**
	 * 
	 * @param cfg
	 * @return
	 */
	public final static AdvancedRabbitMqPublisher<String> createWithString(@Nonnull RabbitPublisherCfg cfg) {
		return createWithString(null, cfg, Charsets.UTF8, null, null);
	}

	/**
	 * 
	 * @param cfg
	 * @param charset
	 * @return
	 */
	public final static AdvancedRabbitMqPublisher<String> createWithString(@Nonnull RabbitPublisherCfg cfg,
			@Nonnull Charset charset) {
		return createWithString(null, cfg, charset, null, null);
	}

	/**
	 * 
	 * @param tag
	 * @param cfg
	 * @return
	 */
	public final static AdvancedRabbitMqPublisher<String> createWithString(String tag,
			@Nonnull RabbitPublisherCfg cfg) {
		return createWithString(tag, cfg, Charsets.UTF8, null, null);
	}

	/**
	 * 
	 * @param tag
	 * @param cfg
	 * @param charset
	 * @return
	 */
	public final static AdvancedRabbitMqPublisher<String> createWithString(String tag, @Nonnull RabbitPublisherCfg cfg,
			@Nonnull Charset charset) {
		return createWithString(tag, cfg, charset, null, null);
	}

	/**
	 * 
	 * @param tag
	 * @param cfg
	 * @param charset
	 * @param ackCallback
	 * @param noAckCallback
	 * @return
	 */
	public final static AdvancedRabbitMqPublisher<String> createWithString(@Nullable String tag,
			@Nonnull RabbitPublisherCfg cfg, @Nonnull Charset charset, @Nonnull AckCallback ackCallback,
			@Nonnull NoAckCallback noAckCallback) {
		return new AdvancedRabbitMqPublisher<>(tag, cfg, msg -> msg.getBytes(charset), ackCallback, noAckCallback);
	}

	/**
	 * 
	 * @param tag           标签
	 * @param cfg           配置器
	 * @param serializer    序列化器
	 * @param ackCallback   ACK成功回调
	 * @param noAckCallback ACK未成功回调
	 */
	private AdvancedRabbitMqPublisher(@Nullable String tag, @Nonnull RabbitPublisherCfg cfg,
			@Nonnull BytesSerializer<T> serializer, @Nullable AckCallback ackCallback,
			@Nullable NoAckCallback noAckCallback) {
		super(nonEmpty(tag) ? tag : "adv-publisher-" + datetimeOfMillisecond(), cfg.getConnection());
		Assertor.nonNull(cfg.getPublishExchange(), "exchangeRelation");
		this.publishExchange = cfg.getPublishExchange();
		this.exchangeName = publishExchange.getExchangeName();
		this.defaultRoutingKey = cfg.getDefaultRoutingKey();
		this.defaultMsgProps = cfg.getDefaultMsgProps();
		this.msgPropsSupplier = cfg.getMsgPropsSupplier();
		this.confirm = cfg.getConfirmOptions().isConfirm();
		this.confirmTimeout = cfg.getConfirmOptions().getConfirmTimeout();
		this.confirmRetry = cfg.getConfirmOptions().getConfirmRetry();
		this.serializer = serializer;
		this.hasPropsSupplier = msgPropsSupplier != null;
		this.publisherName = "publisher::[" + rabbitConnection.getConnectionInfo() + "$" + exchangeName + "]";
		createConnection();
		declareExchange();
		// 如果设置为需要应答确认, 则进行相关设置
		if (confirm) {
			// 是否存在ACK成功回调
			final boolean hasAckCallback = ackCallback != null;
			// 是否存在ACK未成功回调
			final boolean hasNoAckCallback = noAckCallback != null;
			// 添加ACK & NoAck回调
			channel.addConfirmListener(
					// ACK Callback
					(deliveryTag, multiple) -> {
						if (hasAckCallback)
							ackCallback.handle(deliveryTag, multiple);
						else
							log.warn("Undefined AckCallback function. Publisher -> {}", publisherName);
					},
					// NoAck Callback
					(deliveryTag, multiple) -> {
						if (hasNoAckCallback)
							ackCallback.handle(deliveryTag, multiple);
						else
							log.warn("Undefined NoAckCallback function. Publisher -> {}", publisherName);
					});
			try {
				// Enables publisher acknowledgements on this channel.
				channel.confirmSelect();
			} catch (IOException ioe) {
				log.error("Enables publisher acknowledgements failure, publisherName -> {}, connectionInfo -> {}",
						publisherName, rabbitConnection.getConnectionInfo(), ioe);
				throw new InitializeFailureException(
						"Enables publisher acknowledgements failure, From publisher -> {}" + publisherName, ioe);
			}
		}
		newStartTime();
	}

	/**
	 * 定义相关队列组件
	 * 
	 * @throws DeclareRuntimeException
	 */
	private void declareExchange() throws DeclareRuntimeException {
		try {
			if (publishExchange == ExchangeDef.Anonymous) {
				log.warn("Publisher -> {} use anonymous exchange, Please specify [queue name] "
						+ "as the [routing key] when publish", tag);
			} else {
				this.publishExchange.declare(RabbitMqOperator.newWith(channel));
			}
		} catch (DeclareException e) {
			// 在定义Exchange和进行绑定时抛出任何异常都需要终止程序
			log.error("Exchange declare throw exception -> connection configurator info : {}, " + "error message : {}",
					rabbitConnection.getCfgInfo(), e.getMessage(), e);
			closeIgnoreException();
			throw new DeclareRuntimeException(e);
		}

	}

	@Override
	public void sent(@Nonnull T msg) throws PublishFailedException {
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
			closeIgnoreException();
			SleepSupport.sleep(rabbitConnection.getRecoveryInterval());
			createConnection();
		}
		if (confirm) {
			try {
				confirmPublish(target, msg, props);
			} catch (IOException e) {
				log.error("Function publish throw IOException -> {}, isConfirm==[true], msg==[{}]", e.getMessage(), msg,
						e);
				closeIgnoreException();
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
				closeIgnoreException();
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
			if (channel.waitForConfirms(confirmTimeout)) {
				return;
			}
			log.error("Call method channel.waitForConfirms(confirmTimeout==[{}]) retry==[{}]", confirmTimeout, retry);
			if (++retry == confirmRetry) {
				throw new MsgConfirmFailureException(exchangeName, routingKey, retry, confirmTimeout);
			}
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
			// TODO 添加序列化异常处理
			byte[] bytes = serializer.serialization(msg);
			if (bytes != null) {
				channel.basicPublish(
						// exchange: the exchange to publish the message to
						exchangeName,
						// routingKey: the routing key
						routingKey,
						// props: other properties for the message - routing headers etc
						props,
						// body: the message body
						bytes);
			}
		} catch (IOException ioe) {
			StringBuilder properties = new StringBuilder(500);
			props.appendPropertyDebugStringTo(properties);
			log.error(
					"Function channel.basicPublish() throw IOException, exchange==[{}], routingKey==[{}], properties==[{}] -> {}",
					exchangeName, routingKey, properties, ioe.getMessage(), ioe);
			throw ioe;
		}
	}

	@Override
	public boolean closeIgnoreException() {
		log.info("Call method destroy() from Publisher name==[{}]", publisherName);
		return super.closeIgnoreException();
	}

	@Override
	public String getName() {
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

		RabbitConnection connection = RabbitConnection.configuration("127.0.0.1", 5672, "guest", "guest").build();

		ExchangeDef fanoutExchange = ExchangeDef.fanout("fanout-test");

		try (AdvancedRabbitMqPublisher<String> publisher = AdvancedRabbitMqPublisher
				.createWithString(RabbitPublisherCfg.configuration(connection, fanoutExchange).build())) {
			Threads.startNewThread(() -> {
				int count = 0;
				while (true) {
					SleepSupport.sleep(5000);
					publisher.publish(String.valueOf(++count));
					System.out.println(count);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

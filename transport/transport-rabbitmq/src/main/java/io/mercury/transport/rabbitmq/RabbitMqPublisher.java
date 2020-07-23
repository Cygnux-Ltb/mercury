package io.mercury.transport.rabbitmq;

import static io.mercury.common.util.StringUtil.bytesToStr;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;

import com.rabbitmq.client.AMQP.BasicProperties;

import io.mercury.common.character.Charsets;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.Threads;
import io.mercury.common.util.Assertor;
import io.mercury.transport.core.api.Publisher;
import io.mercury.transport.core.api.Sender;
import io.mercury.transport.core.exception.PublishFailedException;
import io.mercury.transport.rabbitmq.configurator.RmqConnection;
import io.mercury.transport.rabbitmq.configurator.RmqPublisherConfigurator;
import io.mercury.transport.rabbitmq.declare.ExchangeRelationship;
import io.mercury.transport.rabbitmq.exception.AmqpDeclareException;
import io.mercury.transport.rabbitmq.exception.AmqpDeclareRuntimeException;
import io.mercury.transport.rabbitmq.exception.AmqpNoConfirmException;

@ThreadSafe
public class RabbitMqPublisher extends BaseRabbitMqTransport implements Publisher<byte[]>, Sender<byte[]> {

	private static final Logger log = CommonLoggerFactory.getLogger(RabbitMqPublisher.class);

	// 发布消息使用的[ExchangeDeclare]
	private final ExchangeRelationship publishExchange;
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

	private final boolean confirm;
	private final long confirmTimeout;
	private final int confirmRetry;

	private final String publisherName;

	@SuppressWarnings("unused")
	private Consumer<Long> ackCallback;

	@SuppressWarnings("unused")
	private Consumer<Long> noAckCallback;

	/**
	 * 
	 * @param configurator
	 */
	public RabbitMqPublisher(@Nonnull RmqPublisherConfigurator configurator) {
		this(null, configurator, null, null);
	}

	/**
	 * 
	 * @param tag
	 * @param configurator
	 */
	public RabbitMqPublisher(String tag, @Nonnull RmqPublisherConfigurator configurator) {
		this(tag, configurator, null, null);
	}

	/**
	 * 
	 * @param tag
	 * @param configurator
	 * @param ackCallback
	 * @param noAckCallback
	 */
	public RabbitMqPublisher(String tag, @Nonnull RmqPublisherConfigurator configurator, Consumer<Long> ackCallback,
			Consumer<Long> noAckCallback) {
		super(tag, "Publisher", configurator.connection());
		Assertor.nonNull(configurator.publishExchange(), "exchangeRelation");
		this.publishExchange = configurator.publishExchange();
		this.exchangeName = publishExchange.exchangeName();
		this.defaultRoutingKey = configurator.defaultRoutingKey();
		this.defaultMsgProps = configurator.defaultMsgProps();
		this.msgPropsSupplier = configurator.msgPropsSupplier();
		this.confirm = configurator.confirm();
		this.confirmTimeout = configurator.confirmTimeout();
		this.confirmRetry = configurator.confirmRetry();
		this.ackCallback = ackCallback;
		this.noAckCallback = noAckCallback;
		this.hasPropsSupplier = msgPropsSupplier != null;
		this.publisherName = "publisher::" + rmqConnection.fullInfo() + "$" + exchangeName;
		createConnection();
		declare();
	}

	private void declare() throws AmqpDeclareRuntimeException {
		try {
			if (publishExchange == ExchangeRelationship.Anonymous) {
				log.warn("Publisher -> {} use anonymous exchange, Please specify [queue name] "
						+ "as the [routing key] when publish", tag);
			} else {
				this.publishExchange.declare(RabbitMqDeclarant.newWith(channel));
			}
		} catch (AmqpDeclareException e) {
			// 在定义Exchange和进行绑定时抛出任何异常都需要终止程序
			log.error("Exchange declare throw exception -> connection configurator info : {}, " + "error message : {}",
					rmqConnection.fullInfo(), e.getMessage(), e);
			destroy();
			throw new AmqpDeclareRuntimeException(e);
		}

	}

	@Override
	public void send(@Nonnull byte[] msg) throws PublishFailedException {
		publish(msg);
	}

	@Override
	public void publish(@Nonnull byte[] msg) throws PublishFailedException {
		publish(defaultRoutingKey, msg, defaultMsgProps);
	}

	@Override
	public void publish(@Nonnull String target, @Nonnull byte[] msg) throws PublishFailedException {
		publish(target, msg, hasPropsSupplier ? msgPropsSupplier.get() : defaultMsgProps);
	}

	/**
	 * 
	 * @param target
	 * @param msg
	 * @param props
	 * @throws PublishFailedException
	 */
	public void publish(@Nonnull String target, @Nonnull byte[] msg, @Nonnull BasicProperties props)
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
				log.error("Method publish isConfirm==[true] throw IOException -> {}, msg==[{}]", e.getMessage(),
						bytesToStr(msg), e);
				destroy();
				throw new PublishFailedException(e);
			} catch (AmqpNoConfirmException e) {
				log.error("Method publish isConfirm==[true] throw NoConfirmException -> {}, msg==[{}]", e.getMessage(),
						bytesToStr(msg), e);
				throw new PublishFailedException(e);
			}
		} else {
			try {
				basicPublish(target, msg, props);
			} catch (IOException e) {
				log.error("Method publish isConfirm==[false] throw IOException -> {}, msg==[{}]", e.getMessage(),
						bytesToStr(msg), e);
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
	 * @throws AmqpNoConfirmException
	 */
	private void confirmPublish(String routingKey, byte[] msg, BasicProperties props)
			throws IOException, AmqpNoConfirmException {
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
	 * @throws AmqpNoConfirmException
	 */
	private void confirmPublish0(String routingKey, byte[] msg, BasicProperties props, int retry)
			throws IOException, AmqpNoConfirmException {
		try {
			channel.confirmSelect();
			basicPublish(routingKey, msg, props);
			if (channel.waitForConfirms(confirmTimeout))
				return;
			log.error("Call method channel.waitForConfirms(confirmTimeout==[{}]) retry==[{}]", confirmTimeout, retry);
			if (++retry == confirmRetry)
				throw new AmqpNoConfirmException(exchangeName, routingKey, retry, confirmTimeout);
			confirmPublish0(routingKey, msg, props, retry);
		} catch (IOException e) {
			log.error("Method channel.confirmSelect() throw IOException from publisherName -> {}, routingKey -> {}",
					publisherName, routingKey, e);
			throw e;
		} catch (InterruptedException e) {
			log.error("Method channel.waitForConfirms() throw InterruptedException from publisherName -> {}, "
					+ "routingKey -> {}", publisherName, routingKey, e);
		} catch (TimeoutException e) {
			log.error("Method channel.waitForConfirms() throw TimeoutException from publisherName -> {}, "
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
	private void basicPublish(String routingKey, byte[] msg, BasicProperties props) throws IOException {
		try {
			channel.basicPublish(
					// param1: the exchange to publish the message to
					exchangeName,
					// param2: the routing key
					routingKey,
					// param3: other properties for the message - routing headers etc
					props,
					// param4: the message body
					msg);
		} catch (IOException e) {
			StringBuilder sb = new StringBuilder(240);
			props.appendPropertyDebugStringTo(sb);
			log.error("Method channel.basicPublish(exchange==[{}], routingKey==[{}], properties==[{}], msg==[...]) "
					+ "throw IOException -> {}", exchangeName, routingKey, sb.toString(), e.getMessage(), e);
			throw e;
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

	public static void main(String[] args) {

		RmqConnection connectionConfigurator0 = RmqConnection.configuration("127.0.0.1", 5672, "guest", "guest")
				.build();

		ExchangeRelationship fanoutExchange = ExchangeRelationship.fanout("fanout-test");

		try (RabbitMqPublisher publisher = new RabbitMqPublisher(
				RmqPublisherConfigurator.configuration(connectionConfigurator0, fanoutExchange).build())) {
			Threads.startNewThread(() -> {
				int count = 0;
				while (true) {
					Threads.sleep(5000);
					publisher.publish(String.valueOf(++count).getBytes(Charsets.UTF8));
					System.out.println(count);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

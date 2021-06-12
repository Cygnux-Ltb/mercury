package io.mercury.transport.rabbitmq.configurator;

import static com.rabbitmq.client.MessageProperties.PERSISTENT_BASIC;
import static io.mercury.common.util.Assertor.nonNull;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.rabbitmq.client.AMQP.BasicProperties;

import io.mercury.serialization.json.JsonWrapper;
import io.mercury.transport.rabbitmq.declare.AmqpQueue;
import io.mercury.transport.rabbitmq.declare.ExchangeDefinition;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 
 * @author yellow013
 * 
 */
public final class RabbitPublisherCfg extends RabbitConfigurator {

	// 发布者ExchangeDeclare
	@Getter
	private final ExchangeDefinition publishExchange;

	// 消息发布RoutingKey
	@Getter
	private final String defaultRoutingKey;

	// 消息发布参数
	@Getter
	private final BasicProperties defaultMsgProps;

	// 消息参数提供者
	@Getter
	private final Supplier<BasicProperties> msgPropsSupplier;

	// 发布确认选项
	@Getter
	private final PublishConfirmOptions confirmOptions;

	/**
	 * 
	 * @param builder
	 */
	private RabbitPublisherCfg(Builder builder) {
		super(builder.connection);
		this.publishExchange = builder.publishExchange;
		this.defaultRoutingKey = builder.defaultRoutingKey;
		this.defaultMsgProps = builder.defaultMsgProps;
		this.msgPropsSupplier = builder.msgPropsSupplier;
		this.confirmOptions = builder.confirmOptions;
	}

	/**
	 * Use Anonymous Exchange
	 * 
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 */
	public static Builder configuration(@Nonnull String host, int port, @Nonnull String username,
			@Nonnull String password) {
		return configuration(RabbitConnection.configuration(host, port, username, password).build());
	}

	/**
	 * Use Anonymous Exchange
	 * 
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @param virtualHost
	 * @return
	 */
	public static Builder configuration(@Nonnull String host, int port, @Nonnull String username,
			@Nonnull String password, String virtualHost) {
		return configuration(RabbitConnection.configuration(host, port, username, password, virtualHost).build());
	}

	/**
	 * Use Anonymous Exchange
	 * 
	 * @param connection
	 * @return
	 */
	public static Builder configuration(@Nonnull RabbitConnection connection) {
		return configuration(connection, ExchangeDefinition.Anonymous);
	}

	/**
	 * Use Specified Exchange
	 * 
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @param publishExchange
	 * @return
	 * 
	 */
	public static Builder configuration(@Nonnull String host, int port, @Nonnull String username,
			@Nonnull String password, @Nonnull ExchangeDefinition publishExchange) {
		return configuration(RabbitConnection.configuration(host, port, username, password).build(), publishExchange);
	}

	/**
	 * Use Specified Exchange
	 * 
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @param virtualHost
	 * @param publishExchange
	 * @return
	 */
	public static Builder configuration(@Nonnull String host, int port, @Nonnull String username,
			@Nonnull String password, String virtualHost, @Nonnull ExchangeDefinition publishExchange) {
		return configuration(RabbitConnection.configuration(host, port, username, password, virtualHost).build(),
				publishExchange);
	}

	/**
	 * Use Specified Exchange
	 * 
	 * @param connection
	 * @param exchangeRelation
	 * @return
	 */
	public static Builder configuration(@Nonnull RabbitConnection connection,
			@Nonnull ExchangeDefinition publishExchange) {
		return new Builder(nonNull(connection, "connection"), nonNull(publishExchange, "publishExchange"));
	}

	private transient String toStringCache;

	@Override
	public String toString() {
		if (toStringCache == null)
			toStringCache = JsonWrapper.toJson(this);
		return toStringCache;
	}

	@Accessors(chain = true)
	public static class Builder {

		// 连接配置
		private final RabbitConnection connection;
		// 消息发布Exchange和相关绑定
		private final ExchangeDefinition publishExchange;

		// 消息发布RoutingKey, 默认为空字符串
		@Setter
		private String defaultRoutingKey = "";

		// 默认消息发布参数
		@Setter
		private BasicProperties defaultMsgProps = PERSISTENT_BASIC;

		// 默认消息发布参数提供者
		@Setter
		private Supplier<BasicProperties> msgPropsSupplier = null;

		// 发布确认选项
		@Setter
		private PublishConfirmOptions confirmOptions = PublishConfirmOptions.defaultOption();

		/**
		 * 
		 * @param connection
		 * @param publishExchange
		 */
		private Builder(RabbitConnection connection, ExchangeDefinition publishExchange) {
			this.connection = connection;
			this.publishExchange = publishExchange;
		}

		/**
		 * 
		 * @param confirm
		 * @return
		 */
		public Builder setConfirm(boolean confirm) {
			this.confirmOptions.setConfirm(confirm);
			return this;
		}

		/**
		 * 
		 * @param confirmRetry
		 * @return
		 */
		public Builder setConfirmRetry(int confirmRetry) {
			this.confirmOptions.setConfirmRetry(confirmRetry);
			return this;
		}

		/**
		 * 
		 * @param confirmTimeout
		 * @return
		 */
		public Builder setConfirmTimeout(long confirmTimeout) {
			this.confirmOptions.setConfirmTimeout(confirmTimeout);
			return this;
		}

		/**
		 * 
		 * @return
		 */
		public RabbitPublisherCfg build() {
			return new RabbitPublisherCfg(this);
		}

	}

	@Accessors(chain = true)
	public static final class PublishConfirmOptions {

		// 是否执行发布确认, 默认false
		@Getter
		@Setter
		private boolean confirm = false;

		// 发布确认超时毫秒数, 默认5000毫秒
		@Getter
		@Setter
		private long confirmTimeout = 5000;

		// 发布确认重试次数, 默认3次
		@Getter
		@Setter
		private int confirmRetry = 3;

		/**
		 * 使用默认参数
		 * 
		 * @return
		 */
		public static final PublishConfirmOptions defaultOption() {
			return new PublishConfirmOptions();
		}

		/**
		 * 指定具体参数
		 * 
		 * @return
		 */
		public static final PublishConfirmOptions withOption(boolean confirm, long confirmTimeout, int confirmRetry) {
			return new PublishConfirmOptions(confirm, confirmTimeout, confirmRetry);
		}

		private PublishConfirmOptions() {
		}

		private PublishConfirmOptions(boolean confirm, long confirmTimeout, int confirmRetry) {
			this.confirm = confirm;
			this.confirmTimeout = confirmTimeout;
			this.confirmRetry = confirmRetry;
		}

	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(configuration(RabbitConnection.configuration("localhost", 5672, "user0", "userpass").build(),
				ExchangeDefinition.direct("TEST").bindingQueue(AmqpQueue.named("TEST_0"))).build());
	}

}

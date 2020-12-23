package io.mercury.transport.rabbitmq.configurator;

import static com.rabbitmq.client.MessageProperties.PERSISTENT_BASIC;
import static io.mercury.common.util.Assertor.nonNull;
import static io.mercury.transport.rabbitmq.configurator.PublishConfirmOptions.defaultOption;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.rabbitmq.client.AMQP.BasicProperties;

import io.mercury.common.util.StringUtil;
import io.mercury.transport.rabbitmq.declare.AmqpQueue;
import io.mercury.transport.rabbitmq.declare.ExchangeRelationship;

/**
 * 
 * @author yellow013
 * 
 */
public final class RmqPublisherConfigurator extends RmqConfigurator {

	// 发布者ExchangeDeclare
	private ExchangeRelationship publishExchange;

	// 消息发布RoutingKey
	private String defaultRoutingKey;

	// 消息发布参数
	private BasicProperties defaultMsgProps;

	// 消息参数提供者
	private Supplier<BasicProperties> msgPropsSupplier;

	// 发布确认选项
	private PublishConfirmOptions confirmOptions;

	/**
	 * 
	 * @param builder
	 */
	private RmqPublisherConfigurator(Builder builder) {
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
		return configuration(RmqConnection.configuration(host, port, username, password).build());
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
		return configuration(RmqConnection.configuration(host, port, username, password, virtualHost).build());
	}

	/**
	 * Use Anonymous Exchange
	 * 
	 * @param connection
	 * @return
	 */
	public static Builder configuration(@Nonnull RmqConnection connection) {
		return configuration(connection, ExchangeRelationship.Anonymous);
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
			@Nonnull String password, @Nonnull ExchangeRelationship publishExchange) {
		return configuration(RmqConnection.configuration(host, port, username, password).build(), publishExchange);
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
			@Nonnull String password, String virtualHost, @Nonnull ExchangeRelationship publishExchange) {
		return configuration(RmqConnection.configuration(host, port, username, password, virtualHost).build(),
				publishExchange);
	}

	/**
	 * Use Specified Exchange
	 * 
	 * @param connection
	 * @param exchangeRelation
	 * @return
	 */
	public static Builder configuration(@Nonnull RmqConnection connection,
			@Nonnull ExchangeRelationship publishExchange) {
		return new Builder(nonNull(connection, "connection"), nonNull(publishExchange, "publishExchange"));
	}

	/**
	 * @return the exchangeDeclare
	 */
	public ExchangeRelationship publishExchange() {
		return publishExchange;
	}

	/**
	 * @return the defaultRoutingKey
	 */
	public String defaultRoutingKey() {
		return defaultRoutingKey;
	}

	/**
	 * @return the msgProperties
	 */
	public BasicProperties defaultMsgProps() {
		return defaultMsgProps;
	}

	/**
	 * 
	 * @return
	 */
	public Supplier<BasicProperties> msgPropsSupplier() {
		return msgPropsSupplier;
	}

	/**
	 * 
	 * @return the confirmOptions
	 */
	public PublishConfirmOptions confirmOptions() {
		return confirmOptions;
	}

	/**
	 * @return the isConfirm
	 */
	public boolean confirm() {
		return confirmOptions.isConfirm();
	}

	/**
	 * @return the confirmTimeout
	 */
	public long confirmTimeout() {
		return confirmOptions.getConfirmTimeout();
	}

	/**
	 * @return the confirmRetry
	 */
	public int confirmRetry() {
		return confirmOptions.getConfirmRetry();
	}

	private transient String toStringCache;

	@Override
	public String toString() {
		if (toStringCache == null)
			toStringCache = StringUtil.toStringWithReflection(this);
		return toStringCache;
	}

	public static class Builder {

		// 连接配置
		private RmqConnection connection;
		// 消息发布Exchange和相关绑定
		private ExchangeRelationship publishExchange;
		// 消息发布RoutingKey, 默认为空字符串
		private String defaultRoutingKey = "";
		// 默认消息发布参数
		private BasicProperties defaultMsgProps = PERSISTENT_BASIC;
		// 默认消息发布参数提供者
		private Supplier<BasicProperties> msgPropsSupplier = null;
		// 发布确认选项
		private PublishConfirmOptions confirmOptions = defaultOption();

		/**
		 * 
		 * @param connection
		 */
		private Builder(RmqConnection connection) {
			this.connection = connection;
		}

		/**
		 * 
		 * @param connection
		 * @param publishExchange
		 */
		private Builder(RmqConnection connection, ExchangeRelationship publishExchange) {
			this.connection = connection;
			this.publishExchange = publishExchange;
		}

		/**
		 * 
		 * @return
		 */
		public RmqPublisherConfigurator build() {
			return new RmqPublisherConfigurator(this);
		}

		/**
		 * @param defaultRoutingKey the defaultRoutingKey to set
		 */
		public Builder setDefaultRoutingKey(@Nonnull String defaultRoutingKey) {
			this.defaultRoutingKey = defaultRoutingKey;
			return this;
		}

		/**
		 * @param msgProperties the msgProperties to set
		 */
		public Builder setDefaultMsgProps(@Nonnull BasicProperties defaultMsgProps) {
			this.defaultMsgProps = defaultMsgProps;
			return this;
		}

		/**
		 * @param msgPropertiesSupplier
		 */
		public Builder setMsgPropsSupplier(@Nonnull Supplier<BasicProperties> msgPropsSupplier) {
			this.msgPropsSupplier = msgPropsSupplier;
			return this;
		}

		/**
		 * @param isConfirm the isConfirm to set
		 */
		public Builder setConfirm(boolean confirm) {
			this.confirmOptions.setConfirm(confirm);
			return this;
		}

		/**
		 * @param confirmTimeout the confirmTimeout to set
		 */
		public Builder setConfirmTimeout(long confirmTimeout) {
			this.confirmOptions.setConfirmTimeout(confirmTimeout);
			return this;
		}

		/**
		 * @param confirmRetry the confirmRetry to set
		 */
		public Builder setConfirmRetry(int confirmRetry) {
			this.confirmOptions.setConfirmRetry(confirmRetry);
			return this;
		}

	}

	public static void main(String[] args) {
		System.out.println(configuration(RmqConnection.configuration("localhost", 5672, "user0", "userpass").build(),
				ExchangeRelationship.direct("TEST").bindingQueue(AmqpQueue.named("TEST_0"))).build());
	}

}

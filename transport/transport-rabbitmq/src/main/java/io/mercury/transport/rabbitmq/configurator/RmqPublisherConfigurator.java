package io.mercury.transport.rabbitmq.configurator;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.MessageProperties;

import io.mercury.common.util.Assertor;
import io.mercury.common.util.StringUtil;
import io.mercury.transport.rabbitmq.declare.AmqpQueue;
import io.mercury.transport.rabbitmq.declare.ExchangeRelation;

/**
 * 
 * @author yellow013
 * 
 */
public final class RmqPublisherConfigurator extends RmqConfigurator {

	// 发布者ExchangeDeclare
	private ExchangeRelation publishExchange;
	// 默认RoutingKey
	private String defaultRoutingKey;
	// 默认消息发布参数
	private BasicProperties defaultMsgProps;
	// 消息参数提供者
	private Supplier<BasicProperties> msgPropsSupplier;
	// 是否进行发布确认
	private boolean confirm;
	// 发布确认超时时间
	private long confirmTimeout;
	// 发布确认重试次数
	private int confirmRetry;

	private RmqPublisherConfigurator(Builder builder) {
		super(builder.connection);
		this.publishExchange = builder.publishExchange;
		this.defaultRoutingKey = builder.defaultRoutingKey;
		this.defaultMsgProps = builder.defaultMsgProps;
		this.msgPropsSupplier = builder.msgPropsSupplier;
		this.confirm = builder.confirm;
		this.confirmTimeout = builder.confirmTimeout;
		this.confirmRetry = builder.confirmRetry;
	}

	/**
	 * Use Anonymous Exchange
	 * 
	 * @param connection
	 * @return
	 */
	public static Builder configuration(@Nonnull String host, int port, @Nonnull String username,
			@Nonnull String password) {
		return configuration(RmqConnection.configuration(host, port, username, password).build());
	}

	/**
	 * Use Anonymous Exchange
	 * 
	 * @param connection
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
		return configuration(connection, ExchangeRelation.Anonymous);
	}

	/**
	 * 
	 * @param connection
	 * @param exchangeRelation
	 * @return
	 */
	public static Builder configuration(@Nonnull RmqConnection connection, @Nonnull ExchangeRelation publishExchange) {
		return new Builder(Assertor.nonNull(connection, "connection"),
				Assertor.nonNull(publishExchange, "publishExchange"));
	}

	/**
	 * @return the exchangeDeclare
	 */
	public ExchangeRelation publishExchange() {
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
	 * @return the isConfirm
	 */
	public boolean confirm() {
		return confirm;
	}

	/**
	 * @return the confirmTimeout
	 */
	public long confirmTimeout() {
		return confirmTimeout;
	}

	/**
	 * @return the confirmRetry
	 */
	public int confirmRetry() {
		return confirmRetry;
	}

	private transient String toStringCache;

	@Override
	public String toString() {
		if (toStringCache == null)
			toStringCache = StringUtil.reflectionToString(this);
		return toStringCache;
	}

	public static class Builder {

		// 连接配置
		private RmqConnection connection;

		private ExchangeRelation publishExchange;

		private String defaultRoutingKey = "";
		private BasicProperties defaultMsgProps = MessageProperties.PERSISTENT_BASIC;
		private Supplier<BasicProperties> msgPropsSupplier = null;

		private boolean confirm = false;
		private long confirmTimeout = 5000;
		private int confirmRetry = 3;

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
		private Builder(RmqConnection connection, ExchangeRelation publishExchange) {
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
		public Builder setDefaultRoutingKey(String defaultRoutingKey) {
			this.defaultRoutingKey = defaultRoutingKey;
			return this;
		}

		/**
		 * @param msgProperties the msgProperties to set
		 */
		public Builder setDefaultMsgProps(BasicProperties defaultMsgProps) {
			this.defaultMsgProps = defaultMsgProps;
			return this;
		}

		/**
		 * @param msgPropertiesSupplier
		 */
		public Builder setMsgPropsSupplier(Supplier<BasicProperties> msgPropsSupplier) {
			this.msgPropsSupplier = msgPropsSupplier;
			return this;
		}

		/**
		 * @param isConfirm the isConfirm to set
		 */
		public Builder setConfirm(boolean confirm) {
			this.confirm = confirm;
			return this;
		}

		/**
		 * @param confirmTimeout the confirmTimeout to set
		 */
		public Builder setConfirmTimeout(long confirmTimeout) {
			this.confirmTimeout = confirmTimeout;
			return this;
		}

		/**
		 * @param confirmRetry the confirmRetry to set
		 */
		public Builder setConfirmRetry(int confirmRetry) {
			this.confirmRetry = confirmRetry;
			return this;
		}

	}

	public static void main(String[] args) {
		System.out.println(configuration(RmqConnection.configuration("localhost", 5672, "user0", "userpass").build(),
				ExchangeRelation.direct("TEST").bindingQueue(AmqpQueue.named("TEST_0"))).build());
	}

}

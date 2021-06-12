package io.mercury.transport.rabbitmq.configurator;

import static io.mercury.common.util.Assertor.nonNull;

import javax.annotation.Nonnull;

import io.mercury.serialization.json.JsonWrapper;
import io.mercury.transport.rabbitmq.declare.ExchangeDefinition;
import io.mercury.transport.rabbitmq.declare.QueueDefinition;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 
 * @author yellow013
 *
 */
public final class RabbitReceiverCfg extends RabbitConfigurator {

	// 接受者QueueDeclare
	@Getter
	private final QueueDefinition receiveQueue;

	// 错误消息ExchangeDeclare
	@Getter
	private final ExchangeDefinition errMsgExchange;

	// 错误消息RoutingKey
	@Getter
	private final String errMsgRoutingKey;

	// 错误消息QueueDeclare
	@Getter
	private final QueueDefinition errMsgQueue;

	// 消费者独占队列
	@Getter
	private final boolean exclusive;

	// ACK选项
	@Getter
	private final ReceiveAckOptions ackOptions;

	/**
	 * 
	 * @param builder
	 */
	private RabbitReceiverCfg(Builder builder) {
		super(builder.connection);
		this.receiveQueue = builder.receiveQueue;
		this.errMsgExchange = builder.errMsgExchange;
		this.errMsgRoutingKey = builder.errMsgRoutingKey;
		this.errMsgQueue = builder.errMsgQueue;
		this.exclusive = builder.exclusive;
		this.ackOptions = builder.ackOptions;
	}

	/**
	 * 
	 * @param connection
	 * @param receiveQueue
	 * @return
	 */
	public static Builder configuration(@Nonnull RabbitConnection connection, @Nonnull QueueDefinition receiveQueue) {
		nonNull(connection, "connection");
		nonNull(receiveQueue, "receiveQueue");
		return new Builder(connection, receiveQueue);
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
		// 接受者QueueRelationship
		private final QueueDefinition receiveQueue;
		// 错误消息ExchangeRelationship

		/* v UnProcessable Message v */
		// 错误消息处理Exchange和关联关系
		@Setter
		private ExchangeDefinition errMsgExchange;
		// 错误消息处理RoutingKey
		@Setter
		private String errMsgRoutingKey = "";
		// 错误消息处理QueueRelationship和关联关系
		@Setter
		private QueueDefinition errMsgQueue;
		/* ^ UnProcessable Message ^ */

		// 接收者是否独占队列
		@Setter
		private boolean exclusive = false;

		// ACK选项
		@Setter
		private ReceiveAckOptions ackOptions = ReceiveAckOptions.defaultOption();

		private Builder(RabbitConnection connection, QueueDefinition receiveQueue) {
			this.connection = connection;
			this.receiveQueue = receiveQueue;
		}

		/**
		 * @param isAutoAck the isAutoAck to set
		 */
		public Builder setAutoAck(boolean autoAck) {
			this.ackOptions.setAutoAck(autoAck);
			return this;
		}

		/**
		 * @param isMultipleAck the isMultipleAck to set
		 */
		public Builder setMultipleAck(boolean multipleAck) {
			this.ackOptions.setMultipleAck(multipleAck);
			return this;
		}

		/**
		 * 
		 * @param maxAckTotal
		 * @return
		 */
		public Builder setMaxAckTotal(int maxAckTotal) {
			this.ackOptions.setMaxAckTotal(maxAckTotal);
			return this;
		}

		/**
		 * 
		 * @param maxAckReconnection
		 * @return
		 */
		public Builder setMaxAckReconnection(int maxAckReconnection) {
			this.ackOptions.setMaxAckReconnection(maxAckReconnection);
			return this;
		}

		/**
		 * 
		 * @param qos
		 * @return
		 */
		public Builder setQos(int qos) {
			this.ackOptions.setQos(qos);
			return this;
		}

		public RabbitReceiverCfg build() {
			return new RabbitReceiverCfg(this);
		}

	}
	
	
	/**
	 * 
	 * @author yellow013
	 */
	@Accessors(chain = true)
	public static final class ReceiveAckOptions {

		// 自动ACK, 默认true
		@Getter
		@Setter
		private boolean autoAck = true;

		// 一次ACK多条, 默认false
		@Getter
		@Setter
		private boolean multipleAck = false;

		// ACK最大自动重试次数, 默认16次
		@Getter
		@Setter
		private int maxAckTotal = 16;

		// ACK最大自动重连次数, 默认8次
		@Getter
		@Setter
		private int maxAckReconnection = 8;

		// QOS预取, 默认256
		@Getter
		@Setter
		private int qos = 256;

		/**
		 * 
		 */
		private ReceiveAckOptions() {
		}

		/**
		 * 
		 * @param autoAck
		 * @param multipleAck
		 * @param maxAckTotal
		 * @param maxAckReconnection
		 * @param qos
		 */
		private ReceiveAckOptions(boolean autoAck, boolean multipleAck, int maxAckTotal, int maxAckReconnection, int qos) {
			this.autoAck = autoAck;
			this.multipleAck = multipleAck;
			this.maxAckTotal = maxAckTotal;
			this.maxAckReconnection = maxAckReconnection;
			this.qos = qos;
		}

		/**
		 * 使用默认参数
		 * 
		 * @return
		 */
		public static final ReceiveAckOptions defaultOption() {
			return new ReceiveAckOptions();
		}

		/**
		 * 指定具体参数
		 * 
		 * @return
		 */
		public static final ReceiveAckOptions withOption(boolean autoAck, boolean multipleAck, int maxAckTotal,
				int maxAckReconnection, int qos) {
			return new ReceiveAckOptions(autoAck, multipleAck, maxAckTotal, maxAckReconnection, qos);
		}

	}


}

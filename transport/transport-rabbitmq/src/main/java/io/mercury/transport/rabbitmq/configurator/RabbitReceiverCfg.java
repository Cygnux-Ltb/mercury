package io.mercury.transport.rabbitmq.configurator;

import static io.mercury.common.lang.Assertor.nonNull;

import javax.annotation.Nonnull;

import io.mercury.serialization.json.JsonWrapper;
import io.mercury.transport.rabbitmq.declare.ExchangeDef;
import io.mercury.transport.rabbitmq.declare.QueueDef;

/**
 * 
 * @author yellow013
 *
 */
public final class RabbitReceiverCfg extends RabbitConfigurator {

	// 接受者QueueDeclare
	private final QueueDef receiveQueue;

	// 错误消息ExchangeDeclare
	private final ExchangeDef errMsgExchange;

	// 错误消息RoutingKey
	private final String errMsgRoutingKey;

	// 错误消息QueueDeclare
	private final QueueDef errMsgQueue;

	// 消费者独占队列
	private final boolean exclusive;

	// ACK选项
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
	public static Builder configuration(@Nonnull RabbitConnection connection, @Nonnull QueueDef receiveQueue) {
		nonNull(connection, "connection");
		nonNull(receiveQueue, "receiveQueue");
		return new Builder(connection, receiveQueue);
	}

	public QueueDef getReceiveQueue() {
		return receiveQueue;
	}

	public ExchangeDef getErrMsgExchange() {
		return errMsgExchange;
	}

	public String getErrMsgRoutingKey() {
		return errMsgRoutingKey;
	}

	public QueueDef getErrMsgQueue() {
		return errMsgQueue;
	}

	public boolean isExclusive() {
		return exclusive;
	}

	public ReceiveAckOptions getAckOptions() {
		return ackOptions;
	}

	public String getToStringCache() {
		return toStringCache;
	}

	private transient String toStringCache;

	@Override
	public String toString() {
		if (toStringCache == null)
			toStringCache = JsonWrapper.toJson(this);
		return toStringCache;
	}

	public static class Builder {

		// 连接配置
		private final RabbitConnection connection;
		// 接受者QueueRelationship
		private final QueueDef receiveQueue;
		// 错误消息ExchangeRelationship

		/* v UnProcessable Message v */
		// 错误消息处理Exchange和关联关系
		private ExchangeDef errMsgExchange;
		// 错误消息处理RoutingKey
		private String errMsgRoutingKey = "";
		// 错误消息处理QueueRelationship和关联关系
		private QueueDef errMsgQueue;
		/* ^ UnProcessable Message ^ */

		// 接收者是否独占队列
		private boolean exclusive = false;

		// ACK选项
		private ReceiveAckOptions ackOptions = ReceiveAckOptions.withDefault();

		private Builder(RabbitConnection connection, QueueDef receiveQueue) {
			this.connection = connection;
			this.receiveQueue = receiveQueue;
		}

		public Builder setErrMsgExchange(ExchangeDef errMsgExchange) {
			this.errMsgExchange = errMsgExchange;
			return this;
		}

		public Builder setErrMsgRoutingKey(String errMsgRoutingKey) {
			this.errMsgRoutingKey = errMsgRoutingKey;
			return this;
		}

		public Builder setErrMsgQueue(QueueDef errMsgQueue) {
			this.errMsgQueue = errMsgQueue;
			return this;
		}

		public Builder setExclusive(boolean exclusive) {
			this.exclusive = exclusive;
			return this;
		}

		public Builder setAckOptions(ReceiveAckOptions ackOptions) {
			this.ackOptions = ackOptions;
			return this;
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
	public static final class ReceiveAckOptions {

		// 自动ACK, 默认true
		private boolean autoAck = true;

		// 一次ACK多条, 默认false
		private boolean multipleAck = false;

		// ACK最大自动重试次数, 默认16次
		private int maxAckTotal = 16;

		// ACK最大自动重连次数, 默认8次
		private int maxAckReconnection = 8;

		// QOS预取, 默认256
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
		private ReceiveAckOptions(boolean autoAck, boolean multipleAck, int maxAckTotal, int maxAckReconnection,
				int qos) {
			this.autoAck = autoAck;
			this.multipleAck = multipleAck;
			this.maxAckTotal = maxAckTotal;
			this.maxAckReconnection = maxAckReconnection;
			this.qos = qos;
		}

		public ReceiveAckOptions setAutoAck(boolean autoAck) {
			this.autoAck = autoAck;
			return this;
		}

		public ReceiveAckOptions setMultipleAck(boolean multipleAck) {
			this.multipleAck = multipleAck;
			return this;
		}

		public ReceiveAckOptions setMaxAckTotal(int maxAckTotal) {
			this.maxAckTotal = maxAckTotal;
			return this;
		}

		public ReceiveAckOptions setMaxAckReconnection(int maxAckReconnection) {
			this.maxAckReconnection = maxAckReconnection;
			return this;
		}

		public ReceiveAckOptions setQos(int qos) {
			this.qos = qos;
			return this;
		}

		public boolean isAutoAck() {
			return autoAck;
		}

		public boolean isMultipleAck() {
			return multipleAck;
		}

		public int getMaxAckTotal() {
			return maxAckTotal;
		}

		public int getMaxAckReconnection() {
			return maxAckReconnection;
		}

		public int getQos() {
			return qos;
		}

		/**
		 * 使用默认参数
		 * 
		 * @return
		 */
		public static final ReceiveAckOptions withDefault() {
			return new ReceiveAckOptions();
		}

		/**
		 * 指定具体参数
		 * 
		 * @return
		 */
		public static final ReceiveAckOptions with(boolean autoAck, boolean multipleAck, int maxAckTotal,
				int maxAckReconnection, int qos) {
			return new ReceiveAckOptions(autoAck, multipleAck, maxAckTotal, maxAckReconnection, qos);
		}

	}

}

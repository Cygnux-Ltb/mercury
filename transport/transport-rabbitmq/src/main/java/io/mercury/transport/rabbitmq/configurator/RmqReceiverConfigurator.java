package io.mercury.transport.rabbitmq.configurator;

import javax.annotation.Nonnull;

import io.mercury.common.util.Assertor;
import io.mercury.common.util.StringUtil;
import io.mercury.transport.rabbitmq.declare.ExchangeRelationship;
import io.mercury.transport.rabbitmq.declare.QueueRelationship;

public final class RmqReceiverConfigurator extends RmqConfigurator {

	// 接受者QueueDeclare
	private QueueRelationship receiveQueue;

	// 错误消息ExchangeDeclare
	private ExchangeRelationship errMsgExchange;

	// 错误消息RoutingKey
	private String errMsgRoutingKey;

	// 错误消息QueueDeclare
	private QueueRelationship errMsgQueue;

	// 自动ACK
	private boolean autoAck;

	// 一次ACK多条
	private boolean multipleAck;

	// 最大重新ACK次数
	private int maxAckTotal;

	// 最大ACK重连次数
	private int maxAckReconnection;

	// QOS预取
	private int qos;

	private RmqReceiverConfigurator(Builder builder) {
		super(builder.connection);
		this.receiveQueue = builder.receiveQueue;
		this.errMsgExchange = builder.errMsgExchange;
		this.errMsgRoutingKey = builder.errMsgRoutingKey;
		this.errMsgQueue = builder.errMsgQueue;
		this.autoAck = builder.autoAck;
		this.multipleAck = builder.multipleAck;
		this.maxAckTotal = builder.maxAckTotal;
		this.maxAckReconnection = builder.maxAckReconnection;
		this.qos = builder.qos;
	}

	/**
	 * 
	 * @param connection
	 * @param receiveQueue
	 * @return
	 */
	public static Builder configuration(@Nonnull RmqConnection connection, @Nonnull QueueRelationship receiveQueue) {
		return new Builder(Assertor.nonNull(connection, "connection"), Assertor.nonNull(receiveQueue, "receiveQueue"));
	}

	/**
	 * @return the queueDeclare
	 */
	public QueueRelationship receiveQueue() {
		return receiveQueue;
	}

	/**
	 * @return the errorMsgExchange
	 */
	public ExchangeRelationship errMsgExchange() {
		return errMsgExchange;
	}

	/**
	 * 
	 * @return the errorMsgRoutingKey
	 */
	public String errMsgRoutingKey() {
		return errMsgRoutingKey;
	}

	/**
	 * 
	 * @return the errorMsgQueue
	 */
	public QueueRelationship errMsgQueue() {
		return errMsgQueue;
	}

	/**
	 * @return the isAutoAck
	 */
	public boolean autoAck() {
		return autoAck;
	}

	/**
	 * @return the isMultipleAck
	 */
	public boolean multipleAck() {
		return multipleAck;
	}

	/**
	 * @return the maxAckTotal
	 */
	public int maxAckTotal() {
		return maxAckTotal;
	}

	/**
	 * @return the maxAckReconnection
	 */
	public int maxAckReconnection() {
		return maxAckReconnection;
	}

	/**
	 * @return the qos
	 */
	public int qos() {
		return qos;
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
		// 接受者QueueRelationship
		private QueueRelationship receiveQueue;
		// 错误消息ExchangeRelationship
		private ExchangeRelationship errMsgExchange;
		// 错误消息RoutingKey
		private String errMsgRoutingKey = "";
		// 错误消息QueueRelationship
		private QueueRelationship errMsgQueue;
		// 自动ACK
		private boolean autoAck = true;
		// 一次ACK多条
		private boolean multipleAck = false;
		// 最大重新ACK次数
		private int maxAckTotal = 16;
		// 最大ACK重连次数
		private int maxAckReconnection = 8;
		// QOS预取
		private int qos = 256;

		private Builder(RmqConnection connection, QueueRelationship receiveQueue) {
			this.connection = connection;
			this.receiveQueue = receiveQueue;
		}

		/**
		 * @param errorMsgExchange the errorMsgExchange to set
		 */
		public Builder setErrMsgExchange(ExchangeRelationship errMsgExchange) {
			this.errMsgExchange = errMsgExchange;
			return this;
		}

		/**
		 * 
		 * @param errorMsgRoutingKey
		 */
		public Builder setErrMsgRoutingKey(String errMsgRoutingKey) {
			this.errMsgRoutingKey = errMsgRoutingKey;
			return this;
		}

		/**
		 * 
		 * @param errorMsgQueue
		 */
		public Builder setErrMsgQueue(QueueRelationship errMsgQueue) {
			this.errMsgQueue = errMsgQueue;
			return this;
		}

		/**
		 * @param isAutoAck the isAutoAck to set
		 */
		public Builder setAutoAck(boolean autoAck) {
			this.autoAck = autoAck;
			return this;
		}

		/**
		 * @param isMultipleAck the isMultipleAck to set
		 */
		public Builder setMultipleAck(boolean multipleAck) {
			this.multipleAck = multipleAck;
			return this;
		}

		/**
		 * @param maxAckTotal the maxAckTotal to set
		 */
		public Builder setMaxAckTotal(int maxAckTotal) {
			this.maxAckTotal = maxAckTotal;
			return this;
		}

		/**
		 * @param maxAckReconnection the maxAckReconnection to set
		 */
		public Builder setMaxAckReconnection(int maxAckReconnection) {
			this.maxAckReconnection = maxAckReconnection;
			return this;
		}

		/**
		 * @param qos the QOS to set
		 */
		public Builder setQos(int qos) {
			this.qos = qos;
			return this;
		}

		public RmqReceiverConfigurator build() {
			return new RmqReceiverConfigurator(this);
		}

	}

}

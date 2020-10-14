package io.mercury.transport.rabbitmq.configurator;

import static io.mercury.common.util.Assertor.nonNull;
import static io.mercury.transport.rabbitmq.configurator.ReceiveAckOptions.defaultOption;

import javax.annotation.Nonnull;

import io.mercury.serialization.json.JsonWrapper;
import io.mercury.transport.rabbitmq.declare.ExchangeRelationship;
import io.mercury.transport.rabbitmq.declare.QueueRelationship;

/**
 * 
 * @author yellow013
 *
 */
public final class RmqReceiverConfigurator extends RmqConfigurator {

	// 接受者QueueDeclare
	private QueueRelationship receiveQueue;

	// 错误消息ExchangeDeclare
	private ExchangeRelationship errMsgExchange;

	// 错误消息RoutingKey
	private String errMsgRoutingKey;

	// 错误消息QueueDeclare
	private QueueRelationship errMsgQueue;

	// 消费者独占队列
	private boolean exclusive;

	// ACK选项
	private ReceiveAckOptions ackOptions;

	/**
	 * 
	 * @param builder
	 */
	private RmqReceiverConfigurator(Builder builder) {
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
	public static Builder configuration(@Nonnull RmqConnection connection, @Nonnull QueueRelationship receiveQueue) {
		return new Builder(nonNull(connection, "connection"), nonNull(receiveQueue, "receiveQueue"));
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
	 * 
	 * @return the exclusive
	 */
	public boolean exclusive() {
		return exclusive;
	}

	public ReceiveAckOptions ackOptions() {
		return ackOptions;
	}

	/**
	 * @return the isAutoAck
	 */
	public boolean autoAck() {
		return ackOptions.isAutoAck();
	}

	/**
	 * @return the isMultipleAck
	 */
	public boolean multipleAck() {
		return ackOptions.isMultipleAck();
	}

	/**
	 * @return the maxAckTotal
	 */
	public int maxAckTotal() {
		return ackOptions.getMaxAckTotal();
	}

	/**
	 * @return the maxAckReconnection
	 */
	public int maxAckReconnection() {
		return ackOptions.getMaxAckReconnection();
	}

	/**
	 * @return the qos
	 */
	public int qos() {
		return ackOptions.getQos();
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
		private RmqConnection connection;
		// 接受者QueueRelationship
		private QueueRelationship receiveQueue;
		// 错误消息ExchangeRelationship

		// UnProcessable Message
		// 错误消息处理Exchange和关联关系
		private ExchangeRelationship errMsgExchange;
		// 错误消息处理RoutingKey
		private String errMsgRoutingKey = "";
		// 错误消息处理QueueRelationship和关联关系
		private QueueRelationship errMsgQueue;

		// 接收者是否独占队列
		private boolean exclusive = false;
		// ACK选项
		private ReceiveAckOptions ackOptions = defaultOption();

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
		 * 
		 * @param exclusive
		 * @return
		 */
		public Builder setExclusive(boolean exclusive) {
			this.exclusive = exclusive;
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
		 * @param maxAckTotal the maxAckTotal to set
		 */
		public Builder setMaxAckTotal(int maxAckTotal) {
			this.ackOptions.setMaxAckTotal(maxAckTotal);
			return this;
		}

		/**
		 * @param maxAckReconnection the maxAckReconnection to set
		 */
		public Builder setMaxAckReconnection(int maxAckReconnection) {
			this.ackOptions.setMaxAckReconnection(maxAckReconnection);
			return this;
		}

		/**
		 * @param qos the QOS to set
		 */
		public Builder setQos(int qos) {
			this.ackOptions.setQos(qos);
			return this;
		}

		public RmqReceiverConfigurator build() {
			return new RmqReceiverConfigurator(this);
		}

	}

}

package io.mercury.transport.rabbitmq.configurator;

import javax.annotation.Nonnull;

/**
 * 
 * @author yellow013
 *
 */
@Deprecated
public final class RmqReceiverSimpleConfigurator {

	/**
	 * 接收者参数
	 */
	// 接受者队列
	private String receiveQueue;

	// 错误消息Exchange
	private String errorMsgToExchange;
	// 是否持久化
	private boolean durable = true;
	// 连接独占此队列
	private boolean exclusive = false;
	// channel关闭后自动删除队列
	private boolean autoDelete = false;
	// 自动ACK
	private boolean isAutoAck = true;
	// 一次ACK多条
	private boolean isMultipleAck = false;
	// 最大重新ACK次数
	private int maxAckTotal = 16;
	// 最大ACK重连次数
	private int maxAckReconnection = 8;
	// QOS预取
	private int qos = 256;

	// 连接配置
	private RmqConnection rmqConnection;

	private RmqReceiverSimpleConfigurator(RmqConnection rmqConnection) {
		this.rmqConnection = rmqConnection;
	}

	public static RmqReceiverSimpleConfigurator configuration(@Nonnull RmqConnection rmqConnection) {
		return new RmqReceiverSimpleConfigurator(rmqConnection);
	}

	public RmqConnection getRmqConnection() {
		return rmqConnection;
	}

	public String getReceiveQueue() {
		return receiveQueue;
	}

	public String getErrorMsgToExchange() {
		return errorMsgToExchange;
	}

	public boolean isDurable() {
		return durable;
	}

	public boolean isExclusive() {
		return exclusive;
	}

	public boolean isAutoDelete() {
		return autoDelete;
	}

	public boolean isAutoAck() {
		return isAutoAck;
	}

	public boolean isMultipleAck() {
		return isMultipleAck;
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

	public RmqReceiverSimpleConfigurator setReceiveQueue(String receiveQueue) {
		this.receiveQueue = receiveQueue;
		return this;
	}

	public RmqReceiverSimpleConfigurator setErrorMsgToExchange(String errorMsgToExchange) {
		this.errorMsgToExchange = errorMsgToExchange;
		return this;
	}

	public RmqReceiverSimpleConfigurator setDurable(boolean durable) {
		this.durable = durable;
		return this;
	}

	public RmqReceiverSimpleConfigurator setExclusive(boolean exclusive) {
		this.exclusive = exclusive;
		return this;
	}

	public RmqReceiverSimpleConfigurator setAutoDelete(boolean autoDelete) {
		this.autoDelete = autoDelete;
		return this;
	}

	public RmqReceiverSimpleConfigurator setAutoAck(boolean isAutoAck) {
		this.isAutoAck = isAutoAck;
		return this;
	}

	public RmqReceiverSimpleConfigurator setMultipleAck(boolean isMultipleAck) {
		this.isMultipleAck = isMultipleAck;
		return this;
	}

	public RmqReceiverSimpleConfigurator setMaxAckTotal(int maxAckTotal) {
		this.maxAckTotal = maxAckTotal;
		return this;
	}

	public RmqReceiverSimpleConfigurator setMaxAckReconnection(int maxAckReconnection) {
		this.maxAckReconnection = maxAckReconnection;
		return this;
	}

	public RmqReceiverSimpleConfigurator setQos(int qos) {
		this.qos = qos;
		return this;
	}

}

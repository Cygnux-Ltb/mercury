package io.mercury.transport.rabbitmq.configurator;

import javax.annotation.Nonnull;

/**
 * 
 * @author yellow013
 *
 */
@Deprecated
public final class ReceiverConfigurator0 {

	/**
	 * 接收者参数
	 */
	// 接受者队列
	private String receiveQueue;
	// 需要绑定的Exchange
	private String exchange[];
	// 需要绑定的routingKey
	private String routingKey[];

	// 错误消息Ecchange
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
	private RmqConnection connectionConfigurator;

	private ReceiverConfigurator0(RmqConnection connectionConfigurator) {
		this.connectionConfigurator = connectionConfigurator;
	}

	public static ReceiverConfigurator0 configuration(@Nonnull RmqConnection connectionConfigurator) {
		return new ReceiverConfigurator0(connectionConfigurator);
	}

	public RmqConnection getConnectionConfigurator() {
		return connectionConfigurator;
	}

	public String[] getExchange() {
		return exchange;
	}

	public String[] getRoutingKey() {
		return routingKey;
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

	public ReceiverConfigurator0 setExchange(String... exchange) {
		this.exchange = exchange;
		return this;
	}

	public void setRoutingKey(String... routingKey) {
		this.routingKey = routingKey;
	}

	public ReceiverConfigurator0 setReceiveQueue(String receiveQueue) {
		this.receiveQueue = receiveQueue;
		return this;
	}

	public ReceiverConfigurator0 setErrorMsgToExchange(String errorMsgToExchange) {
		this.errorMsgToExchange = errorMsgToExchange;
		return this;
	}

	public ReceiverConfigurator0 setDurable(boolean durable) {
		this.durable = durable;
		return this;
	}

	public ReceiverConfigurator0 setExclusive(boolean exclusive) {
		this.exclusive = exclusive;
		return this;
	}

	public ReceiverConfigurator0 setAutoDelete(boolean autoDelete) {
		this.autoDelete = autoDelete;
		return this;
	}

	public ReceiverConfigurator0 setAutoAck(boolean isAutoAck) {
		this.isAutoAck = isAutoAck;
		return this;
	}

	public ReceiverConfigurator0 setMultipleAck(boolean isMultipleAck) {
		this.isMultipleAck = isMultipleAck;
		return this;
	}

	public ReceiverConfigurator0 setMaxAckTotal(int maxAckTotal) {
		this.maxAckTotal = maxAckTotal;
		return this;
	}

	public ReceiverConfigurator0 setMaxAckReconnection(int maxAckReconnection) {
		this.maxAckReconnection = maxAckReconnection;
		return this;
	}

	public ReceiverConfigurator0 setQos(int qos) {
		this.qos = qos;
		return this;
	}

}

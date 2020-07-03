package io.mercury.transport.rabbitmq.configurator;

import javax.annotation.Nonnull;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.MessageProperties;

/**
 * 
 * @author yellow013
 * 
 *         TODO 扩展针对多个routingKey的绑定关系
 */

public final class RmqPublisherSimpleConfigurator {

	/**
	 * 发布者参数
	 */
	private String exchange = "";
	private String routingKey = "";
	private BasicProperties msgProperties = MessageProperties.PERSISTENT_BASIC;
	// 是否持久化
	private boolean durable = true;
	// 没有使用时自动删除
	private boolean autoDelete = false;
	// 是否为内部Exchange
	private boolean internal = false;
	// 连接独占此队列(针对绑定的队列)
	private boolean exclusive = false;

	private boolean isConfirm = false;
	private long confirmTimeout = 5000;
	private int confirmRetry = 3;

	// 连接配置
	private RmqConnection rmqConnection;

	private RmqPublisherSimpleConfigurator(RmqConnection rmqConnection) {
		this.rmqConnection = rmqConnection;
	}

	public static RmqPublisherSimpleConfigurator configuration(@Nonnull RmqConnection rmqConnection) {
		return new RmqPublisherSimpleConfigurator(rmqConnection);
	}

	public String getExchange() {
		return exchange;
	}

	public String getRoutingKey() {
		return routingKey;
	}

	public BasicProperties getMsgProperties() {
		return msgProperties;
	}

	public boolean isDurable() {
		return durable;
	}

	public boolean isAutoDelete() {
		return autoDelete;
	}

	public boolean isInternal() {
		return internal;
	}

	public boolean isExclusive() {
		return exclusive;
	}

	public boolean isConfirm() {
		return isConfirm;
	}

	public long getConfirmTimeout() {
		return confirmTimeout;
	}

	public int getConfirmRetry() {
		return confirmRetry;
	}

	public RmqConnection getRmqConnection() {
		return rmqConnection;
	}

	public RmqPublisherSimpleConfigurator setExchange(String exchange) {
		this.exchange = exchange;
		return this;
	}

	public RmqPublisherSimpleConfigurator setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
		return this;
	}

	public RmqPublisherSimpleConfigurator setMsgProperties(BasicProperties msgProperties) {
		this.msgProperties = msgProperties;
		return this;
	}

	public RmqPublisherSimpleConfigurator setDurable(boolean durable) {
		this.durable = durable;
		return this;
	}

	public RmqPublisherSimpleConfigurator setAutoDelete(boolean autoDelete) {
		this.autoDelete = autoDelete;
		return this;
	}

	public RmqPublisherSimpleConfigurator setInternal(boolean internal) {
		this.internal = internal;
		return this;
	}

	public RmqPublisherSimpleConfigurator setExclusive(boolean exclusive) {
		this.exclusive = exclusive;
		return this;
	}

	public RmqPublisherSimpleConfigurator setConfirm(boolean isConfirm) {
		this.isConfirm = isConfirm;
		return this;
	}

	public RmqPublisherSimpleConfigurator setConfirmTimeout(long confirmTimeout) {
		this.confirmTimeout = confirmTimeout;
		return this;
	}

	public RmqPublisherSimpleConfigurator setConfirmRetry(int confirmRetry) {
		this.confirmRetry = confirmRetry;
		return this;
	}

	public RmqPublisherSimpleConfigurator setRmqConnection(RmqConnection rmqConnection) {
		this.rmqConnection = rmqConnection;
		return this;
	}

}

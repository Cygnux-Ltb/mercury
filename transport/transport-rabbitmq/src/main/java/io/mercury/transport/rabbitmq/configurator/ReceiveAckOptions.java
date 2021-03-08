package io.mercury.transport.rabbitmq.configurator;

/**
 * 
 * 
 * 
 * @author yellow013
 */
public final class ReceiveAckOptions {

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

	public ReceiveAckOptions(boolean autoAck, boolean multipleAck, int maxAckTotal, int maxAckReconnection, int qos) {
		this.autoAck = autoAck;
		this.multipleAck = multipleAck;
		this.maxAckTotal = maxAckTotal;
		this.maxAckReconnection = maxAckReconnection;
		this.qos = qos;
	}

	private ReceiveAckOptions() {
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

}

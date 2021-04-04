package io.mercury.transport.rabbitmq.configurator;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 
 * @author yellow013
 */
@Accessors(chain = true)
public final class ReceiveAckOptions {

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

}

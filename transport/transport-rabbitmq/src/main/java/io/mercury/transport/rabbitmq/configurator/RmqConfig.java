package io.mercury.transport.rabbitmq.configurator;

/**
 * 
 * @author yellow013
 *
 */
public abstract class RmqConfig {

	/**
	 * 连接配置信息
	 */
	private final RmqConnection connection;

	protected RmqConfig(RmqConnection connection) {
		this.connection = connection;
	}

	public RmqConnection getConnection() {
		return connection;
	}

}

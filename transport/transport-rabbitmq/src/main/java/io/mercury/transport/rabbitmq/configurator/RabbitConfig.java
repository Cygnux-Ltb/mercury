package io.mercury.transport.rabbitmq.configurator;

/**
 * 
 * @author yellow013
 *
 */
abstract class RabbitConfig {

	/**
	 * 连接配置信息
	 */
	private final RabbitConnection connection;

	RabbitConfig(RabbitConnection connection) {
		this.connection = connection;
	}

	public RabbitConnection getConnection() {
		return connection;
	}

}

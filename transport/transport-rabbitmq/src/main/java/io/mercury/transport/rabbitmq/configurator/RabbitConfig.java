package io.mercury.transport.rabbitmq.configurator;

/**
 * 
 * @author yellow013
 *
 */
abstract class RabbitConfigurator {

	/**
	 * 连接配置信息
	 */
	private final RabbitConnection connection;

	RabbitConfigurator(RabbitConnection connection) {
		this.connection = connection;
	}

	public RabbitConnection getConnection() {
		return connection;
	}

}

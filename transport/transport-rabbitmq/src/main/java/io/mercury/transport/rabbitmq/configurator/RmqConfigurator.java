package io.mercury.transport.rabbitmq.configurator;

public abstract class RmqConfigurator {

	// 连接配置
	private RmqConnection connection;

	protected RmqConfigurator(RmqConnection connection) {
		this.connection = connection;
	}

	/**
	 * @return the connectionConfigurator
	 */
	public RmqConnection connection() {
		return connection;
	}

}

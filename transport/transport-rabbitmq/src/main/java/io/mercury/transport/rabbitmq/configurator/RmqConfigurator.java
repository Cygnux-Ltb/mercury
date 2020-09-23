package io.mercury.transport.rabbitmq.configurator;

/**
 * 
 * @author yellow013
 *
 */
public abstract class RmqConfigurator {

	/**
	 * 连接配置信息
	 */
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

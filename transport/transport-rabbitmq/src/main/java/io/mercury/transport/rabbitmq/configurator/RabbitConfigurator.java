package io.mercury.transport.rabbitmq.configurator;

import javax.annotation.Nonnull;

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

	protected RmqConfigurator(@Nonnull RmqConnection connection) {
		this.connection = connection;
	}

	/**
	 * @return the connectionConfigurator
	 */
	public RmqConnection connection() {
		return connection;
	}

}

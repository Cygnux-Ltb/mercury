package io.mercury.transport.rabbitmq.configurator;

import io.mercury.common.config.ConfigOption;

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

	public static enum RabbitConfigOption implements ConfigOption {

		Host("rmq.host", "rabbitmq.host"),

		Port("rmq.port", "rabbitmq.port"),

		Username("rmq.username", "rabbitmq.username"),

		Password("rmq.password", "rabbitmq.password"),

		VirtualHost("rmq.virtualHost", "rabbitmq.virtualHost")

		;

		private final String configName;

		private final String otherConfigName;

		private RabbitConfigOption(String configName, String otherConfigName) {
			this.configName = configName;
			this.otherConfigName = otherConfigName;
		}

		@Override
		public String getConfigName() {
			return configName;
		}

		@Override
		public String getOtherConfigName() {
			return otherConfigName;
		}

	}

}

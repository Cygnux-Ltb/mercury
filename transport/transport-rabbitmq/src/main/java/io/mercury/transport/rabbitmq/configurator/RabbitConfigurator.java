package io.mercury.transport.rabbitmq.configurator;

import javax.annotation.Nonnull;

import lombok.Getter;

/**
 * 
 * @author yellow013
 *
 */
public abstract class RabbitConfigurator {

	/**
	 * 连接配置信息
	 */
	@Getter
	private final RabbitConnection connection;

	protected RabbitConfigurator(@Nonnull RabbitConnection connection) {
		this.connection = connection;
	}

}

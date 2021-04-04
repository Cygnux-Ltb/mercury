package io.mercury.transport.rabbitmq.configurator;

import static lombok.AccessLevel.PROTECTED;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author yellow013
 *
 */
@RequiredArgsConstructor(access = PROTECTED)
public abstract class RabbitConfigurator {

	/**
	 * 连接配置信息
	 */
	@Getter
	private final RabbitConnection connection;

}

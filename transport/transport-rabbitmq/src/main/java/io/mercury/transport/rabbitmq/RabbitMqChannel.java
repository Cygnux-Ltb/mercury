package io.mercury.transport.rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;

import io.mercury.transport.rabbitmq.configurator.RmqConnection;

public final class RabbitMqChannel extends AbstractRabbitMqTransport {

	/**
	 * Create GeneralChannel of host, port, username and password
	 * 
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public static RabbitMqChannel create(String host, int port, String username, String password) {
		return create(RmqConnection.configuration(host, port, username, password).build());
	}

	/**
	 * Create GeneralChannel of host, port, username, password and virtualHost
	 * 
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @param virtualHost
	 * @return
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public static RabbitMqChannel create(String host, int port, String username, String password,
			String virtualHost) {
		return create(RmqConnection.configuration(host, port, username, password, virtualHost).build());
	}

	/**
	 * Create GeneralChannel of RmqConnection
	 * 
	 * @param configurator
	 * @return
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public static RabbitMqChannel create(RmqConnection connection) {
		return new RabbitMqChannel("", connection);
	}

	/**
	 * Create GeneralChannel of Channel
	 * 
	 * @param channel
	 * @return
	 */
	static RabbitMqChannel ofChannel(Channel channel) {
		return new RabbitMqChannel(channel);
	}

	private RabbitMqChannel(String tag, RmqConnection connection) {
		super(tag, "GeneralChannel", connection);
		createConnection();
	}

	private RabbitMqChannel(Channel channel) {
		this.channel = channel;
	}

	public Channel internalChannel() {
		return channel;
	}

}

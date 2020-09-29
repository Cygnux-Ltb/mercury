package io.mercury.transport.rabbitmq;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;

import io.mercury.common.datetime.TimeZone;
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
	public static RabbitMqChannel newWith(String host, int port, String username, String password) {
		return newWith(RmqConnection.configuration(host, port, username, password).build());
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
	public static RabbitMqChannel newWith(String host, int port, String username, String password, String virtualHost) {
		return newWith(RmqConnection.configuration(host, port, username, password, virtualHost).build());
	}

	/**
	 * Create GeneralChannel of RmqConnection
	 * 
	 * @param configurator
	 * @return
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public static RabbitMqChannel newWith(RmqConnection connection) {
		return new RabbitMqChannel(null, connection);
	}

	/**
	 * Create GeneralChannel of Channel
	 * 
	 * @param channel
	 * @return
	 */
	public static RabbitMqChannel newWith(Channel channel) {
		return new RabbitMqChannel(channel);
	}

	private RabbitMqChannel(String tag, RmqConnection connection) {
		super("GeneralChannel-" + ZonedDateTime.now(TimeZone.SYS_DEFAULT), connection);
		createConnection();
	}

	private RabbitMqChannel(Channel channel) {
		super("channel-" + channel.getChannelNumber());
		this.channel = channel;
	}

	public Channel internalChannel() {
		return channel;
	}

}

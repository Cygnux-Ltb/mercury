package io.mercury.transport.rmq;

import static io.mercury.common.datetime.TimeZone.SYS_DEFAULT;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;

import io.mercury.transport.rmq.configurator.RmqConnection;

public final class RmqChannel extends RmqTransport {

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
	public static RmqChannel with(String host, int port, String username, String password) {
		return with(RmqConnection.with(host, port, username, password).build());
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
	public static RmqChannel with(String host, int port, String username, String password, String virtualHost) {
		return with(RmqConnection.with(host, port, username, password, virtualHost).build());
	}

	/**
	 * Create GeneralChannel of RmqConnection
	 * 
	 * @param configurator
	 * @return
	 * @throws IOException
	 * @throws TimeoutException
	 */
	public static RmqChannel with(RmqConnection connection) {
		return new RmqChannel(null, connection);
	}

	/**
	 * Create GeneralChannel of Channel
	 * 
	 * @param channel
	 * @return
	 */
	public static RmqChannel newWith(Channel channel) {
		return new RmqChannel(channel);
	}

	/**
	 * 
	 * @param tag
	 * @param connection
	 */
	private RmqChannel(String tag, RmqConnection connection) {
		super("RmqChannel-" + ZonedDateTime.now(SYS_DEFAULT), connection);
		createConnection();
	}

	/**
	 * 
	 * @param channel
	 */
	private RmqChannel(Channel channel) {
		super("channel-" + channel.getChannelNumber());
		this.channel = channel;
	}

	/**
	 * 
	 * @return
	 */
	public Channel internalChannel() {
		return channel;
	}

}

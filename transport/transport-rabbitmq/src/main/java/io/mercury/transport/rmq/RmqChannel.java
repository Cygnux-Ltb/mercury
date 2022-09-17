package io.mercury.transport.rmq;

import com.rabbitmq.client.Channel;
import io.mercury.transport.rmq.configurator.RmqConnection;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeoutException;

import static io.mercury.common.datetime.TimeZone.SYS_DEFAULT;

public final class RmqChannel extends RmqTransport {

    /**
     * Create GeneralChannel of host, port, username and password
     *
     * @param host     String
     * @param port     int
     * @param username String
     * @param password String
     * @return RmqChannel
     * @throws IOException      ioe
     * @throws TimeoutException te
     */
    public static RmqChannel with(String host, int port, String username, String password) {
        return with(RmqConnection.with(host, port, username, password).build());
    }

    /**
     * Create GeneralChannel of host, port, username, password and virtualHost
     *
     * @param host        String
     * @param port        int
     * @param username    String
     * @param password    String
     * @param virtualHost String
     * @return RmqChannel
     * @throws IOException      ioe
     * @throws TimeoutException te
     */
    public static RmqChannel with(String host, int port, String username, String password, String virtualHost) {
        return with(RmqConnection.with(host, port, username, password, virtualHost).build());
    }

    /**
     * Create GeneralChannel of RmqConnection
     *
     * @param connection io.mercury.transport.rmq.configurator.RmqConnection
     * @return RmqChannel
     * @throws IOException
     * @throws TimeoutException
     */
    public static RmqChannel with(RmqConnection connection) {
        return new RmqChannel(null, connection);
    }

    /**
     * Create GeneralChannel of Channel
     *
     * @param channel com.rabbitmq.client.Channel
     * @return RmqChannel
     */
    public static RmqChannel newWith(Channel channel) {
        return new RmqChannel(channel);
    }

    /**
     * @param tag
     * @param connection
     */
    private RmqChannel(String tag, RmqConnection connection) {
        super("RmqChannel-" + ZonedDateTime.now(SYS_DEFAULT), connection);
        createConnection();
    }

    /**
     * @param channel
     */
    private RmqChannel(Channel channel) {
        super("channel-" + channel.getChannelNumber());
        this.channel = channel;
    }

    /**
     * @return
     */
    public Channel internalChannel() {
        return channel;
    }

}

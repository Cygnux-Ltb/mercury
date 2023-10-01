package io.mercury.transport.rmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Method;
import com.rabbitmq.client.ShutdownSignalException;
import io.mercury.common.functional.ThrowableHandler;
import io.mercury.common.lang.Asserter;
import io.mercury.common.log4j2.Log4j2LoggerFactory;
import io.mercury.common.util.StringSupport;
import io.mercury.transport.api.Transport;
import io.mercury.transport.api.TransportComponent;
import io.mercury.transport.exception.ConnectionFailedException;
import io.mercury.transport.rmq.cfg.RmqConnection;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static io.mercury.common.thread.SleepSupport.sleep;
import static java.lang.System.currentTimeMillis;

public abstract class RmqTransport extends TransportComponent implements Transport, Closeable {

    private static final Logger log = Log4j2LoggerFactory.getLogger(RmqTransport.class);

    // 连接RabbitMQ Server使用的组件
    protected ConnectionFactory connectionFactory;
    protected volatile Connection connection;
    protected volatile Channel channel;

    // 存储配置信息对象
    protected RmqConnection rmqConnection;

    // 停机事件, 在监听到ShutdownSignalException时调用
    protected ShutdownSignalHandler shutdownSignalHandler;

    // 组件标签
    protected final String tag;

    /**
     * @param tag String
     */
    protected RmqTransport(String tag) {
        // Generally not used
        this.tag = tag;
    }

    /**
     * @param tag           String
     * @param rmqConnection RmqConnection
     */
    protected RmqTransport(@Nonnull String tag, @Nonnull RmqConnection rmqConnection) {
        Asserter.nonNull(rmqConnection, "rabbitConnection");
        this.tag = tag;
        this.rmqConnection = rmqConnection;
        this.shutdownSignalHandler = rmqConnection.getShutdownSignalHandler();
    }

    protected void createConnection() throws ConnectionFailedException {
        log.info("Create connection started");
        if (connectionFactory == null) {
            connectionFactory = rmqConnection.createConnectionFactory();
        }
        try {
            connection = connectionFactory.newConnection();
            connection.setId(tag + "$[" + currentTimeMillis() + "]");
            log.info("Function -> [connectionFactory.newConnection()] finished, tag -> {}, connection id -> {}", tag,
                    connection.getId());
            connection.addShutdownListener(this::handleShutdownSignal);
            channel = connection.createChannel();
            log.info("Function -> [connection.createChannel()] finished, connection id -> {}, channel number -> {}",
                    connection.getId(), channel.getChannelNumber());
            log.info("Create connection finished");
        } catch (IOException | TimeoutException e) {
            log.error("Func RabbitMqTransport::createConnection() throw: {}, msg: {}", e.getClass().getSimpleName(),
                    e.getMessage(), e);
            throw new ConnectionFailedException(e.getMessage(), e);
        }
    }

    @Override
    public boolean isConnected() {
        return connection != null && connection.isOpen() && channel != null && channel.isOpen();
    }

    protected boolean closeAndReconnection() {
        log.info("Function closeAndReconnection()");
        closeConnection();
        sleep(rmqConnection.getRecoveryInterval() / 2);
        createConnection();
        sleep(rmqConnection.getRecoveryInterval() / 2);
        return isConnected();
    }

    protected void handleShutdownSignal(ShutdownSignalException sig) {
        // 关闭信号
        log.info("Shutdown listener message -> {}", sig.getMessage());
        if (isNormalShutdown(sig))
            log.info("connection id -> {}, normal shutdown", connection.getId());
        else {
            log.error("connection id -> {}, not normal shutdown", connection.getId());
            // 如果回调函数不为null, 则执行此函数
            if (shutdownSignalHandler != null)
                shutdownSignalHandler.accept(sig);
        }
    }

    private boolean isNormalShutdown(ShutdownSignalException sig) {
        Method reason = sig.getReason();
        if (reason instanceof AMQP.Channel.Close channelClose) {
            return channelClose.getReplyCode() == AMQP.REPLY_SUCCESS
                    && StringSupport.isEquals(channelClose.getReplyText(), "OK");
        } else if (reason instanceof AMQP.Connection.Close connectionClose) {
            return connectionClose.getReplyCode() == AMQP.REPLY_SUCCESS
                    && StringSupport.isEquals(connectionClose.getReplyText(), "OK");
        } else
            return false;
    }

    protected void closeConnection() {
        log.info("Call Func -> RabbitMqTransport::closeConnection()");
        try {
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                    log.info("Channel is closed!");
                } catch (IOException | TimeoutException e) {
                    log.error("Func -> Channel::close() throw: {}, msg: {}", e.getClass().getSimpleName(),
                            e.getMessage());
                    throw e;
                }
            }
            if (connection != null && connection.isOpen()) {
                try {
                    connection.close();
                    log.info("Connection is closed!");
                } catch (IOException e) {
                    log.error("Func -> Connection::close() throw: {}, msg: {}", e.getClass().getSimpleName(),
                            e.getMessage());
                    throw e;
                }
            }
        } catch (IOException | TimeoutException e) {
            log.error("Catch Exception: {}, msg: {}", e.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    @Override
    public boolean closeIgnoreException() {
        log.info("Call Func -> RabbitMqTransport::closeIgnoreException(), tag==[{}]", tag);
        closeConnection();
        newEndTime();
        return true;
    }

    @Override
    public void close() throws IOException {
        closeIgnoreException();
    }

    @Override
    public String getName() {
        return tag;
    }

    @FunctionalInterface
    public interface ShutdownSignalHandler extends ThrowableHandler<ShutdownSignalException> {

    }

}

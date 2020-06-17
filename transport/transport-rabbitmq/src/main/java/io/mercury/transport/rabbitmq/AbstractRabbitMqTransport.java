package io.mercury.transport.rabbitmq;

import static io.mercury.common.thread.ThreadTool.sleep;
import static io.mercury.common.util.StringUtil.isNullOrEmpty;

import java.io.Closeable;
import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.TimeoutException;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Method;
import com.rabbitmq.client.ShutdownSignalException;

import io.mercury.common.functional.ShutdownEvent;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.util.Assertor;
import io.mercury.common.util.StringUtil;
import io.mercury.transport.core.TransportModule;
import io.mercury.transport.rabbitmq.configurator.RmqConnection;

public abstract class AbstractRabbitMqTransport implements TransportModule, Closeable {

	// 连接RabbitMQ Server使用的组件
	protected ConnectionFactory connectionFactory;
	protected volatile Connection connection;
	protected volatile Channel channel;

	// 存储配置信息对象
	protected RmqConnection rmqConnection;

	// 停机事件, 在监听到ShutdownSignalException时调用
	protected ShutdownEvent<Exception> shutdownEvent;

	// 子类共用Logger
	protected Logger log = CommonLoggerFactory.getLogger(getClass());

	protected String tag;

	protected AbstractRabbitMqTransport() {
		// Generally not used
	}

	/**
	 * 
	 * @param tag
	 * @param moduleType
	 * @param rmqConnection
	 */
	protected AbstractRabbitMqTransport(String tag, @Nonnull String moduleType, @Nonnull RmqConnection rmqConnection) {
		this.tag = isNullOrEmpty(tag) ? moduleType + "-" + Instant.now() : tag;
		this.rmqConnection = Assertor.nonNull(rmqConnection, "rmqConnection");
		this.shutdownEvent = rmqConnection.shutdownEvent();
	}

	protected void createConnection() {
		log.info("Create connection started");
		if (connectionFactory == null) {
			connectionFactory = rmqConnection.createConnectionFactory();
		}
		try {
			connection = connectionFactory.newConnection();
			connection.setId(tag + "-" + System.nanoTime());
			log.info("Call method connectionFactory.newConnection() finished, tag -> {}, connection id -> {}", tag,
					connection.getId());
			connection.addShutdownListener(signal -> {
				// 输出信号到控制台
				log.info("Shutdown listener message -> {}", signal.getMessage());
				if (isNormalShutdown(signal))
					log.info("connection id -> {}, is normal shutdown", connection.getId());
				else {
					log.error("connection id -> {}, not normal shutdown", connection.getId());
					// 如果回调函数不为null, 则执行此函数
					if (shutdownEvent != null)
						shutdownEvent.accept(signal);
				}
			});
			channel = connection.createChannel();
			log.info("Call method connection.createChannel() finished, connection id -> {}, channel number -> {}",
					connection.getId(), channel.getChannelNumber());
			log.info("Create connection finished");
		} catch (IOException e) {
			log.error("Method createConnection() throw IOException -> {}", e.getMessage(), e);
		} catch (TimeoutException e) {
			log.error("Method createConnection() throw TimeoutException -> {}", e.getMessage(), e);
		}
	}

	@Override
	public boolean isConnected() {
		return connection != null && connection.isOpen() && channel != null && channel.isOpen();
	}

	protected boolean closeAndReconnection() {
		log.info("Call method closeAndReconnection()");
		closeConnection();
		sleep(rmqConnection.recoveryInterval() / 2);
		createConnection();
		sleep(rmqConnection.recoveryInterval() / 2);
		return isConnected();
	}

	private boolean isNormalShutdown(ShutdownSignalException sig) {
		Method reason = sig.getReason();
		if (reason instanceof AMQP.Channel.Close) {
			AMQP.Channel.Close channelClose = (AMQP.Channel.Close) reason;
			return channelClose.getReplyCode() == AMQP.REPLY_SUCCESS
					&& StringUtil.isEquals(channelClose.getReplyText(), "OK");
		} else if (reason instanceof AMQP.Connection.Close) {
			AMQP.Connection.Close connectionClose = (AMQP.Connection.Close) reason;
			return connectionClose.getReplyCode() == AMQP.REPLY_SUCCESS
					&& StringUtil.isEquals(connectionClose.getReplyText(), "OK");
		} else
			return false;
	}

	protected void closeConnection() {
		log.info("Call method closeConnection()");
		try {
			if (channel != null && channel.isOpen()) {
				channel.close();
				log.info("Channel is closeed!");
			}
			if (connection != null && connection.isOpen()) {
				connection.close();
				log.info("Connection is closeed!");
			}
		} catch (IOException e) {
			log.error("Method closeConnection() throw IOException -> {}", e.getMessage(), e);
		} catch (TimeoutException e) {
			log.error("Method closeConnection() throw TimeoutException -> {}", e.getMessage(), e);
		}
	}

	@Override
	public boolean destroy() {
		log.info("Call method destroy() from AbstractRabbitMqTransport tag==[{}]", tag);
		closeConnection();
		return true;
	}

	@Override
	public void close() throws IOException {
		destroy();
	}

	@Override
	public String name() {
		return tag;
	}

}

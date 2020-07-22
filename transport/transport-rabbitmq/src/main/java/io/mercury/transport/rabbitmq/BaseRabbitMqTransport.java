package io.mercury.transport.rabbitmq;

import static io.mercury.common.thread.Threads.sleep;
import static io.mercury.common.util.StringUtil.isNullOrEmpty;

import java.io.Closeable;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeoutException;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Method;
import com.rabbitmq.client.ShutdownSignalException;

import io.mercury.common.datetime.TimeZone;
import io.mercury.common.functional.ShutdownEvent;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.util.Assertor;
import io.mercury.common.util.StringUtil;
import io.mercury.transport.core.TransportModule;
import io.mercury.transport.rabbitmq.configurator.RmqConnection;

public abstract class BaseRabbitMqTransport implements TransportModule, Closeable {

	private static final Logger log = CommonLoggerFactory.getLogger(BaseRabbitMqTransport.class);

	// 连接RabbitMQ Server使用的组件
	protected ConnectionFactory connectionFactory;
	protected volatile Connection connection;
	protected volatile Channel channel;

	// 存储配置信息对象
	protected RmqConnection rmqConnection;

	// 停机事件, 在监听到ShutdownSignalException时调用
	protected ShutdownEvent<Exception> shutdownEvent;

	// 组件标记
	protected final String tag;

	/**
	 * 
	 * @param tag
	 */
	protected BaseRabbitMqTransport(String tag) {
		// Generally not used
		this.tag = tag;
	}

	/**
	 * 
	 * @param tag
	 * @param moduleType
	 * @param rmqConnection
	 */
	protected BaseRabbitMqTransport(String tag, @Nonnull String moduleType, @Nonnull RmqConnection rmqConnection) {
		Assertor.nonNull(rmqConnection, "rmqConnection");
		this.tag = isNullOrEmpty(tag) ? moduleType + "-" + ZonedDateTime.now(TimeZone.SYS_DEFAULT) : tag;
		this.rmqConnection = rmqConnection;
		this.shutdownEvent = rmqConnection.shutdownEvent();
	}

	protected void createConnection() {
		log.info("Create connection started");
		if (connectionFactory == null) {
			connectionFactory = rmqConnection.createConnectionFactory();
		}
		try {
			connection = connectionFactory.newConnection();
			connection.setId(tag + "$[" + System.currentTimeMillis() + "]");
			log.info("Call function -> [connectionFactory.newConnection()] finished, tag -> {}, connection id -> {}",
					tag, connection.getId());
			connection.addShutdownListener(this::handleShutdownSignal);
			channel = connection.createChannel();
			log.info(
					"Call function -> [connection.createChannel()] finished, connection id -> {}, channel number -> {}",
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

	protected void handleShutdownSignal(ShutdownSignalException sig) {
		// 输出关闭信号
		log.info("Shutdown listener message -> {}", sig.getMessage());
		if (isNormalShutdown(sig)) {
			log.info("connection id -> {}, normal shutdown", connection.getId());
		} else {
			log.error("connection id -> {}, not normal shutdown", connection.getId());
			// 如果回调函数不为null, 则执行此函数
			if (shutdownEvent != null)
				shutdownEvent.accept(sig);
		}
	}

	protected boolean isNormalShutdown(ShutdownSignalException sig) {
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
				try {
					channel.close();
					log.info("Channel is closed!");
				} catch (IOException e) {
					log.error("Function -> [channel.close()] throw IOException : [{}]", e.getMessage());
					throw e;
				} catch (TimeoutException e) {
					log.error("Function -> [channel.close()] throw TimeoutException : [{}]", e.getMessage());
					throw e;
				}
			}
			if (connection != null && connection.isOpen()) {
				try {
					connection.close();
					log.info("Connection is closed!");
				} catch (IOException e) {
					log.error("Function -> [connection.close()] throw TimeoutException : [{}]", e.getMessage());
					throw e;
				}
			}
		} catch (IOException e) {
			log.error("Catch IOException...", e);
		} catch (TimeoutException e) {
			log.error("Catch TimeoutException...", e);
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

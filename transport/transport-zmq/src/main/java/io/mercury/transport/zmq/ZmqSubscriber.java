package io.mercury.transport.zmq;

import java.io.IOException;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.transport.api.Subscriber;
import io.mercury.transport.configurator.TcpKeepAlive;
import io.mercury.transport.configurator.Topics;
import io.mercury.transport.zmq.exception.ZmqConnectionException;

/**
 * 
 * @author yellow013
 *
 */
public final class ZmqSubscriber extends ZmqTransport implements Subscriber {

	/**
	 * topics
	 */
	private final Topics topics;

	/**
	 * 订阅消息消费者
	 */
	private final BiConsumer<byte[], byte[]> consumer;

	private static final Logger log = CommonLoggerFactory.getLogger(ZmqSubscriber.class);

	ZmqSubscriber(@Nonnull ZmqConfigurator cfg, @Nonnull Topics topics, BiConsumer<byte[], byte[]> consumer)
			throws ZmqConnectionException {
		super(cfg);
		this.topics = topics;
		this.consumer = consumer;
		var addr = cfg.getAddr();
		if (socket.connect(addr))
			log.info("connected addr -> {}", addr);
		else {
			log.error("unable to connect addr -> {}", addr);
			throw new ZmqConnectionException(addr);
		}
		setTcpKeepAlive(cfg.getTcpKeepAlive() == null
				// 使用默认TcpKeepAlive配置
				? TcpKeepAlive.enable().setKeepAliveCount(10).setKeepAliveIdle(30).setKeepAliveInterval(30)
				: cfg.getTcpKeepAlive());
		topics.each(topic -> socket.subscribe(topic.getBytes(ZMQ.CHARSET)));
		this.name = "ZMQ::SUB$" + addr + "/" + topics;
		newStartTime();
	}

	public Topics getTopics() {
		return topics;
	}

	@Override
	protected SocketType getSocketType() {
		return SocketType.SUB;
	}

	@Override
	public void subscribe() {
		while (isRunning.get()) {
			var topic = socket.recv();
			log.debug("received topic, length: {}", topic.length);
			var msg = socket.recv();
			log.debug("received msg, length: {}", topic.length);
			consumer.accept(topic, msg);
		}
		log.info("ZmqSubscriber -> [{}] already closed", name);
	}

	@Override
	public void reconnect() {
		throw new UnsupportedOperationException("ZmqSubscriber unsupport reconnect");
	}

	@Override
	public void run() {
		subscribe();
	}

	public static void main(String[] args) {
		try (ZmqSubscriber subscriber = ZmqConfigurator.tcp("127.0.0.1", 13001).ioThreads(2).newSubscriber(
				Topics.with("test"),
				(topic, msg) -> System.out.println(new String(topic) + " -> " + new String(msg)))) {
			subscriber.subscribe();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

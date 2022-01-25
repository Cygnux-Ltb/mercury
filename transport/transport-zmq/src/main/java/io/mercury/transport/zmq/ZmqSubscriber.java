package io.mercury.transport.zmq;

import java.io.IOException;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import io.mercury.common.lang.Assertor;
import io.mercury.common.log.Log4j2LoggerFactory;
import io.mercury.transport.api.Subscriber;
import io.mercury.transport.attr.TcpKeepAlive;
import io.mercury.transport.attr.Topics;
import io.mercury.transport.zmq.exception.ZmqConnectionException;

/**
 * 
 * @author yellow013
 *
 */
public final class ZmqSubscriber extends ZmqTransport implements Subscriber {

	private static final Logger log = Log4j2LoggerFactory.getLogger(ZmqSubscriber.class);

	// topics
	private final Topics topics;

	// 订阅消息消费者
	private final BiConsumer<byte[], byte[]> consumer;

	ZmqSubscriber(@Nonnull ZmqConfigurator cfg, @Nonnull Topics topics, @Nonnull BiConsumer<byte[], byte[]> consumer)
			throws ZmqConnectionException {
		super(cfg);
		Assertor.nonNull(topics, "topics");
		Assertor.nonNull(consumer, "consumer");
		this.topics = topics;
		this.consumer = consumer;
		String addr = cfg.getAddr();
		if (socket.connect(addr))
			log.info("ZmqSubscriber connected addr -> {}", addr);
		else {
			log.error("ZmqSubscriber unable to connect addr -> {}", addr);
			throw new ZmqConnectionException(addr);
		}
		setTcpKeepAlive(cfg.getTcpKeepAlive() == null
				// 如果TcpKeepAlive为空, 使用默认TcpKeepAlive配置
				? TcpKeepAlive.enable().setKeepAliveCount(10).setKeepAliveIdle(30).setKeepAliveInterval(30)
				: cfg.getTcpKeepAlive());
		topics.each(topic -> socket.subscribe(topic.getBytes(ZMQ.CHARSET)));
		this.name = "zsub$" + addr + "/" + topics;
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
	public ZmqTransportType getTransportType() {
		return ZmqTransportType.ZmqSubscriber;
	}

	@Override
	public void subscribe() {
		while (isRunning.get()) {
			byte[] topic = socket.recv();
			log.debug("received topic, length: {}", topic.length);
			byte[] msg = socket.recv();
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

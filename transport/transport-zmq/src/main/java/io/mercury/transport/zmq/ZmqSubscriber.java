package io.mercury.transport.zmq;

import java.io.IOException;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.transport.api.Receiver;
import io.mercury.transport.api.Subscriber;
import io.mercury.transport.configurator.TcpKeepAlive;
import io.mercury.transport.configurator.Topics;
import io.mercury.transport.exception.ReceiverStartException;
import io.mercury.transport.zmq.exception.ZmqConnectionException;

/**
 * 
 * @author yellow013
 *
 */
public final class ZmqSubscriber extends ZmqTransport implements Receiver, Subscriber {

	/**
	 * topics
	 */
	private final Topics topics;

	/**
	 * 订阅消息消费者
	 */
	private final BiConsumer<byte[], byte[]> consumer;

	private static final Logger log = CommonLoggerFactory.getLogger(ZmqSubscriber.class);

	ZmqSubscriber(@Nonnull Topics topics, ZmqConfigurator cfg, BiConsumer<byte[], byte[]> consumer)
			throws ZmqConnectionException {
		super(cfg);
		this.topics = topics;
		this.consumer = consumer;
		String addr = cfg.getAddr();
		if (zSocket.connect(addr))
			log.info("connected addr -> {}", addr);
		else {
			log.error("unable to connect addr -> {}", addr);
			throw new ZmqConnectionException(addr);
		}
		setTcpKeepAlive(cfg.getTcpKeepAlive() == null
				// 使用默认TcpKeepAlive配置
				? TcpKeepAlive.enable().setKeepAliveCount(10).setKeepAliveIdle(30).setKeepAliveInterval(30)
				: cfg.getTcpKeepAlive());
		topics.each(topic -> zSocket.subscribe(topic.getBytes(ZMQ.CHARSET)));
		newStartTime();
		this.name = "ZMQ::SUB$" + addr + "/" + topics;
	}

	public Topics getTopics() {
		return topics;
	}

	@Override
	protected SocketType getSocketType() {
		return SocketType.SUB;
	}

	@Override
	public void receive() throws ReceiverStartException {
		while (isRunning.get()) {
			byte[] topic = zSocket.recv();
			log.debug("received topic bytes, length: {}", topic.length);
			byte[] msg = zSocket.recv();
			log.debug("received msg bytes, length: {}", topic.length);
			consumer.accept(topic, msg);
		}
		log.warn("ZmqSubscriber -> [{}] already closed", name);
	}

	@Override
	public void subscribe() {
		while (isRunning.get()) {
			byte[] topic = zSocket.recv();
			log.debug("received topic bytes, length: {}", topic.length);
			byte[] msg = zSocket.recv();
			log.debug("received msg bytes, length: {}", topic.length);
			consumer.accept(topic, msg);
		}
		log.warn("ZmqSubscriber -> [{}] already closed", name);
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
		try (ZmqSubscriber subscriber = ZmqConfigurator.tcp("127.0.0.1", 13001).ioThreads(2).createSubscriber(
				Topics.with("test"),
				(topic, msg) -> System.out.println(new String(topic) + " -> " + new String(msg)))) {
			subscriber.subscribe();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

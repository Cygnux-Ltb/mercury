package io.mercury.transport.zmq;

import static io.mercury.transport.configurator.TcpKeepAliveOption.KeepAlive.Enable;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.zeromq.SocketType;

import io.mercury.common.character.Charsets;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.util.Assertor;
import io.mercury.common.util.StringUtil;
import io.mercury.transport.api.Subscriber;
import io.mercury.transport.configurator.TcpKeepAliveOption;
import io.mercury.transport.zmq.cfg.ZmqAddress;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 
 * @author yellow013
 *
 */
public final class ZmqSubscriber extends ZmqTransport0 implements Subscriber, Closeable, Runnable {

	@Getter
	private final ZmqAddress addr;

	/**
	 * topics
	 */
	@Getter
	private final String[] topics;

	@Getter
	private final String name;

	/**
	 * 订阅消息消费者
	 */
	private final BiConsumer<byte[], byte[]> consumer;

	private static final Logger log = CommonLoggerFactory.getLogger(ZmqSubscriber.class);

	private ZmqSubscriber(@Nonnull Builder builder) {
		super(builder.ioThreads);
		Assertor.nonNull(builder.consumer, "consumer");
		this.addr = builder.addr;
		this.consumer = builder.consumer;
		this.topics = builder.topics;
		if (!initSocket(SocketType.SUB).connect(addr.getAddr())) {
			log.info("");
		}
		setTcpKeepAlive(builder.tcpKeepAliveOption);
		for (String topic : topics) {
			socket.subscribe(topic.getBytes(Charsets.UTF8));
		}
		this.name = "ZMQ::SUB$" + builder.addr.getAddr() + StringUtil.toString(topics);
	}

	public final static Builder newBuilder(ZmqAddress addr) {
		return new Builder(addr);
	}

	/**
	 * 
	 * @author yellow013
	 *
	 */
	@RequiredArgsConstructor
	@Accessors(chain = true)
	public static class Builder {

		private final ZmqAddress addr;

		private String[] topics = new String[] { "" };

		@Setter
		private int ioThreads = 1;

		@Setter
		private TcpKeepAliveOption tcpKeepAliveOption = new TcpKeepAliveOption().setKeepAlive(Enable)
				.setKeepAliveCount(10).setKeepAliveIdle(30).setKeepAliveInterval(30);

		private BiConsumer<byte[], byte[]> consumer;

		public Builder setTopics(String... topics) {
			this.topics = topics;
			return this;
		}

		public ZmqSubscriber build(BiConsumer<byte[], byte[]> consumer) {
			this.consumer = consumer;
			return new ZmqSubscriber(this);
		}
	}

	@Override
	public void subscribe() {
		while (isRunning.get()) {
			byte[] topic = socket.recv();
			log.debug("received topic bytes, length: {}", topic.length);
			byte[] msg = socket.recv();
			log.debug("received msg bytes, length: {}", topic.length);
			consumer.accept(topic, msg);
		}
		log.warn("ZmqSubscriber -> {} has exited", name);
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

		try (ZmqSubscriber subscriber = ZmqSubscriber.newBuilder(ZmqAddress.tcp("127.0.0.1", 13001))
				.setTopics("command").setIoThreads(2)
				.build((topic, msg) -> System.out.println(new String(topic) + "->" + new String(msg)))) {
			subscriber.subscribe();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

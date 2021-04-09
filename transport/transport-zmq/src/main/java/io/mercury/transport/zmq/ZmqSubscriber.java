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
import io.mercury.transport.zmq.exception.ZmqConnectionException;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 
 * @author yellow013
 *
 */
public final class ZmqSubscriber extends ZmqTransport implements Subscriber, Closeable, Runnable {

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

	private ZmqSubscriber(@Nonnull Builder builder) throws ZmqConnectionException {
		super(builder.addr, builder.ioThreads);
		Assertor.nonNull(builder.consumer, "consumer");
		this.consumer = builder.consumer;
		this.topics = builder.topics;
		if (socket.connect(addr.getAddr())) {
			log.info("connected addr -> {}", addr);
		} else {
			log.error("unable to connect addr -> {}", addr);
			throw new ZmqConnectionException(addr);
		}
		setTcpKeepAlive(builder.tcpKeepAliveOption);
		for (String topic : topics) {
			socket.subscribe(topic.getBytes(Charsets.UTF8));
		}
		this.name = "Zmq::Sub$" + builder.addr.getAddr() + "/" + StringUtil.toString(topics);
	}

	/**
	 * 创建TCP协议连接
	 * 
	 * @param port
	 * @return
	 */
	public final static Builder tcp(int port) {
		return new Builder(ZmqAddress.tcp(port));
	}

	/**
	 * 创建TCP协议连接
	 * 
	 * @param addr
	 * @param port
	 * @return
	 */
	public final static Builder tcp(String addr, int port) {
		return new Builder(ZmqAddress.tcp(addr, port));
	}

	/**
	 * 创建IPC协议连接
	 * 
	 * @param addr
	 * @return
	 */
	public final static Builder ipc(String addr) {
		return new Builder(ZmqAddress.ipc(addr));
	}

	@Override
	protected SocketType getSocketType() {
		return SocketType.SUB;
	}

	/**
	 * 
	 * @author yellow013
	 *
	 */
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

		private Builder(ZmqAddress addr) {
			this.addr = addr;
		}

		public Builder setTopics(String... topics) {
			this.topics = topics;
			return this;
		}

		/**
		 * 在构建时定义如何处理接收到的Topic和Content
		 * 
		 * @param consumer
		 * @return
		 */
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

		try (ZmqSubscriber subscriber = ZmqSubscriber.tcp("127.0.0.1", 13001).setTopics("command").setIoThreads(2)
				.build((topic, msg) -> System.out.println(new String(topic) + "->" + new String(msg)))) {
			subscriber.subscribe();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

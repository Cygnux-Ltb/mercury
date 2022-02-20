package io.mercury.transport.zmq;

import static io.mercury.common.sys.CurrentRuntime.availableProcessors;
import static io.mercury.transport.zmq.ZmqConfigOption.Addr;
import static io.mercury.transport.zmq.ZmqConfigOption.IoThreads;
import static io.mercury.transport.zmq.ZmqConfigOption.Port;
import static io.mercury.transport.zmq.ZmqConfigOption.Protocol;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.zeromq.ZMQ;

import com.typesafe.config.Config;

import io.mercury.common.annotation.OnlyOverrideEquals;
import io.mercury.common.config.ConfigWrapper;
import io.mercury.common.lang.Assertor;
import io.mercury.common.log.Log4j2LoggerFactory;
import io.mercury.common.net.IpAddressIllegalException;
import io.mercury.common.net.IpAddressValidator;
import io.mercury.common.serialization.BytesSerializer;
import io.mercury.common.serialization.JsonDeserializable;
import io.mercury.common.serialization.JsonSerializable;
import io.mercury.serialization.json.JsonParser;
import io.mercury.serialization.json.JsonWrapper;
import io.mercury.transport.TransportConfigurator;
import io.mercury.transport.attr.TcpKeepAlive;
import io.mercury.transport.attr.Topics;

@OnlyOverrideEquals
public final class ZmqConfigurator
		implements TransportConfigurator, JsonSerializable, JsonDeserializable<ZmqConfigurator> {

	private static final Logger log = Log4j2LoggerFactory.getLogger(ZmqConfigurator.class);

	private final ZmqAddr addr;

	private int ioThreads = 1;

	private int highWaterMark = 8192;

	private TcpKeepAlive tcpKeepAlive = null;

	/**
	 * 
	 * @param addr
	 */
	private ZmqConfigurator(ZmqAddr addr) {
		this.addr = addr;
	}

	private ZmqConfigurator(ZmqProtocol protocol, String addr) {
		this(new ZmqAddr(protocol, addr));
	}

	public ZmqAddr getAddr() {
		return addr;
	}

	public int getIoThreads() {
		return ioThreads;
	}

	public int getHighWaterMark() {
		return highWaterMark;
	}

	public TcpKeepAlive getTcpKeepAlive() {
		return tcpKeepAlive;
	}

	@Override
	public String getConnectionInfo() {
		return addr.toString();
	}

	/**
	 * 
	 * @param config
	 * @return
	 */
	public static ZmqConfigurator withConfig(@Nonnull Config config) {
		return withConfig("", config);
	}

	/**
	 * 
	 * @param config
	 * @param module
	 * @return
	 */
	public static ZmqConfigurator withConfig(String module, @Nonnull Config config) {
		Assertor.nonNull(config, "config");
		ConfigWrapper<ZmqConfigOption> delegate = new ConfigWrapper<>(module, config);
		ZmqProtocol protocol = ZmqProtocol.of(delegate.getStringOrThrows(Protocol));
		ZmqConfigurator conf = null;
		switch (protocol) {
		case TCP:
			int port = delegate.getIntOrThrows(Port);
			if (delegate.hasOption(Addr)) {
				String tcpAddr = delegate.getStringOrThrows(Addr);
				Assertor.isValid(tcpAddr, IpAddressValidator::isIpAddress, new IpAddressIllegalException(tcpAddr));
				conf = ZmqConfigurator.tcp(tcpAddr, port);
			} else {
				// 没有addr配置项, 使用本地地址
				conf = ZmqConfigurator.tcp(port);
			}
			break;
		case IPC:
		case INPROC:
			// 使用ipc或inproc协议
			String localAddr = delegate.getStringOrThrows(Addr);
			conf = new ZmqConfigurator(protocol, localAddr);
		default:
			break;
		}
		if (delegate.hasOption(IoThreads))
			conf.ioThreads(delegate.getInt(IoThreads));
		log.info("created ZmqConfigurator object -> {}", conf);
		return conf;
	}

	/**
	 * 创建TCP协议连接
	 * 
	 * @param port
	 * @return
	 */
	public static ZmqConfigurator tcp(int port) {
		return tcp("*", port);
	}

	/**
	 * 创建TCP协议连接
	 * 
	 * @param addr
	 * @param port
	 * @return
	 */
	public static ZmqConfigurator tcp(@Nonnull String addr, int port) {
		return new ZmqConfigurator(ZmqAddr.tcp(addr, port));
	}

	/**
	 * 使用[ipc]协议连接, 用于进程间通信
	 * 
	 * @param addr
	 * @return
	 */
	public static ZmqConfigurator ipc(@Nonnull String addr) {
		return new ZmqConfigurator(ZmqAddr.ipc(addr));
	}

	/**
	 * 使用[inproc]协议连接, 用于线程间通信
	 * 
	 * @param addr
	 * @return
	 */
	public static ZmqConfigurator inproc(@Nonnull String addr) {
		return new ZmqConfigurator(ZmqAddr.inproc(addr));
	}

	/**
	 * 
	 * @param ioThreads
	 * @return
	 */
	public ZmqConfigurator ioThreads(int ioThreads) {
		Assertor.greaterThan(ioThreads, 0, "ioThreads");
		this.ioThreads = ioThreads < availableProcessors() ? ioThreads : availableProcessors();
		return this;
	}

	/**
	 * Set high water mark with socket
	 * 
	 * @param highWaterMark
	 * @return
	 */
	public ZmqConfigurator setHighWaterMark(int highWaterMark) {
		this.highWaterMark = highWaterMark;
		return this;
	}

	/**
	 * 
	 * @param tcpKeepAlive
	 * @return
	 */
	public ZmqConfigurator tcpKeepAlive(@Nonnull TcpKeepAlive tcpKeepAlive) {
		Assertor.nonNull(tcpKeepAlive, "tcpKeepAlive");
		this.tcpKeepAlive = tcpKeepAlive;
		return this;
	}

	/**
	 * 
	 * @return ZmqSender<byte[]>
	 */
	public ZmqSender<byte[]> newSender() {
		return newSender(bytes -> bytes);
	}

	/**
	 * 
	 * @param <T>
	 * @param ser
	 * @return ZmqSender<T>
	 */
	public <T> ZmqSender<T> newSender(@Nonnull BytesSerializer<T> ser) {
		Assertor.nonNull(ser, "ser");
		return new ZmqSender<>(this, ser);
	}

	/**
	 * 
	 * @param handler
	 * @return ZmqReceiver
	 */
	public ZmqReceiver newReceiver(@Nonnull Function<byte[], byte[]> handler) {
		Assertor.nonNull(handler, "handler");
		return new ZmqReceiver(this, handler);
	}

	/**
	 * 
	 * @param consumer
	 * @return ZmqSubscriber
	 */
	public ZmqSubscriber newSubscriber(@Nonnull BiConsumer<byte[], byte[]> consumer) {
		return newSubscriber(Topics.with(""), consumer);
	}

	/**
	 * 
	 * @param topics
	 * @param consumer
	 * @return ZmqSubscriber
	 */
	public ZmqSubscriber newSubscriber(@Nonnull Topics topics, @Nonnull BiConsumer<byte[], byte[]> consumer) {
		Assertor.nonNull(topics, "topics");
		Assertor.nonNull(consumer, "consumer");
		return new ZmqSubscriber(this, topics, consumer);
	}

	/**
	 * 
	 * @return ZmqPublisher
	 */
	public ZmqPublisher<byte[]> newPublisherWithBinary() {
		return newPublisherWithBinary("");
	}

	/**
	 * 
	 * @param topic
	 * @return ZmqPublisher
	 */
	public ZmqPublisher<byte[]> newPublisherWithBinary(@Nonnull String topic) {
		return newPublisher(topic, bytes -> bytes);
	}

	/**
	 * 
	 * @return ZmqPublisher
	 */
	public ZmqPublisher<String> newPublisherWithString() {
		return newPublisherWithString("", ZMQ.CHARSET);
	}

	/**
	 * 
	 * @param topic
	 * @return ZmqPublisher
	 */
	public ZmqPublisher<String> newPublisherWithString(@Nonnull String topic) {
		return newPublisherWithString(topic, ZMQ.CHARSET);
	}

	/**
	 * 
	 * @param encode
	 * @return ZmqPublisher
	 */
	public ZmqPublisher<String> newPublisherWithString(@Nonnull Charset encode) {
		return newPublisherWithString("", encode);
	}

	/**
	 * 
	 * @param topic
	 * @param encode
	 * @return ZmqPublisher
	 */
	public ZmqPublisher<String> newPublisherWithString(@Nonnull String topic, @Nonnull Charset encode) {
		Assertor.nonNull(encode, "encode");
		return newPublisher(topic, str -> str.getBytes(encode));
	}

	/**
	 * 
	 * @param <T>
	 * @param ser
	 * @return ZmqPublisher
	 */
	public <T> ZmqPublisher<T> newPublisher(@Nonnull BytesSerializer<T> ser) {
		return newPublisher("", ser);
	}

	/**
	 * 
	 * @param <T>
	 * @param topic
	 * @param ser
	 * @return ZmqPublisher
	 */
	public <T> ZmqPublisher<T> newPublisher(@Nonnull String topic, @Nonnull BytesSerializer<T> ser) {
		Assertor.nonNull(topic, "topic");
		Assertor.nonNull(ser, "ser");
		return new ZmqPublisher<>(this, topic, ser);
	}

	private transient String toString;

	@Override
	public String toString() {
		if (toString == null)
			this.toString = JsonWrapper.toJson(this);
		return toString;
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		if (obj == null || !(obj instanceof ZmqConfigurator))
			return false;
		else {
			ZmqConfigurator o = (ZmqConfigurator) obj;
			if (!this.addr.equals(o.getAddr()))
				return false;
			if (this.ioThreads != o.getIoThreads())
				return false;
			if (!this.tcpKeepAlive.equals(o.getTcpKeepAlive()))
				return false;
			return true;
		}
	}

	@Override
	public String getConfigInfo() {
		return toString();
	}

	public static String getZmqVersion() {
		return ZMQ.getVersionString();
	}

	@Nonnull
	@Override
	public String toJson() {
		return JsonWrapper.toJsonHasNulls(this);
	}

	@Nonnull
	@Override
	public ZmqConfigurator fromJson(@Nonnull String json) {
		Map<String, Object> map = JsonParser.toMap(json);
		ZmqProtocol protocol = ZmqProtocol.of((String) map.get("protocol"));
		String addr = (String) map.get("addr");
		int ioThreads = (int) map.get("ioThreads");
		TcpKeepAlive tcpKeepAlive = JsonParser.toObject((String) map.get("tcpKeepAlive"), TcpKeepAlive.class);
		return new ZmqConfigurator(protocol, addr).ioThreads(ioThreads).tcpKeepAlive(tcpKeepAlive);
	}

	/**
	 * 当前支持的协议类型
	 * 
	 * @author yellow013
	 */
	public static enum ZmqProtocol {

		TCP("tcp"), IPC("ipc"), INPROC("inproc");

		private final String name;
		private final String prefix;

		ZmqProtocol(String name) {
			this.name = name;
			this.prefix = name + "://";
		}

		public String fixAddr(String addr) {
			if (!addr.startsWith(prefix))
				return prefix + addr;
			return addr;
		}

		@Override
		public String toString() {
			return name();
		}

		public static ZmqProtocol of(String name) {
			for (ZmqProtocol protocol : ZmqProtocol.values()) {
				if (protocol.name.equalsIgnoreCase(name))
					return protocol;
				if (protocol.prefix.equalsIgnoreCase(name))
					return protocol;
			}
			throw new IllegalArgumentException("Unsupported protocol type -> " + name);
		}

	}

	/**
	 * 
	 * @author yellow013
	 */
	public static class ZmqAddr {

		private final ZmqProtocol protocol;

		private final String addr;

		private final String completeInfo;

		public ZmqAddr(ZmqProtocol protocol, String addr) {
			this.protocol = protocol;
			this.addr = addr;
			this.completeInfo = protocol.fixAddr(addr);
		}

		/**
		 * 创建TCP协议连接
		 * 
		 * @param port
		 * @return
		 */
		public static ZmqAddr tcp(int port) {
			return tcp("*", port);
		}

		/**
		 * 创建TCP协议连接
		 * 
		 * @param addr
		 * @param port
		 * @return
		 */
		public static ZmqAddr tcp(@Nonnull String addr, int port) {
			Assertor.nonEmpty(addr, "addr");
			Assertor.atWithinRange(port, 4096, 65536, "port");
			if (!addr.equals("*"))
				IpAddressValidator.assertIpAddress(addr);
			return new ZmqAddr(ZmqProtocol.TCP, addr + ":" + port);
		}

		/**
		 * 使用[ipc]协议连接, 用于进程间通信
		 * 
		 * @param addr
		 * @return
		 */
		public static ZmqAddr ipc(@Nonnull String addr) {
			Assertor.nonEmpty(addr, "addr");
			return new ZmqAddr(ZmqProtocol.IPC, addr);
		}

		/**
		 * 使用[inproc]协议连接, 用于线程间通信
		 * 
		 * @param addr
		 * @return
		 */
		public static ZmqAddr inproc(@Nonnull String addr) {
			Assertor.nonEmpty(addr, "addr");
			return new ZmqAddr(ZmqProtocol.INPROC, addr);
		}

		public ZmqProtocol getProtocol() {
			return protocol;
		}

		public String getAddr() {
			return addr;
		}

		public String getCompleteInfo() {
			return completeInfo;
		}

		@Override
		public String toString() {
			return completeInfo;
		}

	}

	public static void main(String[] args) {
		ZmqConfigurator configurator = ZmqConfigurator.tcp("192.168.1.1", 5551).ioThreads(3)
				.tcpKeepAlive(TcpKeepAlive.withDefault());
		System.out.println(configurator);
		System.out.println(ZmqConfigurator.getZmqVersion());
		System.out.println(IpAddressValidator.isIPv4("192.168.1.1"));
	}

}

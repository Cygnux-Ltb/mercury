package io.mercury.transport.zmq;

import java.nio.charset.Charset;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.zeromq.ZMQ;

import io.mercury.common.annotation.OnlyOverrideEquals;
import io.mercury.common.character.Charsets;
import io.mercury.common.config.Configurator;
import io.mercury.common.serialization.BytesSerializer;
import io.mercury.common.serialization.JsonDeserializable;
import io.mercury.common.util.Assertor;
import io.mercury.serialization.json.JsonWrapper;
import io.mercury.transport.configurator.TcpKeepAlive;
import io.mercury.transport.configurator.Topics;

@OnlyOverrideEquals
public final class ZmqConfigurator implements Configurator, JsonDeserializable<ZmqConfigurator> {

	private final String addr;

	private int ioThreads = 1;

	private TcpKeepAlive tcpKeepAlive;

	private String tag = "";

	private ZmqConfigurator(String addr) {
		this.addr = addr;
	}

	public String getAddr() {
		return addr;
	}

	public int getIoThreads() {
		return ioThreads;
	}

	public TcpKeepAlive getTcpKeepAlive() {
		return tcpKeepAlive;
	}

	public String getTag() {
		return tag;
	}

	/**
	 * 创建TCP协议连接
	 * 
	 * @param addr
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
	public static ZmqConfigurator tcp(String addr, int port) {
		if (port < 2048 || port > 65536)
			throw new IllegalArgumentException("port ");
		if (!addr.startsWith("tcp://")) {
			addr = "tcp://" + addr;
		}
		return new ZmqConfigurator(addr + ":" + port);
	}

	/**
	 * 创建IPC协议连接
	 * 
	 * @param addr
	 * @param port
	 * @return
	 */
	public static ZmqConfigurator ipc(String addr) {
		if (!addr.startsWith("ipc://")) {
			addr = "ipc://" + addr;
		}
		return new ZmqConfigurator(addr);
	}

	public ZmqConfigurator ioThreads(int ioThreads) {
		this.ioThreads = ioThreads;
		return this;
	}

	public ZmqConfigurator tcpKeepAlive(TcpKeepAlive tcpKeepAlive) {
		this.tcpKeepAlive = tcpKeepAlive;
		return this;
	}

	public ZmqConfigurator tag(String tag) {
		this.tag = tag;
		return this;
	}

	/**
	 * 
	 * @param <T>
	 * @param ser
	 * @return ZmqSender
	 */
	public ZmqSender<byte[]> buildSender() {
		return buildSender(bytes -> bytes);
	}

	/**
	 * 
	 * @param <T>
	 * @param ser
	 * @return ZmqSender
	 */
	public <T> ZmqSender<T> buildSender(@Nonnull BytesSerializer<T> ser) {
		Assertor.nonNull(ser, "ser");
		return new ZmqSender<>(this, ser);
	}

	/**
	 * 
	 * @param handler
	 * @return receiver
	 */
	public ZmqReceiver buildReceiver(@Nonnull Function<byte[], byte[]> handler) {
		Assertor.nonNull(handler, "handler");
		return new ZmqReceiver(this, handler);
	}

	/**
	 * 
	 * @param consumer
	 * @return
	 */
	public ZmqSubscriber buildSubscriber(BiConsumer<byte[], byte[]> consumer) {
		return buildSubscriber(Topics.with(""), consumer);
	}

	/**
	 * 
	 * @param topics
	 * @param consumer
	 * @return ZmqSubscriber
	 */
	public ZmqSubscriber buildSubscriber(Topics topics, BiConsumer<byte[], byte[]> consumer) {
		Assertor.nonNull(topics, "topics");
		Assertor.nonNull(consumer, "consumer");
		return new ZmqSubscriber(topics, this, consumer);
	}

	/**
	 * 
	 * @return
	 */
	public ZmqPublisher<byte[]> buildBytesPublisher() {
		return buildBytesPublisher("");
	}

	/**
	 * 
	 * @param topic
	 * @return
	 */
	public ZmqPublisher<byte[]> buildBytesPublisher(String topic) {
		return buildPublisher(topic, bytes -> bytes);
	}

	/**
	 * 
	 * @return
	 */
	public ZmqPublisher<String> buildStringPublisher() {
		return buildStringPublisher("", Charsets.UTF8);
	}

	/**
	 * 
	 * @param topic
	 * @return
	 */
	public ZmqPublisher<String> buildStringPublisher(String topic) {
		return buildStringPublisher(topic, Charsets.UTF8);
	}

	/**
	 * 
	 * @param encode
	 * @return
	 */
	public ZmqPublisher<String> buildStringPublisher(Charset encode) {
		return buildStringPublisher("", encode);
	}

	/**
	 * 
	 * @param topic
	 * @param encode
	 * @return
	 */
	public ZmqPublisher<String> buildStringPublisher(String topic, Charset encode) {
		return buildPublisher(topic, str -> str.getBytes(encode));
	}

	/**
	 * 
	 * @param <T>
	 * @param ser
	 * @return
	 */
	public <T> ZmqPublisher<T> buildPublisher(BytesSerializer<T> ser) {
		return buildPublisher("", ser);
	}

	/**
	 * 
	 * @param <T>
	 * @param topic
	 * @param ser
	 * @return
	 */
	public <T> ZmqPublisher<T> buildPublisher(String topic, BytesSerializer<T> ser) {
		return new ZmqPublisher<>(topic, this, ser);
	}

	private transient String strCache;

	@Override
	public String toString() {
		if (strCache == null)
			this.strCache = JsonWrapper.toJson(this);
		return strCache;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ZmqConfigurator)) {
			return super.equals(obj);
		} else {
			ZmqConfigurator o = (ZmqConfigurator) obj;
			if (!this.addr.equals(o.getAddr()))
				return false;
			if (this.ioThreads != o.getIoThreads())
				return false;
			if (!this.tag.equals(o.getTag()))
				return false;
			if (!this.tcpKeepAlive.equals(o.getTcpKeepAlive()))
				return false;
			return true;
		}
	}

	@Override
	public String getCfgInfo() {
		return toString();
	}

	public static String getZmqVersion() {
		return ZMQ.getVersionString();
	}

	public static void main(String[] args) {
		System.out.println(ZmqConfigurator.tcp("192.168.1.1", 5551).ioThreads(3).tag("TEST")
				.tcpKeepAlive(TcpKeepAlive.withDefault()).toString());

		System.out.println(ZMQ.getFullVersion() + "\n" + ZMQ.getMajorVersion() + "\n" + ZMQ.getMinorVersion() + "\n"
				+ ZMQ.getPatchVersion() + "\n" + ZMQ.getVersionString());
	}

	@Override
	public ZmqConfigurator fromJson(String json) {
		return null;
	}

}

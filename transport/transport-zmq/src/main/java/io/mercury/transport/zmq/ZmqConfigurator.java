package io.mercury.transport.zmq;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.zeromq.ZMQ;

import io.mercury.common.annotation.OnlyOverrideEquals;
import io.mercury.common.config.Configurator;
import io.mercury.common.lang.Assertor;
import io.mercury.common.serialization.BytesSerializer;
import io.mercury.common.serialization.JsonDeserializable;
import io.mercury.serialization.json.JsonParser;
import io.mercury.serialization.json.JsonWrapper;
import io.mercury.transport.configurator.TcpKeepAlive;
import io.mercury.transport.configurator.Topics;

@OnlyOverrideEquals
public final class ZmqConfigurator implements Configurator, JsonDeserializable<ZmqConfigurator> {

	private final String addr;

	private int ioThreads = 1;

	private TcpKeepAlive tcpKeepAlive;

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
	public static ZmqConfigurator tcp(@Nonnull String addr, int port) {
		Assertor.nonEmpty(addr, "addr");
		Assertor.atWithinRange(port, 4096, 65536, "port");
		if (!addr.startsWith("tcp://"))
			addr = "tcp://" + addr;
		return new ZmqConfigurator(addr + ":" + port);
	}

	/**
	 * 使用[ipc]协议连接, 用于进程间通信
	 * 
	 * @param addr
	 * @param port
	 * @return
	 */
	public static ZmqConfigurator ipc(@Nonnull String addr) {
		Assertor.nonEmpty(addr, "addr");
		if (!addr.startsWith("ipc://"))
			addr = "ipc://" + addr;
		return new ZmqConfigurator(addr);
	}

	/**
	 * 使用[inproc]协议连接, 用于线程间通信
	 * 
	 * @param addr
	 * @return
	 */
	public static ZmqConfigurator inproc(@Nonnull String addr) {
		Assertor.nonEmpty(addr, "addr");
		if (!addr.startsWith("inproc://"))
			addr = "inproc://" + addr;
		return new ZmqConfigurator(addr);
	}

	/**
	 * 
	 * @param ioThreads
	 * @return
	 */
	public ZmqConfigurator ioThreads(int ioThreads) {
		Assertor.greaterThan(ioThreads, 1, "ioThreads");
		this.ioThreads = ioThreads;
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
	 * @param <T>
	 * @param ser
	 * @return ZmqSender
	 */
	public ZmqSender<byte[]> newSender() {
		return newSender(bytes -> bytes);
	}

	/**
	 * 
	 * @param <T>
	 * @param serializer
	 * @return ZmqSender
	 */
	public <T> ZmqSender<T> newSender(@Nonnull BytesSerializer<T> serializer) {
		Assertor.nonNull(serializer, "serializer");
		return new ZmqSender<>(this, serializer);
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
	 * @param serializer
	 * @return ZmqPublisher
	 */
	public <T> ZmqPublisher<T> newPublisher(@Nonnull BytesSerializer<T> serializer) {
		return newPublisher("", serializer);
	}

	/**
	 * 
	 * @param <T>
	 * @param topic
	 * @param serializer
	 * @return ZmqPublisher
	 */
	public <T> ZmqPublisher<T> newPublisher(@Nonnull String topic, @Nonnull BytesSerializer<T> serializer) {
		Assertor.nonNull(topic, "topic");
		Assertor.nonNull(serializer, "serializer");
		return new ZmqPublisher<>(this, topic, serializer);
	}

	private transient String cache;

	@Override
	public String toString() {
		if (cache == null)
			this.cache = JsonWrapper.toJson(this);
		return cache;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ZmqConfigurator))
			return super.equals(obj);
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
	public String getCfgInfo() {
		return toString();
	}

	public static String getZmqVersion() {
		return ZMQ.getVersionString();
	}

	public static void main(String[] args) {
		System.out.println(
				ZmqConfigurator.tcp("192.168.1.1", 5551).ioThreads(3).tcpKeepAlive(TcpKeepAlive.withDefault()));
		System.out.println(ZmqConfigurator.getZmqVersion());
	}

	@Override
	public ZmqConfigurator fromJson(String json) {
		Map<String, Object> map = JsonParser.toMap(json);
		var addr = (String) map.get("addr");
		int ioThreads = (int) map.get("ioThreads");
		var tcpKeepAliveJson = (String) map.get("tcpKeepAlive");
		var tcpKeepAlive = JsonParser.toObject(tcpKeepAliveJson, TcpKeepAlive.class);
		return new ZmqConfigurator(addr).ioThreads(ioThreads).tcpKeepAlive(tcpKeepAlive);
	}

}

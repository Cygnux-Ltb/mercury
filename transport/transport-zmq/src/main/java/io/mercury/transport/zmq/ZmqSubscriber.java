package io.mercury.transport.zmq;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import io.mercury.common.character.Charsets;
import io.mercury.common.util.Assertor;
import io.mercury.transport.core.api.Subscriber;
import io.mercury.transport.zmq.configurator.ZmqConfigurator;

public class ZmqSubscriber implements Subscriber, Closeable {

	private ZContext zCtx;
	private ZMQ.Socket zSocket;

	private String name;

	private Consumer<byte[]> consumer;
	private ZmqConfigurator configurator;

	private AtomicBoolean isRun = new AtomicBoolean(true);

	public ZmqSubscriber(@Nonnull ZmqConfigurator configurator, @Nonnull Consumer<byte[]> consumer) {
		Assertor.nonNull(configurator, "configurator");
		Assertor.nonNull(consumer, "consumer");
		this.configurator = configurator;
		this.consumer = consumer;
		init();
	}

	private void init() {
		this.zCtx = new ZContext(configurator.ioThreads());
		this.zSocket = zCtx.createSocket(SocketType.SUB);
		this.zSocket.connect(configurator.getHost());
		this.zSocket.subscribe(configurator.topic().getBytes());
		this.zSocket.setTCPKeepAlive(1);
		this.zSocket.setTCPKeepAliveCount(10);
		this.zSocket.setTCPKeepAliveIdle(15);
		this.zSocket.setTCPKeepAliveInterval(15);
		this.name = "ZMQ.SUB$" + configurator.getHost() + "::" + configurator.topic();
	}

	@Override
	public void subscribe() {
		while (isRun.get()) {
			zSocket.recv();
			byte[] msgBytes = zSocket.recv();
			consumer.accept(msgBytes);
		}
	}

	@Override
	public boolean destroy() {
		this.isRun.set(false);
		zSocket.close();
		zCtx.close();
		return zCtx.isClosed();
	}

	@Override
	public String name() {
		return name;
	}

	public static void main(String[] args) {

		// JeroMqConfigurator configurator =
		// JeroMqConfigurator.builder().setHost("tcp://127.0.0.1:10001").setIoThreads(2).setTopic("").build();

		ZmqConfigurator configurator = ZmqConfigurator.builder().setHost("tcp://127.0.0.1:13001").setTopic("command")
				.setIoThreads(2).build();

		try (ZmqSubscriber jeroMQSubscriber = new ZmqSubscriber(configurator,
				(byte[] byteMsg) -> System.out.println(new String(byteMsg, Charsets.UTF8)))) {
			jeroMQSubscriber.subscribe();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isConnected() {
		return !zCtx.isClosed();
	}

	@Override
	public void close() throws IOException {
		destroy();
	}

	@Override
	public void reconnect() {
		// TODO Auto-generated method stub

	}

}

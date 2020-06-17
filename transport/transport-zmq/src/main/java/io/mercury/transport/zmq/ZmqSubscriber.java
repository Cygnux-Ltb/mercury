package io.mercury.transport.zmq;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import io.mercury.common.character.Charsets;
import io.mercury.transport.core.api.Subscriber;
import io.mercury.transport.zmq.configurator.ZmqConfigurator;

public class ZmqSubscriber implements Subscriber, Closeable {

	private ZContext zCtx;
	private ZMQ.Socket zSocket;

	private String subscriberName;

	private Consumer<byte[]> callback;
	private ZmqConfigurator configurator;

	private AtomicBoolean isRun = new AtomicBoolean(true);

	public ZmqSubscriber(ZmqConfigurator configurator, Consumer<byte[]> callback) {
		if (configurator == null || callback == null)
			throw new IllegalArgumentException("configurator is null in JeroMQSubscriber init mothed !");
		this.configurator = configurator;
		this.callback = callback;
		init();
	}

	private void init() {
		this.zCtx = new ZContext(configurator.ioThreads());
		this.zSocket = zCtx.createSocket(SocketType.SUB);
		this.zSocket.connect(configurator.host());
		this.zSocket.subscribe(configurator.topic().getBytes());
		this.zSocket.setTCPKeepAlive(1);
		this.zSocket.setTCPKeepAliveCount(10);
		this.zSocket.setTCPKeepAliveIdle(15);
		this.zSocket.setTCPKeepAliveInterval(15);
		this.subscriberName = "JeroMQ.SUB$" + configurator.host() + "::" + configurator.topic();
	}

	@Override
	public void subscribe() {
		while (isRun.get()) {
			zSocket.recv();
			byte[] msgBytes = zSocket.recv();
			callback.accept(msgBytes);
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
		return subscriberName;
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

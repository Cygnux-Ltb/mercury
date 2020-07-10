package io.mercury.transport.zmq;

import java.io.Closeable;
import java.io.IOException;
import java.util.Random;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import io.mercury.common.thread.Threads;
import io.mercury.common.util.Assertor;
import io.mercury.transport.core.api.Publisher;
import io.mercury.transport.zmq.configurator.ZmqConfigurator;

public class ZmqPublisher implements Publisher<byte[]>, Closeable {

	private ZContext zCtx;
	private ZMQ.Socket zSocket;

	private String topic;

	private String publisherName;

	private ZmqConfigurator configurator;

	public ZmqPublisher(ZmqConfigurator configurator) {
		this.configurator = Assertor.nonNull(configurator, "configurator");
		init();
	}

	private void init() {
		this.zCtx = new ZContext(configurator.ioThreads());
		this.zSocket = zCtx.createSocket(SocketType.PUB);
		this.zSocket.bind(configurator.host());
		this.topic = configurator.topic();
		this.publisherName = "ZMQ::PUB$" + configurator.host();
	}

	@Override
	public void publish(byte[] msg) {
		publish(topic, msg);
	}

	@Override
	public void publish(String target, byte[] msg) {
		zSocket.sendMore(target);
		zSocket.send(msg, ZMQ.NOBLOCK);
	}

	@Override
	public boolean destroy() {
		zSocket.close();
		zCtx.close();
		return zCtx.isClosed();
	}

	@Override
	public String name() {
		return publisherName;
	}

	public static void main(String[] args) {
//		JeroMqConfigurator configurator = JeroMqConfigurator.builder().setHost("tcp://*:5559").setIoThreads(1)
//				.setTopic("").build();

		ZmqConfigurator configurator = ZmqConfigurator.builder().setHost("tcp://127.0.0.1:13001").setTopic("command")
				.setIoThreads(2).build();

		try (ZmqPublisher publisher = new ZmqPublisher(configurator)) {
			Random random = new Random();

			for (;;) {
				publisher.publish(String.valueOf(random.nextInt()).getBytes());
				Threads.sleep(1000);
			}
		} catch (Exception e) {
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

}

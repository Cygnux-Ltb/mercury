package io.mercury.transport.zmq;

import java.io.Closeable;
import java.io.IOException;
import java.util.Random;

import javax.annotation.Nonnull;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;

import io.mercury.common.thread.Threads;
import io.mercury.common.util.Assertor;
import io.mercury.transport.core.api.Publisher;
import io.mercury.transport.zmq.configurator.ZmqConfigurator;

public class ZmqPublisher implements Publisher<byte[]>, Closeable {

	private ZContext zCtx;
	private Socket socket;

	private String topic;

	private String name;

	private ZmqConfigurator configurator;

	public ZmqPublisher(@Nonnull ZmqConfigurator configurator) {
		Assertor.nonNull(configurator, "configurator");
		this.configurator = configurator;
		init();
	}

	private void init() {
		this.zCtx = new ZContext(configurator.ioThreads());
		this.socket = zCtx.createSocket(SocketType.PUB);
		this.socket.bind(configurator.getHost());
		this.topic = configurator.topic();
		this.name = "ZMQ::PUB$" + configurator.getHost();
	}

	@Override
	public void publish(byte[] msg) {
		publish(topic, msg);
	}

	@Override
	public void publish(String target, byte[] msg) {
		socket.sendMore(target);
		socket.send(msg, ZMQ.NOBLOCK);
	}

	@Override
	public boolean destroy() {
		socket.close();
		zCtx.close();
		return zCtx.isClosed();
	}

	@Override
	public String name() {
		return name;
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

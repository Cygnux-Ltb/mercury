package io.mercury.transport.zmq;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import io.mercury.common.thread.Threads;
import io.mercury.common.util.Assertor;
import io.mercury.transport.core.api.Receiver;
import io.mercury.transport.zmq.configurator.ZmqConfigurator;

public class ZmqPipeline implements Receiver, Closeable {

	private ZContext zCtx;
	private ZMQ.Socket zSocket;

	private String name;

	private Function<byte[], byte[]> pipeline;
	private ZmqConfigurator configurator;

	private volatile boolean isRun = true;

	public ZmqPipeline(@Nonnull ZmqConfigurator configurator, @Nonnull Function<byte[], byte[]> pipeline) {
		Assertor.nonNull(configurator, "configurator");
		Assertor.nonNull(pipeline, "pipeline");
		this.configurator = configurator;
		this.pipeline = pipeline;
		init();
	}

	private void init() {
		this.zCtx = new ZContext(configurator.ioThreads());
		this.zSocket = zCtx.createSocket(SocketType.REP);
		this.zSocket.bind(configurator.getHost());
		this.name = "ZMQ::REP:" + configurator.getHost();
	}

	@Override
	public void receive() {
		while (isRun) {
			byte[] recvBytes = zSocket.recv();
			byte[] sendBytes = pipeline.apply(recvBytes);
			if (sendBytes != null)
				zSocket.send(sendBytes);
		}
		return;
	}

	@Override
	public boolean destroy() {
		this.isRun = false;
		Threads.sleep(50);
		zSocket.close();
		zCtx.close();
		return zCtx.isClosed();
	}

	@Override
	public String name() {
		return name;
	}

	public static void main(String[] args) {
		try (ZmqPipeline receiver = new ZmqPipeline(
				ZmqConfigurator.builder().setIoThreads(10).setHost("tcp://*:5551").build(), (byte[] byteMsg) -> {
					System.out.println(new String(byteMsg));
					return null;
				})) {
			Threads.sleep(15000);
			Threads.startNewThread(() -> receiver.receive());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean isConnected() {
		return !zCtx.isClosed();
	}

	@Override
	public void reconnect() {
		// TODO Auto-generated method stub
	}

	@Override
	public void close() throws IOException {
		destroy();
	}

}

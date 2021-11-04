package io.mercury.transport.socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;

import io.mercury.common.concurrent.queue.AbstractSingleConsumerQueue;
import io.mercury.common.concurrent.queue.jct.JctSingleConsumerQueue;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.util.Assertor;
import io.mercury.transport.api.Sender;
import io.mercury.transport.api.TransportComponent;
import io.mercury.transport.socket.configurator.SocketConfigurator;

public final class SocketSender extends TransportComponent implements Sender<byte[]> {

	private SocketConfigurator configurator;

	private Socket socket;

	private AtomicBoolean isRun = new AtomicBoolean(true);

	protected static final Logger log = CommonLoggerFactory.getLogger(SocketSender.class);

	public SocketSender(SocketConfigurator configurator) {
		Assertor.nonNull(configurator, "configurator");
		this.configurator = configurator;
		init();
	}

	private void init() {
		try {
			this.socket = new Socket(configurator.getHost(), configurator.getPort());
		} catch (IOException e) {
			log.error("Throw IOException -> {}", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isConnected() {
		return socket == null ? false : socket.isConnected();
	}

	@Override
	public boolean closeIgnoreException() {
		this.isRun.set(false);
		try {
			outputStream.close();
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public String getName() {
		return "SocketSender -> " + socket.hashCode();
	}

	@Override
	public void sent(byte[] msg) {
		innerQueue.enqueue(msg);
	}

	private DataOutputStream outputStream;

	private void processSendQueue(byte[] msg) {
		try {
			if (isRun.get()) {
				if (outputStream == null)
					outputStream = new DataOutputStream(socket.getOutputStream());
				outputStream.write(msg);
			}
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			closeIgnoreException();
		}
	}

	private AbstractSingleConsumerQueue<byte[]> innerQueue = JctSingleConsumerQueue.multiProducer(getName() + "-InnerQueue")
			.setCapacity(512).buildWithProcessor(bytes -> processSendQueue(bytes));

	public static void main(String[] args) throws IOException {
		SocketConfigurator configurator = SocketConfigurator.builder().host("192.168.1.138").port(7901).build();
		try (SocketSender sender = new SocketSender(configurator)) {
			sender.sent("hello".getBytes());
		}
	}

}

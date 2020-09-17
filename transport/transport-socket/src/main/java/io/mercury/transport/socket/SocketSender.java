package io.mercury.transport.socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;

import io.mercury.common.concurrent.queue.MpscArrayBlockingQueue;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.util.Assertor;
import io.mercury.transport.core.api.Sender;
import io.mercury.transport.socket.configurator.SocketConfigurator;

public class SocketSender implements Sender<byte[]> {

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
			this.socket = new Socket(configurator.host(), configurator.port());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public boolean isConnected() {
		return socket == null ? false : socket.isConnected();
	}

	@Override
	public boolean destroy() {
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
	public String name() {
		return "SocketSender -> " + socket.hashCode();
	}

	@Override
	public void send(byte[] msg) {
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
			destroy();
		}
	}

	private MpscArrayBlockingQueue<byte[]> innerQueue = MpscArrayBlockingQueue.autoStartQueue(1024,
			bytes -> processSendQueue(bytes));

	public static void main(String[] args) {
		SocketConfigurator configurator = SocketConfigurator.builder().host("192.168.1.138").port(7901).build();
		SocketSender sender = new SocketSender(configurator);
		sender.send("hello".getBytes());
	}

}

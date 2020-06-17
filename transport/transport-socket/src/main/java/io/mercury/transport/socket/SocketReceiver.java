package io.mercury.transport.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.ThreadTool;
import io.mercury.transport.core.api.Receiver;
import io.mercury.transport.socket.configurator.SocketConfigurator;

public class SocketReceiver implements Receiver {

	private SocketConfigurator configurator;
	private Consumer<byte[]> callback;

	private Socket socket;

	private AtomicBoolean isReceiving = new AtomicBoolean(false);
	private AtomicBoolean isRun = new AtomicBoolean(false);

	protected Logger log = CommonLoggerFactory.getLogger(getClass());

	/**
	 * @param configurator
	 * @param callback
	 * @param serverSocket
	 */
	public SocketReceiver(SocketConfigurator configurator, Consumer<byte[]> callback) {
		super();
		if (configurator == null || callback == null)
			throw new IllegalArgumentException("configurator or callback is null for init ");
		this.configurator = configurator;
		this.callback = callback;
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
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public String name() {
		return "SocketReceiver -> " + socket.hashCode();
	}

	@Override
	public void receive() {
		if (!isRun.get())
			isRun.set(true);
		if (!isReceiving.get())
			startReceiveThread();
	}

	private synchronized void startReceiveThread() {
		if (isReceiving.get())
			return;
		isReceiving.set(true);
		ThreadTool.startNewThread(() -> {
			InputStream inputStream = null;
			try {
				inputStream = socket.getInputStream();
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage());
			}
			while (isRun.get()) {
				try {
					int available = inputStream.available();
					if (available == 0) {
						ThreadTool.sleep(configurator.receiveInterval());
						continue;
					}
					byte[] bytes = new byte[available];
					IOUtils.read(inputStream, bytes);
					callback.accept(bytes);
				} catch (IOException e) {
					log.error(e.getMessage(), e);
					try {
						inputStream.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					destroy();
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void main(String[] args) {

		SocketConfigurator configurator = SocketConfigurator.builder().host("192.168.1.138").port(7901).build();

		SocketReceiver receiver = new SocketReceiver(configurator, bytes -> System.out.println(new String(bytes)));

		receiver.receive();

	}

	@Override
	public void reconnect() {
		// TODO Auto-generated method stub

	}

}

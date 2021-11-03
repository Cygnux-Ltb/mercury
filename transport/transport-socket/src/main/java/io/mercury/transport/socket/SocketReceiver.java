package io.mercury.transport.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.SleepSupport;
import io.mercury.common.thread.Threads;
import io.mercury.common.util.Assertor;
import io.mercury.transport.api.Receiver;
import io.mercury.transport.api.TransportComponent;
import io.mercury.transport.socket.configurator.SocketConfigurator;

public class SocketReceiver extends TransportComponent implements Receiver {

	private SocketConfigurator configurator;
	private Consumer<byte[]> callback;

	private Socket socket;

	private AtomicBoolean isReceiving = new AtomicBoolean(false);
	private AtomicBoolean isRun = new AtomicBoolean(false);

	protected static final Logger log = CommonLoggerFactory.getLogger(SocketReceiver.class);

	/**
	 * @param configurator
	 * @param callback
	 */
	public SocketReceiver(SocketConfigurator configurator, Consumer<byte[]> callback) {
		Assertor.nonNull(configurator, "configurator");
		Assertor.nonNull(callback, "callback");
		this.configurator = configurator;
		this.callback = callback;
		init();
	}

	private void init() {
		try {
			this.socket = new Socket(configurator.getHost(), configurator.getPort());
		} catch (Exception e) {
			log.error("new Socket({}, {}) throw Exception -> {}", configurator.getHost(), configurator.getPort(),
					e.getMessage(), e);
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
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			log.error("socket.close() throw IOException -> {}", e.getMessage(), e);
		}
		return true;
	}

	@Override
	public String getName() {
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
		Threads.startNewThread(() -> {
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
						SleepSupport.sleep(configurator.receiveInterval());
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
					closeIgnoreException();
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

		try {
			receiver.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void reconnect() {

	}

	@Override
	public void close() throws IOException {
		closeIgnoreException();
	}

}

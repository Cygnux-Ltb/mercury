package io.mercury.transport.zmq;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.zeromq.SocketType;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.Threads;
import io.mercury.transport.api.Receiver;
import io.mercury.transport.zmq.exception.ZmqBindException;

public class ZmqReceiver extends ZmqTransport implements Receiver, Closeable {

	private final Function<byte[], byte[]> handler;

	private static final Logger log = CommonLoggerFactory.getLogger(ZmqPublisher.class);

	ZmqReceiver(@Nonnull ZmqConfigurator cfg, @Nonnull Function<byte[], byte[]> handler) {
		super(cfg);
		this.handler = handler;
		String addr = cfg.getAddr();
		if (zSocket.bind(addr)) {
			log.info("bound addr -> {}", addr);
		} else {
			log.error("unable to bind -> {}", addr);
			throw new ZmqBindException(addr);
		}
		setTcpKeepAlive(cfg.getTcpKeepAlive());
		newStartTime();
		this.name = "Zmq::Fanout$" + cfg.getAddr();
	}

	@Override
	public SocketType getSocketType() {
		return SocketType.REP;
	}

	@Override
	public void receive() {
		while (isRunning.get()) {
			byte[] recv = zSocket.recv();
			byte[] sent = handler.apply(recv);
			if (sent != null)
				zSocket.send(sent);
		}
	}

	@Override
	public void reconnect() {
		throw new UnsupportedOperationException("ZmqFanout unsupport reconnect");
	}

	public static void main(String[] args) {
		try (ZmqReceiver receiver = new ZmqReceiver(ZmqConfigurator.tcp(5551).ioThreads(10), (byte[] byteMsg) -> {
			System.out.println(new String(byteMsg));
			return null;
		})) {
			Threads.sleep(15000);
			Threads.startNewThread(() -> receiver.receive());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

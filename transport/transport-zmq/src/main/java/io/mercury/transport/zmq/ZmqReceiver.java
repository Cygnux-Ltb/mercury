package io.mercury.transport.zmq;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.zeromq.SocketType;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.SleepSupport;
import io.mercury.common.thread.Threads;
import io.mercury.common.util.Assertor;
import io.mercury.transport.api.Receiver;
import io.mercury.transport.zmq.exception.ZmqBindException;

public class ZmqReceiver extends ZmqTransport implements Receiver, Closeable {

	private final Function<byte[], byte[]> handler;

	private static final Logger log = CommonLoggerFactory.getLogger(ZmqPublisher.class);

	ZmqReceiver(@Nonnull ZmqConfigurator cfg, @Nonnull Function<byte[], byte[]> handler) {
		super(cfg);
		Assertor.nonNull(handler, "handler");
		this.handler = handler;
		var addr = cfg.getAddr();
		if (socket.bind(addr))
			log.info("ZmqReceiver bound addr -> {}", addr);
		else {
			log.error("ZmqReceiver unable to bind -> {}", addr);
			throw new ZmqBindException(addr);
		}
		setTcpKeepAlive(cfg.getTcpKeepAlive());
		this.name = "ZMQ::REP$" + addr;
		newStartTime();
	}

	public Function<byte[], byte[]> getHandler() {
		return handler;
	}

	@Override
	protected SocketType getSocketType() {
		return SocketType.REP;
	}

	@Override
	public void receive() {
		while (isRunning.get()) {
			var recv = socket.recv();
			var sent = handler.apply(recv);
			if (sent != null)
				socket.send(sent);
		}
	}

	@Override
	public void reconnect() {
		throw new UnsupportedOperationException("ZmqReceiver unsupport reconnect function");
	}

	public static void main(String[] args) {
		try (ZmqReceiver receiver = new ZmqReceiver(ZmqConfigurator.tcp(5551).ioThreads(10), (byte[] byteMsg) -> {
			System.out.println(new String(byteMsg));
			return null;
		})) {
			SleepSupport.sleep(15000);
			Threads.startNewThread(() -> receiver.receive());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

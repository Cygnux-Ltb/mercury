package io.mercury.transport.zmq;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.zeromq.SocketType;

import io.mercury.common.lang.Asserter;
import io.mercury.common.log4j2.Log4j2LoggerFactory;
import io.mercury.common.thread.SleepSupport;
import io.mercury.common.thread.ThreadSupport;
import io.mercury.transport.api.Receiver;
import io.mercury.transport.zmq.exception.ZmqBindException;

public class ZmqReceiver extends ZmqTransport implements Receiver, Closeable {

	private static final Logger log = Log4j2LoggerFactory.getLogger(ZmqPublisher.class);

	private final Function<byte[], byte[]> handler;

	ZmqReceiver(@Nonnull ZmqConfigurator cfg, @Nonnull Function<byte[], byte[]> handler) {
		super(cfg);
		Asserter.nonNull(handler, "handler");
		this.handler = handler;
		String addr = cfg.getAddr().toString();
		if (socket.bind(addr))
			log.info("ZmqReceiver bound addr -> {}", addr);
		else {
			log.error("ZmqReceiver unable to bind -> {}", addr);
			throw new ZmqBindException(addr);
		}
		setTcpKeepAlive(cfg.getTcpKeepAlive());
		this.name = "zrec$" + addr;
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
	public ZmqType getZmqType() {
		return ZmqType.ZmqReceiver;
	}

	private final byte[] emptyMsg = new byte[] {};

	@Override
	public void receive() {
		while (isRunning.get()) {
			byte[] recv = socket.recv();
			byte[] sent = handler.apply(recv);
			if (sent != null)
				socket.send(sent);
			else
				socket.send(emptyMsg);
		}
	}

	@Override
	public void reconnect() {
		throw new UnsupportedOperationException("ZmqReceiver unsupported reconnect function");
	}

	public static void main(String[] args) {
		try (ZmqReceiver receiver = ZmqConfigurator.tcp(5551).newReceiver((byte[] recvMsg) -> {
			System.out.println(new String(recvMsg));
			return null;
		})) {
			SleepSupport.sleep(15000);
			ThreadSupport.startNewThread(receiver::receive);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

package io.mercury.transport.zmq;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.zeromq.SocketType;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.Threads;
import io.mercury.transport.api.Subscriber;
import io.mercury.transport.zmq.exception.ZmqConnectionException;

public class ZmqFanin extends ZmqTransport implements Subscriber, Closeable {

	private final Function<byte[], byte[]> pipeline;

	private static final Logger log = CommonLoggerFactory.getLogger(ZmqPublisher.class);

	public ZmqFanin(@Nonnull ZmqConfigurator cfg, @Nonnull Function<byte[], byte[]> pipeline) {
		super(cfg);
		this.pipeline = pipeline;
		String addr = cfg.getAddr();
		if (zSocket.bind(addr)) {
			log.info("bound addr -> {}", addr);
		} else {
			log.error("unable to bind -> {}", addr);
			throw new ZmqConnectionException(addr);
		}
		setTcpKeepAlive(cfg.getTcpKeepAlive());
		newStartTime();
		this.name = "Zmq::Fanout$" + addr;
	}

	public Function<byte[], byte[]> getPipeline() {
		return pipeline;
	}

	@Override
	public SocketType getSocketType() {
		return SocketType.REP;
	}

	@Override
	public void subscribe() {
		while (isRunning.get()) {
			byte[] recv = zSocket.recv();
			byte[] sent = pipeline.apply(recv);
			if (sent != null)
				zSocket.send(sent);
		}
	}

	@Override
	public void reconnect() {
		throw new UnsupportedOperationException("ZmqFanin unsupport reconnect");
	}

	@Override
	public void run() {
		subscribe();
	}

	public static void main(String[] args) {
		try (ZmqFanin receiver = new ZmqFanin(ZmqConfigurator.tcp(5551).ioThreads(10), bytes -> {
			System.out.println(new String(bytes));
			return null;
		})) {
			Threads.sleep(15000);
			Threads.startNewThread(() -> receiver.subscribe());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

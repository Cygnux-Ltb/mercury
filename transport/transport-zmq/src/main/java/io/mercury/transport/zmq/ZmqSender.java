package io.mercury.transport.zmq;

import java.io.Closeable;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;
import org.zeromq.SocketType;

import io.mercury.common.lang.Assertor;
import io.mercury.common.log.Log4j2LoggerFactory;
import io.mercury.common.serialization.BytesSerializer;
import io.mercury.transport.api.Sender;
import io.mercury.transport.zmq.exception.ZmqConnectionException;

@NotThreadSafe
public class ZmqSender<T> extends ZmqTransport implements Sender<T>, Closeable {

	private final BytesSerializer<T> serializer;

	private static final Logger log = Log4j2LoggerFactory.getLogger(ZmqSender.class);

	/**
	 * @param cfg
	 * @param serializer
	 */
	ZmqSender(@Nonnull ZmqConfigurator cfg, @Nonnull BytesSerializer<T> serializer) {
		super(cfg);
		Assertor.nonNull(serializer, "serializer");
		this.serializer = serializer;
		var addr = cfg.getAddr();
		if (socket.connect(addr))
			log.info("ZmqSender connected addr -> {}", addr);
		else {
			log.error("ZmqSender unable to connect addr -> {}", addr);
			throw new ZmqConnectionException(addr);
		}
		this.name = "ZMQ::REQ$" + addr;
		newStartTime();
	}

	@Override
	protected SocketType getSocketType() {
		return SocketType.REQ;
	}

	@Override
	public void sent(T msg) {
		var bytes = serializer.serialization(msg);
		if (bytes != null && bytes.length > 0) {
			socket.send(bytes);
			socket.recv();
		}
	}

	public static void main(String[] args) {

		ZmqConfigurator cfg = ZmqConfigurator.tcp("localhost", 5551);
		try (ZmqSender<String> sender = new ZmqSender<String>(cfg, msg -> msg.getBytes())) {
			sender.sent("TEST MSG");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

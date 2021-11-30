package io.mercury.transport.zmq;

import java.io.Closeable;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;
import org.zeromq.SocketType;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.serialization.BytesSerializer;
import io.mercury.transport.api.Sender;
import io.mercury.transport.zmq.exception.ZmqConnectionException;

@NotThreadSafe
public class ZmqSender<T> extends ZmqTransport implements Sender<T>, Closeable {

	private final BytesSerializer<T> ser;

	private static final Logger log = CommonLoggerFactory.getLogger(ZmqSender.class);

	/**
	 * @param cfg
	 * @param ser
	 */
	ZmqSender(@Nonnull ZmqConfigurator cfg, @Nonnull BytesSerializer<T> ser) {
		super(cfg);
		this.ser = ser;
		var addr = cfg.getAddr();
		if (socket.connect(addr))
			log.info("connected addr -> {}", addr);
		else {
			log.error("unable to connect addr -> {}", addr);
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
		var bytes = ser.serialization(msg);
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

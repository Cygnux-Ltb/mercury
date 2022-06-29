package io.mercury.transport.zmq;

import java.io.Closeable;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;
import org.zeromq.SocketType;

import io.mercury.common.lang.Asserter;
import io.mercury.common.log.Log4j2LoggerFactory;
import io.mercury.common.serialization.specific.BytesSerializer;
import io.mercury.transport.api.Sender;
import io.mercury.transport.zmq.exception.ZmqConnectionException;

@NotThreadSafe
public class ZmqSender<T> extends ZmqTransport implements Sender<T>, Closeable {

	private static final Logger log = Log4j2LoggerFactory.getLogger(ZmqSender.class);

	private final BytesSerializer<T> serializer;

	/**
	 * @param cfg
	 * @param serializer
	 */
	ZmqSender(@Nonnull ZmqConfigurator cfg, @Nonnull BytesSerializer<T> serializer) {
		super(cfg);
		Asserter.nonNull(serializer, "serializer");
		this.serializer = serializer;
		String addr = cfg.getAddr().toString();
		if (socket.connect(addr))
			log.info("ZmqSender connected addr -> {}", addr);
		else {
			log.error("ZmqSender unable to connect addr -> {}", addr);
			throw new ZmqConnectionException(addr);
		}
		this.name = "zreq$" + addr;
		newStartTime();
	}

	@Override
	protected SocketType getSocketType() {
		return SocketType.REQ;
	}

	@Override
	public ZmqTransportType getTransportType() {
		return ZmqTransportType.ZmqSender;
	}

	@Override
	public void sent(T msg) {
		byte[] bytes = serializer.serialization(msg);
		if (bytes != null && bytes.length > 0) {
			socket.send(bytes);
			socket.recv();
		}
	}

	public static void main(String[] args) {

		ZmqConfigurator cfg = ZmqConfigurator.tcp("localhost", 5551);
		try (ZmqSender<String> sender = new ZmqSender<String>(cfg, String::getBytes)) {
			sender.sent("TEST MSG");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

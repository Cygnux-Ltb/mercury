package io.mercury.transport.zmq;

import java.io.Closeable;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;
import org.zeromq.SocketType;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.serialization.spec.BytesSerializer;
import io.mercury.transport.api.Sender;
import io.mercury.transport.zmq.cfg.ZmqAddress;
import io.mercury.transport.zmq.exception.ZmqConnectionException;

@NotThreadSafe
public class ZmqSender<T> extends ZmqTransport implements Sender<T>, Closeable {

	private final String name;

	private final ZmqSenderConfigurator cfg;

	private final BytesSerializer<T> ser;

	private static final Logger log = CommonLoggerFactory.getLogger(ZmqSender.class);

	private ZmqSender(@Nonnull ZmqSenderConfigurator cfg, @Nonnull BytesSerializer<T> ser) {
		super(cfg.getAddr(), cfg.getIoThreads());
		this.cfg = cfg;
		this.ser = ser;
		if (socket.connect(addr.getAddr())) {
			log.info("connected addr -> {}", addr);
		} else {
			log.error("unable to connect addr -> {}", addr);
			throw new ZmqConnectionException(addr);
		}
		this.name = "Zmq::Req$" + cfg.getConnectionInfo();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	protected SocketType getSocketType() {
		return SocketType.REQ;
	}

	public ZmqSenderConfigurator getCfg() {
		return cfg;
	}

	@Override
	public void sent(T msg) {
		byte[] bytes = ser.serialization(msg);
		if (bytes != null && bytes.length > 0) {
			socket.send(bytes);
			socket.recv();
		}
	}

	/**
	 * 
	 * @author yellow013
	 *
	 */
	public static final class ZmqSenderConfigurator extends ZmqConfigurator<ZmqSenderConfigurator> {

		private ZmqSenderConfigurator(ZmqAddress addr) {
			super(addr);
		}

		@Override
		protected ZmqSenderConfigurator returnSelf() {
			return this;
		}

		/**
		 * 创建TCP协议连接
		 * 
		 * @param port
		 * @return
		 */
		public final static ZmqSenderConfigurator tcp(int port) {
			return new ZmqSenderConfigurator(ZmqAddress.tcp(port));
		}

		/**
		 * 创建TCP协议连接
		 * 
		 * @param addr
		 * @param port
		 * @return
		 */
		public final static ZmqSenderConfigurator tcp(String addr, int port) {
			return new ZmqSenderConfigurator(ZmqAddress.tcp(addr, port));
		}

		/**
		 * 创建IPC协议连接
		 * 
		 * @param addr
		 * @return
		 */
		public final static ZmqSenderConfigurator ipc(String addr) {
			return new ZmqSenderConfigurator(ZmqAddress.ipc(addr));
		}

	}

	public static void main(String[] args) {

		ZmqSenderConfigurator configurator = ZmqSenderConfigurator.tcp("localhost", 5551);

		try (ZmqSender<String> sender = new ZmqSender<String>(configurator, msg -> msg.getBytes())) {

			sender.sent("TEST MSG");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

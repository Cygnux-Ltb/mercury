package io.mercury.transport.zmq;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import io.mercury.common.annotation.AbstractFunction;
import io.mercury.common.lang.Assertor;
import io.mercury.common.log.Log4j2LoggerFactory;
import io.mercury.common.util.StringSupport;
import io.mercury.transport.api.Transport;
import io.mercury.transport.api.TransportComponent;
import io.mercury.transport.attr.TcpKeepAlive;
import io.mercury.transport.attr.TcpKeepAlive.KeepAliveType;

/**
 * 
 * @author yellow013
 *
 *         <pre>
  	-XX:+UseBiasedLocking might increase performance a little bit or not
	-XX:+UseNUMA could increase performance
 *         </pre>
 *
 */
public abstract class ZmqTransport extends TransportComponent implements Transport, Closeable {

	private static final Logger log = Log4j2LoggerFactory.getLogger(ZmqTransport.class);

	// ZMQ配置器
	protected final ZmqConfigurator cfg;

	// 组件运行状态, 初始为已开始运行
	protected final AtomicBoolean isRunning = new AtomicBoolean(true);

	// org.zeromq.ZContext
	protected ZContext context;

	// org.zeromq.ZMQ.Socket
	protected ZMQ.Socket socket;

	// 组件名称
	protected String name;

	protected ZmqTransport(final ZmqConfigurator cfg) {
		Assertor.nonNull(cfg, "cfg");
		this.cfg = cfg;
		this.context = new ZContext(cfg.getIoThreads());
		log.info("ZMQ context initialized, ioThreads=={}", cfg.getIoThreads());
		SocketType type = getSocketType();
		this.socket = context.createSocket(type);
		log.info("ZMQ socket created with type -> {}", type);
	}

	@AbstractFunction
	protected abstract SocketType getSocketType();

	public abstract ZmqTransportType getTransportType();

	/**
	 * 设置TcpKeepAlive, 由子类负责调用
	 *
	 * @param option
	 * @return
	 */
	protected ZMQ.Socket setTcpKeepAlive(TcpKeepAlive option) {
		if (option != null) {
			log.info("setting ZMQ.Socket TCP KeepAlive with -> {}", option);
			KeepAliveType keepAlive = option.getKeepAlive();
			switch (keepAlive) {
			case Enable:
				int keepAliveCount = option.getKeepAliveCount();
				int keepAliveIdle = option.getKeepAliveIdle();
				int keepAliveInterval = option.getKeepAliveInterval();
				log.info(
						"ZMQ.Socket used [Enable] option, KeepAliveCount==[{}], KeepAliveIdle==[{}], KeepAliveInterval==[{}]",
						keepAliveCount, keepAliveIdle, keepAliveInterval);
				socket.setTCPKeepAlive(keepAlive.getCode());
				socket.setTCPKeepAliveCount(keepAliveCount);
				socket.setTCPKeepAliveIdle(keepAliveIdle);
				socket.setTCPKeepAliveInterval(keepAliveInterval);
				break;
			case Disable:
				socket.setTCPKeepAlive(keepAlive.getCode());
				log.info("ZMQ.Socket used [Disable] option");
				break;
			case Default:
			default:
				log.info("ZMQ.Socket used [Default] option");
				break;
			}
		}
		return socket;
	}

	public ZmqConfigurator getConfigurator() {
		return cfg;
	}

	public ZContext getContext() {
		return context;
	}

	public ZMQ.Socket getSocket() {
		return socket;
	}

	public boolean setIdentity(String identity) {
		if (StringSupport.isNullOrEmpty(identity))
			return false;
		return socket.setIdentity(identity.getBytes(ZMQ.CHARSET));
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isConnected() {
		return !context.isClosed();
	}

	@Override
	public void close() throws IOException {
		closeIgnoreException();
	}

	@Override
	public boolean closeIgnoreException() {
		if (isRunning.compareAndSet(true, false)) {
			socket.close();
			context.close();
			newEndTime();
			log.info("Zmq component -> {} closed, Running duration millis -> {}", name, getRunningDuration());
		} else
			log.warn("Zmq component -> {} already closed, Cannot be called again", name);
		return context.isClosed();
	}

}

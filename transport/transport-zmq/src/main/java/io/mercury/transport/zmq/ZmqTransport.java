package io.mercury.transport.zmq;

import io.mercury.common.annotation.AbstractFunction;
import io.mercury.common.lang.Asserter;
import io.mercury.common.log.Log4j2LoggerFactory;
import io.mercury.common.util.StringSupport;
import io.mercury.transport.api.Transport;
import io.mercury.transport.api.TransportComponent;
import io.mercury.transport.attr.TcpKeepAlive;
import io.mercury.transport.attr.TcpKeepAlive.KeepAliveOption;
import org.slf4j.Logger;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author yellow013
 *
 * <pre>
 * -XX:+UseBiasedLocking might increase performance a little bit or not
 * -XX:+UseNUMA could increase performance
 *         </pre>
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
        Asserter.nonNull(cfg, "cfg");
        this.cfg = cfg;
        this.context = new ZContext(cfg.getIoThreads());
        log.info("ZMQ context initialized, ioThreads=={}", cfg.getIoThreads());
        SocketType type = getSocketType();
        this.socket = context.createSocket(type);
        log.info("ZMQ socket created with type -> {}", type);
    }

    @AbstractFunction
    protected abstract SocketType getSocketType();

    @AbstractFunction
    public abstract ZmqType getZmqType();

    /**
     * 设置TcpKeepAlive, 由子类负责调用
     *
     * @param tcpKeepAlive TcpKeepAlive
     * @return ZMQ.Socket
     */
    protected ZMQ.Socket setTcpKeepAlive(TcpKeepAlive tcpKeepAlive) {
        if (tcpKeepAlive != null) {
            log.info("setting ZMQ.Socket TCP KeepAlive with -> {}", tcpKeepAlive);
            KeepAliveOption keepAlive = tcpKeepAlive.getKeepAliveOption();
            switch (keepAlive) {
                case Enable -> {
                    int keepAliveCount = tcpKeepAlive.getKeepAliveCount();
                    int keepAliveIdle = tcpKeepAlive.getKeepAliveIdle();
                    int keepAliveInterval = tcpKeepAlive.getKeepAliveInterval();
                    log.info(
                            "ZMQ.Socket used [Enable] TcpKeepAlive, KeepAliveCount==[{}], KeepAliveIdle==[{}], KeepAliveInterval==[{}]",
                            keepAliveCount, keepAliveIdle, keepAliveInterval);
                    socket.setTCPKeepAlive(keepAlive.getCode());
                    socket.setTCPKeepAliveCount(keepAliveCount);
                    socket.setTCPKeepAliveIdle(keepAliveIdle);
                    socket.setTCPKeepAliveInterval(keepAliveInterval);
                }
                case Disable -> {
                    socket.setTCPKeepAlive(keepAlive.getCode());
                    log.info("ZMQ.Socket used [Disable] tcpKeepAlive");
                }
                case Default, default -> log.info("ZMQ.Socket used [Default] tcpKeepAlive");
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

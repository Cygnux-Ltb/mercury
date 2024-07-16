package io.mercury.transport.zmq;

import com.typesafe.config.Config;
import io.mercury.common.annotation.AbstractFunction;
import io.mercury.common.config.ConfigWrapper;
import io.mercury.common.lang.Asserter;
import io.mercury.common.net.IpAddressIllegalException;
import io.mercury.common.net.IpAddressValidator;
import io.mercury.common.util.StringSupport;
import io.mercury.transport.TransportComponent;
import io.mercury.transport.api.Transport;
import io.mercury.transport.attr.TcpKeepAlive;
import io.mercury.transport.zmq.base.ZmqAddr;
import io.mercury.transport.zmq.base.ZmqProtocol;
import io.mercury.transport.zmq.base.ZmqType;
import org.slf4j.Logger;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import javax.annotation.Nonnull;
import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.mercury.common.lang.Asserter.nonNull;
import static io.mercury.common.log4j2.Log4j2LoggerFactory.getLogger;
import static io.mercury.transport.zmq.ZmqConfigOption.Addr;
import static io.mercury.transport.zmq.ZmqConfigOption.IoThreads;
import static io.mercury.transport.zmq.ZmqConfigOption.Port;
import static io.mercury.transport.zmq.ZmqConfigOption.Protocol;

/**
 * @author yellow013
 *
 * <pre>
 * -XX:+UseBiasedLocking might increase performance a little bit or not
 * <br>
 * -XX:+UseNUMA could increase performance
 * </pre>
 */
public abstract class ZmqComponent extends TransportComponent implements Transport, Closeable {

    private static final Logger log = getLogger(ZmqComponent.class);

    // ZMQ配置器
    protected final ZmqConfigurator configurator;

    // 组件运行状态, 初始为已开始运行
    protected final AtomicBoolean isRunning = new AtomicBoolean(true);

    // org.zeromq.ZContext
    protected final ZContext context;

    // org.zeromq.ZMQ.Socket
    protected final ZMQ.Socket socket;

    // 组件名称
    protected String name;

    ZmqComponent(ZmqConfigurator configurator) {
        nonNull(configurator, "configurator");
        this.configurator = configurator;
        this.context = new ZContext(configurator.getIoThreads());
        log.info("ZMQ context initialized, ioThreads=={}", configurator.getIoThreads());
        var type = getSocketType();
        this.socket = context.createSocket(type);
        log.info("ZMQ socket created with type -> SocketType.{}", type);
    }

    @AbstractFunction
    protected abstract SocketType getSocketType();

    @AbstractFunction
    public abstract ZmqType getZmqType();

    /**
     * 设置TcpKeepAlive, 由子类负责调用
     *
     * @param keepAlive TcpKeepAlive
     */
    protected void setTcpKeepAlive(TcpKeepAlive keepAlive) {
        log.info("Default TCPKeepAlive Setting is [{}], Count==[{}], Idle==[{}], Interval==[{}]",
                socket.getTCPKeepAlive(), socket.getTCPKeepAliveCount(),
                socket.getTCPKeepAliveIdle(), socket.getTCPKeepAliveInterval());
        if (keepAlive != null) {
            log.info("setting ZMQ.Socket TCP KeepAlive with -> {}", keepAlive);
            var keepAliveOption = keepAlive.getOption();
            switch (keepAliveOption) {
                case Enable -> {
                    int keepAliveCount = keepAlive.getCount();
                    int keepAliveIdle = keepAlive.getIdle();
                    int keepAliveInterval = keepAlive.getInterval();
                    log.info(
                            "ZMQ.Socket used [Enable] TcpKeepAlive, KeepAliveCount==[{}], KeepAliveIdle==[{}], KeepAliveInterval==[{}]",
                            keepAliveCount, keepAliveIdle, keepAliveInterval);
                    socket.setTCPKeepAlive(keepAliveOption.getCode());
                    socket.setTCPKeepAliveCount(keepAliveCount);
                    socket.setTCPKeepAliveIdle(keepAliveIdle);
                    socket.setTCPKeepAliveInterval(keepAliveInterval);
                }
                case Disable -> {
                    socket.setTCPKeepAlive(keepAliveOption.getCode());
                    log.info("ZMQ.Socket used [Disable] TcpKeepAlive");
                }
                case Default -> log.info("ZMQ.Socket used [Default] TcpKeepAlive");
            }
        }
    }

    public ZmqConfigurator getConfigurator() {
        return configurator;
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
        } else {
            log.warn("Zmq component -> {} already closed, Cannot be called again", name);
        }
        return context.isClosed();
    }


    /**
     * @param config Config
     * @return ZmqConfigurator
     */
    public static ZmqConfigurator config(@Nonnull Config config) {
        return config("", config);
    }

    /**
     * @param config String
     * @param module Config
     * @return ZmqConfigurator
     */
    public static ZmqConfigurator config(String module, @Nonnull Config config) {
        nonNull(config, "config");
        var wrapper = new ConfigWrapper<ZmqConfigOption>(module, config);
        var protocol = ZmqProtocol.of(wrapper.getStringOrThrows(Protocol));
        ZmqConfigurator conf;
        switch (protocol) {
            // 使用tcp协议
            case TCP -> {
                int port = wrapper.getIntOrThrows(Port);
                if (wrapper.hasOption(Addr)) {
                    var tcpAddr = wrapper.getStringOrThrows(Addr);
                    Asserter.isValid(tcpAddr, IpAddressValidator::isIpAddress,
                            new IpAddressIllegalException(tcpAddr));
                    conf = new ZmqConfigurator(ZmqAddr.tcp(tcpAddr, port));
                } else {
                    // 没有addr配置项, 使用本地地址
                    conf = new ZmqConfigurator(ZmqAddr.tcp(port));
                }
            }
            // 使用ipc或inproc协议
            case IPC, INPROC -> {
                var localAddr = wrapper.getStringOrThrows(Addr);
                conf = new ZmqConfigurator(protocol.addr(localAddr));
            }
            default -> throw new UnsupportedOperationException(StringSupport.toString(protocol));
        }
        conf.ioThreads(wrapper.getInt(IoThreads, 1));
        log.info("created ZmqConfigurator object -> {}", conf);
        return conf;
    }

    /**
     * 创建
     *
     * @param addr ZmqAddr
     * @return ZmqConfigurator
     */
    public static ZmqConfigurator addr(@Nonnull ZmqAddr addr) {
        return new ZmqConfigurator(addr);
    }

    /**
     * 创建TCP协议连接
     *
     * @param port int
     * @return ZmqConfigurator
     */
    public static ZmqConfigurator tcp(int port) {
        return tcp("*", port);
    }

    /**
     * 创建TCP协议连接
     *
     * @param addr String
     * @param port int
     * @return ZmqConfigurator
     */
    public static ZmqConfigurator tcp(@Nonnull String addr, int port) {
        return new ZmqConfigurator(ZmqAddr.tcp(addr, port));
    }

    /**
     * 使用[IPC]协议连接, 用于进程间通信
     *
     * @param addr ZmqConfigurator
     * @return String
     */
    public static ZmqConfigurator ipc(@Nonnull String addr) {
        return new ZmqConfigurator(ZmqAddr.ipc(addr));
    }

    /**
     * 使用[INPROC]协议连接, 用于线程间通信
     *
     * @param addr String
     * @return ZmqConfigurator
     */
    public static ZmqConfigurator inproc(@Nonnull String addr) {
        return new ZmqConfigurator(ZmqAddr.inproc(addr));
    }

}

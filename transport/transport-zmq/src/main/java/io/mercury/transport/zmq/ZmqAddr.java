package io.mercury.transport.zmq;

import io.mercury.common.net.IpAddressValidator;

import javax.annotation.Nonnull;

import static io.mercury.common.lang.Asserter.atWithinRange;
import static io.mercury.common.lang.Asserter.nonEmpty;

/**
 * @author yellow013
 */
public final class ZmqAddr {

    private final ZmqProtocol protocol;
    private final String addr;
    private final String fullUri;

    ZmqAddr(ZmqProtocol protocol, String addr, String fullUri) {
        this.protocol = protocol;
        this.addr = addr;
        this.fullUri = fullUri;
    }

    ZmqAddr(ZmqProtocol protocol, String addr) {
        this(protocol, addr, protocol.prefix() + addr);
    }

    public ZmqProtocol protocol() {
        return protocol;
    }

    public String addr() {
        return addr;
    }

    public String fullUri() {
        return fullUri;
    }

    /**
     * [TCP] 协议连接
     *
     * @param port int
     * @return ZmqAddr
     */
    static ZmqAddr tcp(int port) {
        return tcp("*", port);
    }

    /**
     * [TCP] 协议连接
     *
     * @param addr String
     * @param port int
     * @return ZmqAddr
     */
    static ZmqAddr tcp(@Nonnull String addr, int port) {
        nonEmpty(addr, "addr");
        atWithinRange(port, 4096, 65536, "port");
        if (!addr.equals("*"))
            IpAddressValidator.assertIpAddress(addr);
        return new ZmqAddr(ZmqProtocol.tcp, addr + ":" + port);
    }

    /**
     * [IPC] 协议连接, 用于进程间通信
     *
     * @param addr String
     * @return ZmqAddr
     */
     static ZmqAddr ipc(@Nonnull String addr) {
        nonEmpty(addr, "addr");
        return new ZmqAddr(ZmqProtocol.ipc, addr);
    }

    /**
     * [INPROC] 协议连接, 用于线程间通信
     *
     * @param addr String
     * @return ZmqAddr
     */
    public static ZmqAddr inproc(@Nonnull String addr) {
        nonEmpty(addr, "addr");
        return new ZmqAddr(ZmqProtocol.inproc, addr);
    }

    @Override
    public String toString() {
        return fullUri;
    }

}
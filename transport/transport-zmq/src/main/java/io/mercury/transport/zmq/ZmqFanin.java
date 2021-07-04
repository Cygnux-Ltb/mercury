package io.mercury.transport.zmq;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.zeromq.SocketType;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.thread.Threads;
import io.mercury.common.util.Assertor;
import io.mercury.transport.api.Subscriber;
import io.mercury.transport.zmq.cfg.ZmqAddress;
import io.mercury.transport.zmq.exception.ZmqConnectionException;
import lombok.Getter;

public class ZmqFanin extends ZmqTransport implements Subscriber, Closeable {

    @Getter
    private final String name;

    @Getter
    private final ZmqFanoutConfigurator cfg;

    private final Function<byte[], byte[]> pipeline;

    private static final Logger log = CommonLoggerFactory.getLogger(ZmqPublisher.class);

    public ZmqFanin(@Nonnull ZmqFanoutConfigurator cfg, @Nonnull Function<byte[], byte[]> pipeline) {
        super(cfg.getAddr(), cfg.getIoThreads());
        Assertor.nonNull(pipeline, "pipeline");
        this.cfg = cfg;
        this.pipeline = pipeline;
        if (socket.bind(addr.getAddr())) {
            log.info("bound addr -> {}", addr);
        } else {
            log.error("unable to bind -> {}", addr);
            throw new ZmqConnectionException(addr);
        }
        setTcpKeepAlive(cfg.getTcpKeepAlive());
        this.name = "Zmq::Fanout$" + cfg.getAddr();
    }

    @Override
    protected SocketType getSocketType() {
        return SocketType.REP;
    }

    @Override
    public void subscribe() {
        while (isRunning.get()) {
            byte[] recv = socket.recv();
            byte[] sent = pipeline.apply(recv);
            if (sent != null)
                socket.send(sent);
        }
    }

    @Override
    public void reconnect() {
        throw new UnsupportedOperationException("ZmqFanin unsupport reconnect");
    }

    /**
     * @author yellow013
     */
    public static final class ZmqFanoutConfigurator extends ZmqConfigurator<ZmqFanoutConfigurator> {

        private ZmqFanoutConfigurator(ZmqAddress addr) {
            super(addr);
        }

        @Override
        protected ZmqFanoutConfigurator returnSelf() {
            return this;
        }

        /**
         * 创建TCP协议连接
         *
         * @param port
         * @return
         */
        public final static ZmqFanoutConfigurator tcp(int port) {
            return new ZmqFanoutConfigurator(ZmqAddress.tcp(port));
        }

        /**
         * 创建TCP协议连接
         *
         * @param addr
         * @param port
         * @return
         */
        public final static ZmqFanoutConfigurator tcp(@Nonnull String addr, int port) {
            return new ZmqFanoutConfigurator(ZmqAddress.tcp(addr, port));
        }

        /**
         * 创建IPC协议连接
         *
         * @param addr
         * @return
         */
        public final static ZmqFanoutConfigurator ipc(@Nonnull String addr) {
            return new ZmqFanoutConfigurator(ZmqAddress.ipc(addr));
        }

    }

    public static void main(String[] args) {
        try (ZmqFanin receiver = new ZmqFanin(ZmqFanoutConfigurator.tcp(5551).setIoThreads(10), bytes -> {
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

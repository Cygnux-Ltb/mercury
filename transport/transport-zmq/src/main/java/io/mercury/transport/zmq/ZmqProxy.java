package io.mercury.transport.zmq;

import io.mercury.common.lang.Asserter;
import io.mercury.common.log4j2.Log4j2LoggerFactory;
import io.mercury.transport.api.TransportComponent;
import io.mercury.transport.zmq.exception.ZmqProxyException;
import org.slf4j.Logger;
import org.zeromq.ZMQ;

import javax.annotation.Nonnull;

public final class ZmqProxy extends TransportComponent {

    private static final Logger log = Log4j2LoggerFactory.getLogger(ZmqProxy.class);

    private final String name;

    private final boolean isConnected;

    private final ZmqTransport frontend;
    private final ZmqTransport backend;
    private ZmqTransport capture;
    private ZmqSender<String> control;

    ZmqProxy(@Nonnull ZmqTransport frontend, @Nonnull ZmqTransport backend, ZmqTransport capture,
             ZmqTransport control) {
        Asserter.nonNull(frontend, "frontend");
        Asserter.nonNull(backend, "backend");
        this.frontend = frontend;
        this.backend = backend;
        this.name = "[" + frontend.getName() + "]->[" + backend.getName() + "]";
        try {
            this.isConnected = ZMQ.proxy(
                    // 代理前端
                    frontend.getSocket(),
                    // 代理后端
                    backend.getSocket(),
                    // 捕获通道
                    capture != null ? capture.getSocket() : null,
                    // 控制通道
                    capture != null ? control.getSocket() : null);
        } catch (Exception e) {
            throw new ZmqProxyException("ZMQ.proxy(frontend, backend, capture, control) -> " + e.getMessage(), e);
        }
        log.info("ZmqProxy -> {}", name);
        newStartTime();
    }

    public ZmqTransport getFrontend() {
        return frontend;
    }

    public ZmqTransport getBackend() {
        return backend;
    }

    public ZmqTransport getCapture() {
        return capture;
    }

    public ZmqSender<String> getControl() {
        return control;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public boolean closeIgnoreException() {

        return false;
    }

    /**
     * @param frontend 代理前端
     * @param backend  代理后端
     * @return Builder
     */
    public static Builder config(ZmqTransport frontend, ZmqTransport backend) {
        return new Builder(frontend, backend);
    }

    public static class Builder {
        // 代理前端
        private final ZmqTransport frontend;
        // 代理后端
        private final ZmqTransport backend;

        // 捕获通道
        private ZmqTransport capture;
        // 控制通道
        private ZmqTransport control;

        private Builder(ZmqTransport frontend, ZmqTransport backend) {
            this.frontend = frontend;
            this.backend = backend;
        }

        /**
         * If the capture socket is not NULL, the proxy shall send all messages,<br>
         * received on both frontend and backend, to the capture socket. <br>
         * The capture socket should be a ZMQ_PUB, ZMQ_DEALER, ZMQ_PUSH, or ZMQ_PAIR
         * socket.
         * <p>
         * 代理将发送所有前端和后端接收到的消息到捕获Socket. 捕获Socket应该为ZMQ_PUB, ZMQ_DEALER,
         * ZMQ_PUSH或ZMQ_PAIR类型的套接字
         *
         * @param capture 捕获Socket, 用于接收全部数据
         * @return Builder
         */
        public Builder setCapture(ZmqTransport capture) {
            this.capture = capture;
            return this;
        }

        /**
         * @param control 控制通道, 用于接收控制指令
         * @return Builder
         */
        public Builder setControl(ZmqTransport control) {
            this.control = control;
            return this;
        }

        public ZmqProxy build() {
            return new ZmqProxy(frontend, backend, capture, control);
        }

    }

}

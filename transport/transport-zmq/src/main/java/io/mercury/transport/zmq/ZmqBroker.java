package io.mercury.transport.zmq;

import org.zeromq.SocketType;

public class ZmqBroker extends ZmqComponent {

    ZmqBroker(ZmqConfigurator configurator) {
        super(configurator);
    }

    @Override
    protected SocketType getSocketType() {
        return SocketType.ROUTER;
    }

    @Override
    public ZmqType getZmqType() {
        return ZmqType.ZBroker;
    }

}

package io.mercury.transport.zmq;

import io.mercury.transport.zmq.annotation.ZmqSubscribe;
import org.junit.Test;

import static io.mercury.transport.zmq.ZmqProtocol.IPC;

public class ZmqSubscriberTest {

    @Test
    public void test() {

    }

    @ZmqSubscribe(protocol = IPC, addr = "", ioThreads = 2)
    private void handleZmqMsg() {

    }

}

package io.mercury.common.concurrent.disruptor.dynamic.example;

import io.mercury.common.concurrent.disruptor.dynamic.DynamicDisruptor;
import io.mercury.common.concurrent.disruptor.dynamic.sentinel.SentinelClient;

/**
 * @author : Rookiex
 * @version : 1.0
 */
public class ExampleDisruptorServer {

    private DynamicDisruptor server;

    public void startServer(String name, int bufferSize,
                            int consumeSize, int maxConsumeSize,
                            int windowsLength, int windowsSize) {
        server = new DynamicDisruptor(name, consumeSize, consumeSize, maxConsumeSize);
        SentinelClient sentinelClient = new SentinelClient(windowsLength, windowsSize);
        server.init(bufferSize, sentinelClient, new ExampleHandlerFactory());
        server.start();
    }

    public void sendMsg(int id, String msg) {
        server.publishEvent((a, b, c) -> {
            a.setId(id);
            a.setName(msg);
        });
    }
}

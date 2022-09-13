package io.mercury.common.concurrent.disruptor.dynamic.example;

import io.mercury.common.concurrent.disruptor.dynamic.core.AbstractSentinelHandler;
import io.mercury.common.concurrent.disruptor.dynamic.core.HandlerEvent;
import io.mercury.common.concurrent.disruptor.dynamic.sentinel.SentinelClient;

/**
 * @author : Rookiex
 * @version :
 * @date : Created in 2019/11/11 15:34
 * @describe :
 */
public class ExampleSentinelHandler extends AbstractSentinelHandler {

    public ExampleSentinelHandler(SentinelClient sentinelClient) {
        super(sentinelClient);
    }

    @Override
    public void deal(HandlerEvent event) throws Exception {
        int id = event.getId();
        String name = event.getName();
        Thread.sleep(20);
        if (id % 5000 == 0)
            System.out.println(
                    "connect ping==" + id + ", name==" + name + ", thread ==> " + Thread.currentThread().getName());
    }
}

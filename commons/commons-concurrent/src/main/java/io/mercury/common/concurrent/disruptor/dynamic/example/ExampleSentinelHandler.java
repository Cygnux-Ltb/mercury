package io.mercury.common.concurrent.ring.dynamic.example;

import io.mercury.common.concurrent.ring.dynamic.core.AbstractSentinelHandler;
import io.mercury.common.concurrent.ring.dynamic.core.HandlerEvent;
import io.mercury.common.concurrent.ring.dynamic.sentinel.SentinelClient;

/**
 * @author : Rookiex
 * @version :
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
            System.out.println("connect ping==" + id + ", name==" + name + ", thread ==> "
                    + Thread.currentThread().getName());
    }
}

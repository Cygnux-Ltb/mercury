package io.mercury.common.concurrent.ring.dynamic.example;

import io.mercury.common.concurrent.ring.dynamic.core.AbstractSentinelHandler;
import io.mercury.common.concurrent.ring.dynamic.core.HandlerFactory;
import io.mercury.common.concurrent.ring.dynamic.sentinel.SentinelClient;

/**
 * @author : Rookiex
 * @version :
 */
public class ExampleHandlerFactory implements HandlerFactory {

    private SentinelClient sentinelClient;

    @Override
    public AbstractSentinelHandler createHandler() {
        return new ExampleSentinelHandler(sentinelClient);
    }

    @Override
    public void setSentinelClient(SentinelClient sentinelClient) {
        this.sentinelClient = sentinelClient;
    }

}

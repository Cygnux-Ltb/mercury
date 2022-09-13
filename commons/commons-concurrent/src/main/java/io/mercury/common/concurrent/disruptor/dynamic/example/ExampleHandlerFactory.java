package io.mercury.common.concurrent.disruptor.dynamic.example;

import io.mercury.common.concurrent.disruptor.dynamic.core.AbstractSentinelHandler;
import io.mercury.common.concurrent.disruptor.dynamic.core.HandlerFactory;
import io.mercury.common.concurrent.disruptor.dynamic.sentinel.SentinelClient;

/**
 * @author : Rookiex
 * @version :
 * @date : Created in 2019/11/11 17:28
 * @describe :
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

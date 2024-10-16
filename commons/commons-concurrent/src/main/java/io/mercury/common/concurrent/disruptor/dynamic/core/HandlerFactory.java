package io.mercury.common.concurrent.disruptor.dynamic.core;

import io.mercury.common.concurrent.disruptor.dynamic.sentinel.SentinelClient;

/**
 * @author : Rookiex
 * @version : 1.0
 */
public interface HandlerFactory {

    /**
     * @return 创建handler
     */
    AbstractSentinelHandler createHandler();

    /**
     * @param sentinelClient s 设置
     */
    void setSentinelClient(SentinelClient sentinelClient);

}

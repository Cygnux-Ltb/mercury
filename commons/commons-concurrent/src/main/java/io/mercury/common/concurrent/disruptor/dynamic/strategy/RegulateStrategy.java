package io.mercury.common.concurrent.disruptor.dynamic.strategy;

import io.mercury.common.concurrent.disruptor.dynamic.DynamicDisruptor;
import io.mercury.common.concurrent.disruptor.dynamic.sentinel.SentinelEvent;

/**
 * 调节的策略类
 *
 * @author : Rookiex
 * @version : 1.0
 */
public interface RegulateStrategy {

    /**
     * 调节方法
     */
    void regulate(DynamicDisruptor dynamicDisruptor, SentinelEvent sentinelEvent);

    /**
     * 获得调节的数量
     */
    int getNeedUpdateCount(SentinelEvent sentinelEvent);

    static void updateThreadCount(DynamicDisruptor dynamicDisruptor, int needUpdateCount) {
        if (needUpdateCount > 0) {
            for (int i = 0; i < needUpdateCount; i++) {
                dynamicDisruptor.incrConsumer();
            }
        } else if (needUpdateCount < 0) {
            for (int i = 0; i < -needUpdateCount; i++) {
                dynamicDisruptor.decrConsumer();
            }
        }
    }
}

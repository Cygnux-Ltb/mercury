package io.mercury.common.concurrent.disruptor.dynamic.strategy;

import io.mercury.common.concurrent.disruptor.dynamic.DynamicDisruptor;
import io.mercury.common.concurrent.disruptor.dynamic.sentinel.SentinelEvent;

/**
 * @author : Rookiex
 * @version :
 */
public class DerivativeStrategy implements RegulateStrategy {

    private int lastDifference = 0;

    @Override
    public void regulate(DynamicDisruptor dynamicDisruptor, SentinelEvent sentinelEvent) {
        RegulateStrategy.updateThreadCount(dynamicDisruptor, getNeedUpdateCount(sentinelEvent));
    }

    @Override
    public int getNeedUpdateCount(SentinelEvent sentinelEvent) {
        int updateCount = 0;
        if (lastDifference != 0) {
            int runThreadCount = sentinelEvent.getRunThreadCount();
            int totalThreadCount = sentinelEvent.getTotalThreadCount();
            if (totalThreadCount == runThreadCount) {
                int err = sentinelEvent.getTotalDifference() - lastDifference;
                int needUpdate = err * 100 / sentinelEvent.getRecentConsumeCount() * runThreadCount / 100;
                updateCount += needUpdate;
            }
        }
        lastDifference = sentinelEvent.getTotalDifference();
        return updateCount;
    }

}

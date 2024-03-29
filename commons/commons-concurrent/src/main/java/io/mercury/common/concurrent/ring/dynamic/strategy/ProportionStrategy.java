package io.mercury.common.concurrent.ring.dynamic.strategy;

import io.mercury.common.concurrent.ring.dynamic.DynamicDisruptor;
import io.mercury.common.concurrent.ring.dynamic.sentinel.SentinelEvent;

/**
 * @author : Rookiex
 * @version : 1.0
 * @date : Created in 2019/11/10 19:27
 * @desc : 比例调节策略,如果WINDOWS_COUNT时间窗口内不能处理玩所有消息,
 * 并且线程全部在运行中, 线程比例增加如果线程运行不满,
 * 并且总的任务剩余小于IDLE_THRESHOLD, 移除空闲状态线程
 */
public class ProportionStrategy implements RegulateStrategy {

    @Override
    public void regulate(DynamicDisruptor dynamicDisruptor, SentinelEvent sentinelEvent) {
        RegulateStrategy.updateThreadCount(dynamicDisruptor, getNeedUpdateCount(sentinelEvent));
    }

    @Override
    public int getNeedUpdateCount(SentinelEvent sentinelEvent) {
        int recentConsumeCount = sentinelEvent.getRecentConsumeCount();
        int recentProduceCount = sentinelEvent.getRecentProduceCount();

        // thread info
        int totalThreadCount = sentinelEvent.getTotalThreadCount();
        int runThreadCount = sentinelEvent.getRunThreadCount();

        int updateCount = 0;

        boolean isThreadRunOut = runThreadCount == totalThreadCount;
        if (isThreadRunOut) {
            if (recentProduceCount > recentConsumeCount) {
                // 保留两位小数
                int needAddThread = (recentProduceCount * 100 / recentConsumeCount * runThreadCount)
                        / 100 - runThreadCount;
                updateCount += needAddThread;
            }
        }

        return updateCount;
    }
}

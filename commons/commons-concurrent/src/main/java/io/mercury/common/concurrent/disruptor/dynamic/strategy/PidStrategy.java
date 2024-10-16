package io.mercury.common.concurrent.disruptor.dynamic.strategy;

import io.mercury.common.concurrent.disruptor.dynamic.DynamicDisruptor;
import io.mercury.common.concurrent.disruptor.dynamic.sentinel.SentinelEvent;

import static io.mercury.common.concurrent.disruptor.dynamic.strategy.RegulateStrategy.updateThreadCount;

/**
 * @author : Rookiex
 * @version : 1.0
 */
public class PidStrategy implements RegulateStrategy {

    private final ProportionStrategy proportionStrategy = new ProportionStrategy();
    private final IntegralStrategy integralStrategy = new IntegralStrategy();
    private final DerivativeStrategy derivativeStrategy = new DerivativeStrategy();
    private final SimpleStrategy simpleStrategy = new SimpleStrategy();

    private int p = 66;
    private int i = 100;
    private int d = 66;

    public void setPid(int p, int i, int d) {
        this.p = checkRange(p);
        this.i = checkRange(i);
        this.d = checkRange(d);
    }

    private int checkRange(int value) {
        if (value > 100) {
            return 100;
        } else if (value < 0) {
            return 0;
        }
        return value;
    }

    @Override
    public void regulate(DynamicDisruptor dynamicDisruptor, SentinelEvent sentinelEvent) {
        updateThreadCount(dynamicDisruptor, getNeedUpdateCount(sentinelEvent));
    }

    @Override
    public int getNeedUpdateCount(SentinelEvent sentinelEvent) {
        // 调用pid控制器
        int simpleCount = simpleStrategy.getNeedUpdateCount(sentinelEvent);
        int pCount = proportionStrategy.getNeedUpdateCount(sentinelEvent) * p / 100;
        int iCount = integralStrategy.getNeedUpdateCount(sentinelEvent) * i / 100;
        int dCount = derivativeStrategy.getNeedUpdateCount(sentinelEvent) * d / 100;
        System.out.println(
                " update p == " + pCount + " ,i == " + iCount + " ,d == " + dCount + " simpleCount == " + simpleCount);
        return simpleCount + pCount + iCount + dCount;
    }

}

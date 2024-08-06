package io.mercury.common.concurrent.disruptor.dynamic.core;

/**
 * @author : Rookiex
 * @version : 1.0
 */
public interface DynamicConsumer {

    /**
     * 添加消费者
     */
    void incrConsumer();

    /**
     * 减少消费者
     */
    void decrConsumer();

}

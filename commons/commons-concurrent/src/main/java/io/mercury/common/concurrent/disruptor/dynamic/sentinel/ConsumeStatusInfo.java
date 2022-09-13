package io.mercury.common.concurrent.disruptor.dynamic.sentinel;

/**
 * @author : Rookiex
 * @version : 1.0
 * @date : Created in 2019/11/11 15:32
 * @describe :
 */
public interface ConsumeStatusInfo {

    void addConsumeCount();

    void addProduceCount();

}

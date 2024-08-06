package io.mercury.common.concurrent.ring.dynamic.sentinel;

/**
 * @author : Rookiex
 * @version : 1.0
 */
public interface ConsumeStatusInfo {

    void addConsumeCount();

    void addProduceCount();

}

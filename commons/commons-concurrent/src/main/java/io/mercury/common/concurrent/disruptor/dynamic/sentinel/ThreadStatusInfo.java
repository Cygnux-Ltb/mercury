package io.mercury.common.concurrent.disruptor.dynamic.sentinel;

/**
 * @author : Rookiex
 * @version : 1.0
 */
public interface ThreadStatusInfo {

    void threadRun();

    void threadWait();

    void threadReady();

    void threadShutdown();

}

package io.mercury.common.concurrent.disruptor.dynamic.sentinel;

/**
 * @author : Rookiex
 * @version : 1.0
 * @date : Created in 2019/11/11 15:17
 * @describe :
 */
public interface ThreadStatusInfo {

    void threadRun();

    void threadWait();

    void threadReady();

    void threadShutDown();

}

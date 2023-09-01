package io.mercury.common.concurrent.ring.dynamic.sentinel;

/**
 * @author : Rookiex
 * @version : 1.0
 * @date : Created in 2019/11/11 15:17
 * @desc :
 */
public interface ThreadStatusInfo {

    void threadRun();

    void threadWait();

    void threadReady();

    void threadShutdown();

}

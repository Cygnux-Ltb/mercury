package io.mercury.common.concurrent.ring.dynamic.sentinel;

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

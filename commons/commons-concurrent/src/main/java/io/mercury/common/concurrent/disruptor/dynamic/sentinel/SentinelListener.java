package io.mercury.common.concurrent.disruptor.dynamic.sentinel;

/**
 * @author : Rookiex
 * @version :
 */
public interface SentinelListener {

    void notice(SentinelEvent event);

}

package io.mercury.common.concurrent.ring.dynamic.sentinel;

/**
 * @author : Rookiex
 * @version :
 */
public interface SentinelListener {

    void notice(SentinelEvent event);

}

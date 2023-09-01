package io.mercury.common.concurrent.ring.dynamic.sentinel;

/**
 * @author : Rookiex
 * @version :
 * @date : Created in 2019/11/8 15:50
 * @desc :
 */
public interface SentinelListener {

    void notice(SentinelEvent event);

}

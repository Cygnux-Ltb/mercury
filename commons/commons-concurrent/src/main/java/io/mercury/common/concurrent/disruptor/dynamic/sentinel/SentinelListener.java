package io.mercury.common.concurrent.disruptor.dynamic.sentinel;

/**
 * @author : Rookiex
 * @version :
 * @date : Created in 2019/11/8 15:50
 * @describe :
 */
public interface SentinelListener {

    void notice(SentinelEvent event);

}

package io.mercury.common.disruptor.dynamic.sentinel;

/**
 * @Author : Rookiex
 * @Date : Created in 2019/11/8 15:50
 * @Describe :
 * @version:
 */
public interface SentinelListener {
	
    void notice(SentinelEvent sentinelEvent);
    
}

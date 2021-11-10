package io.mercury.common.concurrent.disruptor;

import java.util.function.BiConsumer;

/**
 * 此接口实现需要负责输入参数到缓冲区事件的转换
 * 
 * @author yellow013
 *
 * @param <E> 从RingBuffer中取得的事件
 * @param <I> 输入的参数
 */
@FunctionalInterface
public interface RingEventPublisher<E, I> extends BiConsumer<E, I> {

}

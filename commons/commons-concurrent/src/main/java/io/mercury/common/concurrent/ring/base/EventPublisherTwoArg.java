package io.mercury.common.concurrent.ring.base;

import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.RingBuffer;

import javax.annotation.Nonnull;

/**
 * 事件发布者, 用于将输入<A0>, <A1>类型参数转换为<E>类型事件, 并调用RingBuffer对象的publishEvent函数
 * <p>
 * 并负责传递EventTranslator实现
 *
 * @param <E>
 * @param <A0>
 * @param <A1>
 * @author yellow013
 */
public final class EventPublisherTwoArg<E, A0, A1> {

    private final RingBuffer<E> ringBuffer;

    private final EventTranslatorTwoArg<E, A0, A1> translator;

    public EventPublisherTwoArg(@Nonnull RingBuffer<E> ringBuffer,
                                @Nonnull EventTranslatorTwoArg<E, A0, A1> translator) {
        this.ringBuffer = ringBuffer;
        this.translator = translator;
    }

    public void publish(A0 arg0, A1 arg1) {
        ringBuffer.publishEvent(translator, arg0, arg1);
    }

}

package io.mercury.common.concurrent.disruptor;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

import javax.annotation.Nonnull;

/**
 * 内部发布者, 用于调用RingBuffer对象的publishEvent函数,
 * <p>
 * 并负责传递EventTranslator实现
 *
 * @author yellow013
 */
public class EventPublisherWrapper<E, I> {

    private final RingBuffer<E> ringBuffer;

    private final EventTranslatorOneArg<E, I> translator;

    public EventPublisherWrapper(@Nonnull RingBuffer<E> ringBuffer,
                                 @Nonnull EventTranslatorOneArg<E, I> translator) {
        this.ringBuffer = ringBuffer;
        this.translator = translator;
    }

    public void handle(I in) {
        ringBuffer.publishEvent(translator, in);
    }

}

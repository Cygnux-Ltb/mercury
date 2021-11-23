package io.mercury.common.concurrent.disruptor;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

/**
 * 内部发布者, 用于调用RingBuffer对象的publishEvent函数,
 * 
 * 并负责传递EventTranslator实现
 * 
 * @author yellow013
 */
public class EventPublisherWrapper<E, I> {

	private final RingBuffer<E> ringBuffer;

	private final EventTranslatorOneArg<E, I> translator;

	public EventPublisherWrapper(RingBuffer<E> ringBuffer, EventTranslatorOneArg<E, I> translator) {
		this.ringBuffer = ringBuffer;
		this.translator = translator;
	}

	public static <E, I> EventPublisherWrapper<E, I> newInstance(RingBuffer<E> ringBuffer,
			EventTranslatorOneArg<E, I> translator) {
		return new EventPublisherWrapper<>(ringBuffer, translator);
	}

	public void handle(I in) {
		ringBuffer.publishEvent(translator, in);
	}

}

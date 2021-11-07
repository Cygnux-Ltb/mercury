package io.mercury.common.concurrent.disruptor;

import java.util.function.BiFunction;

@FunctionalInterface
public interface RingEventSetter<T extends RingEvent, A> extends BiFunction<T, A, T> {

	@Override
	T apply(T t, A a);

}

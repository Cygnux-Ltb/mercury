package io.mercury.common.disruptor;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface RingEventSetter<T extends RingEvent> extends UnaryOperator<T> {

}

package io.mercury.transport.rabbitmq.consumer;

import java.util.function.Predicate;

/**
 * @author xuejian.sun
 * @date 2019/01/17 15:46
 */
@FunctionalInterface
public interface RefreshNowEvent<T> extends Predicate<T> {

	boolean flushNow(T obj);

	@Override
	default boolean test(T t) {
		return flushNow(t);
	}

}

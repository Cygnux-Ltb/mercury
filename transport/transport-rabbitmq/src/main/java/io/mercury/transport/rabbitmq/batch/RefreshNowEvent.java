package io.mercury.transport.rabbitmq.batch;

import java.util.function.Predicate;

/**
 * 
 * @author yellow013
 */
@FunctionalInterface
public interface RefreshNowEvent<T> extends Predicate<T> {

	boolean flushNow(T obj);

	@Override
	default boolean test(T t) {
		return flushNow(t);
	}

}

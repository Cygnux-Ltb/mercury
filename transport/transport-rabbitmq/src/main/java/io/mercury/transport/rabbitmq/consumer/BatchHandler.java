package io.mercury.transport.rabbitmq.consumer;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * @author xuejian.sun
 * @date 2018/11/20 11:28
 */
@FunctionalInterface
public interface QosBatchHandler<T> extends Predicate<Collection<T>> {

	boolean handle(Collection<T> t);

	@Override
	default boolean test(Collection<T> t) {
		return handle(t);
	}

}

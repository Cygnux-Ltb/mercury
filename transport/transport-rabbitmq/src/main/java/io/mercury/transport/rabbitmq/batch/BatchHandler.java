package io.mercury.transport.rabbitmq.batch;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * @author yellow013
 * @date 2019.03.15
 */
@FunctionalInterface
public interface BatchHandler<T> extends Predicate<Collection<T>> {

	boolean handle(Collection<T> t);

	@Override
	default boolean test(Collection<T> t) {
		return handle(t);
	}

}

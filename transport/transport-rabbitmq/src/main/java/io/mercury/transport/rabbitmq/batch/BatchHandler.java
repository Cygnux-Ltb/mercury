package io.mercury.transport.rabbitmq.batch;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * @author yellow013
 */
@FunctionalInterface
public interface BatchHandler<T> extends Predicate<Collection<T>> {

	boolean handle(Collection<T> collection);

	@Override
	default boolean test(Collection<T> collection) {
		return handle(collection);
	}

}

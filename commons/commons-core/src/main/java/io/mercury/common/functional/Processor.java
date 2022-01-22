package io.mercury.common.functional;

import javax.annotation.Nonnull;

import io.mercury.common.collections.queue.LoadContainer;

/**
 * 
 * @author yellow013
 *
 * @param <T>
 */
@FunctionalInterface
public interface Processor<T> {

	/**
	 * 
	 * @param t
	 * @throws Exception
	 */
	void process(@Nonnull T t) throws Exception;

	@FunctionalInterface
	public static interface LoadContainerProcessor<E> extends Processor<LoadContainer<E>> {

		@Override
		void process(LoadContainer<E> t) throws Exception;

	}

}

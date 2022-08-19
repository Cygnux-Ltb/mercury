package io.mercury.common.functional;

import io.mercury.common.collections.queue.LoadContainer;

import javax.annotation.Nonnull;

/**
 * @param <T>
 * @author yellow013
 */
@FunctionalInterface
public interface Processor<T> {

    /**
     * @param t T
     */
    void process(@Nonnull T t) throws Exception;

    @FunctionalInterface
    interface LoadContainerProcessor<E> extends Processor<LoadContainer<E>> {

        @Override
        void process(@Nonnull LoadContainer<E> t) throws Exception;

    }

}

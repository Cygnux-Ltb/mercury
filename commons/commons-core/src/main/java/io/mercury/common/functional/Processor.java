package io.mercury.common.functional;

import javax.annotation.Nonnull;

import io.mercury.common.collections.queue.LoadContainer;

/**
 * @param <T>
 * @author yellow013
 */
@FunctionalInterface
public interface Processor<T> {

    /**
     * @param t
     * @throws Exception
     */
    void process(@Nonnull T t) throws Exception;

    @FunctionalInterface
    interface LoadContainerProcessor<E> extends Processor<LoadContainer<E>> {

        @Override
        void process(@Nonnull LoadContainer<E> t) throws Exception;

    }

}

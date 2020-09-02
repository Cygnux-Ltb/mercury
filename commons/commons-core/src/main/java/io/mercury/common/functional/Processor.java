package io.mercury.common.functional;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface Processor<T> {

	void process(@Nonnull T t) throws Exception;

}

package io.mercury.common.serialization.api;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface Serializer<T, R> {

	@Nonnull
	R serialization(@Nonnull T source);

}

package io.mercury.common.serialization.base;

import javax.annotation.Nullable;

@FunctionalInterface
public interface Serializer<T, R> {

	@Nullable
	R serialization(@Nullable T source);

}

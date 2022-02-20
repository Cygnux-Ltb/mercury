package io.mercury.common.serialization;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@FunctionalInterface
public interface Serializer<T, R> {

	@Nullable
	R serialization(@Nonnull T source);

}

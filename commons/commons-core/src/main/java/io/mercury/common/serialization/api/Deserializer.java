package io.mercury.common.serialization.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@FunctionalInterface
public interface Deserializer<T, R> {

	@Nonnull
	default R deserialization(@Nonnull T source) {
		return deserialization(source, null);
	}

	@Nonnull
	R deserialization(@Nonnull T source, @Nullable R reuse);

}

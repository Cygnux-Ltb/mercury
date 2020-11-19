package io.mercury.common.serialization;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@FunctionalInterface
public interface Deserializer<I, O> {

	@Nonnull
	default O deserialization(@Nonnull I source) {
		return deserialization(null, source);
	}

	@Nonnull
	O deserialization(@Nullable O reuse, @Nonnull I source);

}

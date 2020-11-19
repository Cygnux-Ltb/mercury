package io.mercury.common.serialization;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface Serializer<I, O> {

	@Nonnull
	O serialization(@Nonnull I source);

}

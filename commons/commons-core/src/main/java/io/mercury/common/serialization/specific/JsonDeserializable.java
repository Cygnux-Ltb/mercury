package io.mercury.common.serialization;

import javax.annotation.Nonnull;

public interface JsonDeserializable<T extends JsonDeserializable<T>> {

	@Nonnull
	T fromJson(@Nonnull String json);

}

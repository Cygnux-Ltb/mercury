package io.mercury.common.serialization.specific;

import javax.annotation.Nonnull;

public interface JsonSerializable {

	@Nonnull
	String toJson();

}

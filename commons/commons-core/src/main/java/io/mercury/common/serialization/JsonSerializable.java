package io.mercury.common.serialization;

import javax.annotation.Nonnull;

public interface JsonSerializable {

	@Nonnull
	String toJson();

}

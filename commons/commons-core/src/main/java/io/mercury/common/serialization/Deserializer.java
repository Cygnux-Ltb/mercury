package io.mercury.common.serialization;

@FunctionalInterface
public interface Deserializer<I, O> {

	default O deserialization(I source) {
		return deserialization(null, source);
	}

	O deserialization(O reuse, I source);

}

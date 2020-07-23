package io.mercury.common.serialization;

@FunctionalInterface
public interface Serializer<I, O> {

	O serialization(I source);

}

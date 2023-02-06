package io.mercury.common.codec.api;

@FunctionalInterface
public interface TextEncoder<T, C extends CharSequence> extends Encoder<T, C> {
}
package io.mercury.common.serialization;

@FunctionalInterface
public interface Copyable<T extends Copyable<T>> {

    void copyOf(T source);

}

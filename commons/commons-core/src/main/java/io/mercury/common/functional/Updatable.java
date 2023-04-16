package io.mercury.common.functional;

@FunctionalInterface
public interface Updatable<U extends Updatable<U, T>, T> {

    U updateWith(T in);

}

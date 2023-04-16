package io.mercury.common.functional;

@FunctionalInterface
public interface Updater<U extends Updater<U, T>, T> {

    U updateWith(T in);

}

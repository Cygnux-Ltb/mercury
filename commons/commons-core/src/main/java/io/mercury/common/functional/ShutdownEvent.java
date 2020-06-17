package io.mercury.common.functional;

import java.util.function.Consumer;

@FunctionalInterface
public interface ShutdownEvent<E extends Throwable> extends Consumer<E> {

}

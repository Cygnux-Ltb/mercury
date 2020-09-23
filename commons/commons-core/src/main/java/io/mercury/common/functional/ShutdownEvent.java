package io.mercury.common.functional;

import java.util.function.Consumer;

@FunctionalInterface
public interface ShutdownEvent extends Consumer<Throwable> {

}

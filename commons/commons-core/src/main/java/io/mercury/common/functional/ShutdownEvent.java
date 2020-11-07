package io.mercury.common.functional;

import java.util.function.Consumer;

/**
 * 
 * @author yellow013
 *
 */
@FunctionalInterface
public interface ShutdownEvent extends Consumer<Throwable> {

}

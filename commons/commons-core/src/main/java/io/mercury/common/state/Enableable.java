package io.mercury.common.state;

public interface Enableable {

    boolean isEnabled();

    default boolean isDisabled() {
        return !isEnabled();
    }

    boolean disable();

    boolean enable();

}
package io.mercury.common.fsm;

public interface Enable<T extends Enable<T>> {

	boolean isEnabled();

	default boolean isDisabled() {
		return !isEnabled();
	}

	T disable();

	T enable();

}
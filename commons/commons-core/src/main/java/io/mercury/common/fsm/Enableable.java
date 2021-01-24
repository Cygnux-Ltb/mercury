package io.mercury.common.fsm;

public interface Enableable {

	boolean isEnabled();

	default boolean isDisabled() {
		return !isEnabled();
	}

	boolean disable();

	boolean enable();

}
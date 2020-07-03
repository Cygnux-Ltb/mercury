package io.mercury.common.fsm;

import javax.annotation.concurrent.ThreadSafe;

import io.mercury.common.annotation.lang.AbstractFunction;

@ThreadSafe
public abstract class EnableComponent<T extends Enable<T>> implements Enable<T> {

	private volatile boolean isEnable;

	@Override
	public boolean isEnabled() {
		return isEnable;
	}

	@Override
	public boolean isDisabled() {
		return !isEnable;
	}

	@Override
	public T disable() {
		this.isEnable = false;
		return returnThis();
	}

	@Override
	public T enable() {
		this.isEnable = true;
		return returnThis();
	}

	@AbstractFunction
	protected abstract T returnThis();

}

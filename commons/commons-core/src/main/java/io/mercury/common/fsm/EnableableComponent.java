package io.mercury.common.fsm;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public abstract class EnableableComponent implements Enableable {

	private AtomicBoolean isEnable = new AtomicBoolean(false);

	@Override
	public boolean isEnabled() {
		return isEnable.get();
	}

	@Override
	public boolean enable() {
		return isEnable.compareAndSet(false, true);
	}

	@Override
	public boolean disable() {
		return isEnable.compareAndSet(true, false);
	}

}

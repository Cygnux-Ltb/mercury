package io.mercury.common.fsm;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public abstract class EnableableComponent implements Enableable {

    private final AtomicBoolean enabled = new AtomicBoolean(false);

    @Override
    public boolean isEnabled() {
        return enabled.get();
    }

    @Override
    public boolean enable() {
        return enabled.compareAndSet(false, true);
    }

    @Override
    public boolean disable() {
        return enabled.compareAndSet(true, false);
    }

}

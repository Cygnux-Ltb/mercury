package io.mercury.common.cache;

public final class CachedValue<T> {

    private volatile boolean available;
    private volatile T value;

    public CachedValue(boolean available, final T value) {

    }

}

package io.mercury.common.concurrent.list;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class CacheList<T> {

	private volatile Saved saved;

	private Supplier<List<T>> refresher;

	private class Saved {

		private volatile boolean available;
		private volatile List<T> value;

		private Saved(boolean available, List<T> value) {
			super();
			this.available = available;
			this.value = value;
		}

	}

	public CacheList(Supplier<List<T>> refresher) {
		if (refresher == null)
			throw new IllegalArgumentException("refresher is can't null...");
		this.refresher = refresher;
	}

	private CacheList<T> set(List<T> value) {
		this.saved = new Saved(true, value);
		return this;
	}

	public Optional<List<T>> get() {
		if (saved == null || !saved.available) {
			List<T> refreshed = refresher.get();
			return refreshed == null ? Optional.empty() : set(refreshed).get();
		} else
			// return saved.isAvailable ? Optional.of(saved.value) : get(key);
			return Optional.of(saved.value);
	}

	public CacheList<T> setUnavailable() {
		saved.available = false;
		return this;
	}

}

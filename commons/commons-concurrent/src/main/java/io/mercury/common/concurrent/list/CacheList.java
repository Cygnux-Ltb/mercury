package io.mercury.common.concurrent.list;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import javax.annotation.CheckForNull;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class CacheList<T> {

	private final AtomicReference<Saved> savedRef;

	private final Supplier<List<T>> refresher;

	private final class Saved {

		private volatile boolean available;
		private volatile List<T> value;

		private Saved(boolean available, List<T> value) {
			this.available = available;
			this.value = value;
		}

	}

	public CacheList(Supplier<List<T>> refresher) {
		if (refresher == null)
			throw new IllegalArgumentException("refresher is can't null...");
		this.refresher = refresher;
		this.savedRef = new AtomicReference<>(new Saved(true, refresher.get()));
	}

	/**
	 * 
	 * @param value
	 * @return
	 */

	/**
	 * 
	 * @return
	 */
	@CheckForNull
	public List<T> get() {
		return extractValue(savedRef.get());
	}

	private final List<T> extractValue(Saved saved) {
		if (saved.available) {
			return saved.value;
		} else {
			return extractValue(savedRef.updateAndGet(save -> {
				save.available = true;
				save.value = refresher.get();
				return save;
			}));
		}
	}

	/**
	 * 
	 * @return
	 */
	public CacheList<T> setUnavailable() {
		savedRef.get().available = false;
		savedRef.updateAndGet(save -> {
			save.available = false;
			return save;
		});
		return this;
	}

}

package io.mercury.common.concurrent.list;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import javax.annotation.CheckForNull;
import javax.annotation.concurrent.ThreadSafe;

/**
 * 
 * @author yellow013
 *
 * @param <T>
 */
@ThreadSafe
public final class CacheList<T> {

	private final AtomicReference<Saved> savedRef;

	private final Supplier<List<T>> refresher;

	/**
	 * 
	 * @author yellow013
	 *
	 */
	private final class Saved {

		private volatile boolean available;
		private volatile List<T> value;

		private Saved(boolean available, List<T> value) {
			this.available = available;
			this.value = value;
		}

	}

	/**
	 * 
	 * @param refresher
	 */
	public CacheList(Supplier<List<T>> refresher) {
		if (refresher == null)
			throw new IllegalArgumentException("refresher is can't null...");
		this.refresher = refresher;
		this.savedRef = new AtomicReference<>(new Saved(true, refresher.get()));
	}

	/**
	 * 
	 * @return
	 */
	@CheckForNull
	public List<T> get() {
		return extractValue(savedRef.get());
	}

	/**
	 * 
	 * @param saved
	 * @return
	 */
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
		savedRef.updateAndGet(save -> {
			save.available = false;
			return save;
		});
		return this;
	}

}

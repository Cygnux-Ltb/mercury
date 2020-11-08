package io.mercury.common.collections.customize;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public interface Keeper<K, V> {

	@Nonnull
	V acquire(@Nonnull K k);

	@CheckForNull
	V get(@Nonnull K k);

}

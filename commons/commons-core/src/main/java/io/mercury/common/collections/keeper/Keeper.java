package io.mercury.common.collections.keeper;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.io.Closeable;

/**
 * 
 * @author yellow013
 *
 * @param <K>
 * @param <V>
 */
@ThreadSafe
public interface Keeper<K, V> extends Closeable {

	@Nonnull
	V acquire(@Nonnull K k);

	@CheckForNull
	V get(@Nonnull K k);

}

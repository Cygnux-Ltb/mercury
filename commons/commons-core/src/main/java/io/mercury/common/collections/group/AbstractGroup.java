package io.mercury.common.collections.group;

import static io.mercury.common.collections.ImmutableMaps.newImmutableMap;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;

import org.eclipse.collections.api.map.ImmutableMap;

/**
 * 
 * @author yellow013
 *
 * @param <K>
 * @param <V>
 */
@ThreadSafe
@Immutable
public abstract class AbstractGroup<K, V> implements Group<K, V> {

	protected final ImmutableMap<K, V> savedMap;

	protected final Set<K> keys;

	public AbstractGroup(Supplier<Map<K, V>> supplier) {
		Map<K, V> map = supplier.get();
		if (map == null)
			throw new IllegalArgumentException("supplier result is null");
		this.keys = map.keySet();
		this.savedMap = newImmutableMap(supplier);
	}

	@Override
	public V getMember(K key) throws MemberNotExistException {
		V value = savedMap.get(key);
		if (value == null)
			throw new MemberNotExistException("key -> [" + key + "] no found value");
		return value;
	}

	@Override
	public Set<K> getKeys() {
		return keys;
	}

}

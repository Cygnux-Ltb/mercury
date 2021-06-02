package io.mercury.common.group;

import static io.mercury.common.collections.ImmutableLists.newImmutableList;
import static io.mercury.common.collections.MutableMaps.newConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ConcurrentMutableMap;

import io.mercury.common.annotation.lang.AbstractFunction;
import io.mercury.common.collections.Capacity;

/**
 * 
 * @author yellow013
 *
 * @param <K>
 * @param <V>
 */
@ThreadSafe
public abstract class AbstractGroup<K, V> implements Group<K, V> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8384604279192455942L;

	protected final ConcurrentMutableMap<K, V> savedMap = newConcurrentHashMap(Capacity.L04_SIZE);

	@Override
	public V acquireMember(K key) {
		return savedMap.getIfAbsentPutWithKey(key, this::createMember);
	}

	@Override
	public ImmutableList<V> getMemberList() {
		return newImmutableList(savedMap.values());
	}

	@Nonnull
	@AbstractFunction
	protected abstract V createMember(@Nonnull K key);

}

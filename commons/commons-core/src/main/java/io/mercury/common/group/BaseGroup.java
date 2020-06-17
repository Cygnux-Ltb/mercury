package io.mercury.common.group;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ConcurrentMutableMap;

import io.mercury.common.annotation.lang.ProtectedAbstractMethod;
import io.mercury.common.collections.Capacity;
import io.mercury.common.collections.ImmutableLists;
import io.mercury.common.collections.MutableMaps;

@ThreadSafe
public abstract class BaseGroup<K, V> implements Group<K, V> {

	protected ConcurrentMutableMap<K, V> savedMap = MutableMaps.newConcurrentHashMap(Capacity.L04_SIZE_16);

	@Override
	public V acquireMember(K k) {
		return savedMap.getIfAbsentPutWithKey(k, this::createMember);
	}

	@Override
	public ImmutableList<V> memberList() {
		return ImmutableLists.newList(savedMap.values());
	}

	@Nonnull
	@ProtectedAbstractMethod
	protected abstract V createMember(@Nonnull K k);

}

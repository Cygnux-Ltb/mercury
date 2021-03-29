package io.mercury.common.concurrent.counter;

import javax.annotation.concurrent.ThreadSafe;

import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.api.set.ImmutableSet;

import io.mercury.common.annotation.thread.LockHeld;
import io.mercury.common.collections.MutableMaps;

/**
 * 
 * 去除重复数据的计数器组
 * 
 * @author yellow013
 *
 * @param <T>
 */

@ThreadSafe
public final class DeduplicationCounterGroup<T extends Comparable<T>> {

	private DeduplicationCounter<T> counter = new DeduplicationCounter<>();
	private MutableIntObjectMap<DeduplicationCounter<T>> groupCounterMap = MutableMaps.newIntObjectHashMap();

	/**
	 * 
	 * @return
	 */
	public DeduplicationCounter<T> getCounter() {
		return counter;
	}

	/**
	 * 
	 * @param groupId
	 * @return
	 */
	@LockHeld
	public synchronized DeduplicationCounter<T> getCounterByGroup(int groupId) {
		DeduplicationCounter<T> counter = groupCounterMap.get(groupId);
		if (counter == null) {
			counter = new DeduplicationCounter<>();
			groupCounterMap.put(groupId, counter);
		}
		return counter;
	}

	public void putEvent(int groupId, T event) {
		counter.add(event);
		getCounterByGroup(groupId).add(event);
	}

	public ImmutableSet<T> getDeRepeatSet() {
		return counter.getDeRepeatSet();
	}

	public ImmutableSet<T> getDeRepeatSet(int groupId) {
		return getCounterByGroup(groupId).getDeRepeatSet();
	}

}

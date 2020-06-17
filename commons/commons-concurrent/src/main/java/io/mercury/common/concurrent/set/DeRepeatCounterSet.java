package io.mercury.common.concurrent.set;

import javax.annotation.concurrent.ThreadSafe;

import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.api.set.ImmutableSet;

import io.mercury.common.annotation.thread.LockHeld;
import io.mercury.common.collections.MutableMaps;
import io.mercury.common.concurrent.counter.SyncDeRepeatCounter;

/**
 * 
 * @author yellow013
 *
 * @param <T>
 */

@ThreadSafe
public final class DeRepeatCounterSet<T> {

	private SyncDeRepeatCounter<T> counter = new SyncDeRepeatCounter<>();
	private MutableIntObjectMap<SyncDeRepeatCounter<T>> groupCounterMap = MutableMaps.newIntObjectHashMap();

	public SyncDeRepeatCounter<T> getCounter() {
		return counter;
	}

	@LockHeld
	public synchronized SyncDeRepeatCounter<T> getCounterByGroup(int groupId) {
		SyncDeRepeatCounter<T> counter = groupCounterMap.get(groupId);
		if (counter == null) {
			counter = new SyncDeRepeatCounter<>();
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

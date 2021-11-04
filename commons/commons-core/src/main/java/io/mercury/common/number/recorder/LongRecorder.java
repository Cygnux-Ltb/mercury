package io.mercury.common.number.recorder;

import javax.annotation.concurrent.NotThreadSafe;

import org.eclipse.collections.api.map.primitive.MutableLongIntMap;
import org.eclipse.collections.api.set.primitive.MutableLongSet;

import io.mercury.common.collections.Capacity;
import io.mercury.common.collections.MutableMaps;
import io.mercury.common.collections.MutableSets;

@NotThreadSafe
public final class LongRecorder {

	private MutableLongIntMap longCounter;
	private MutableLongSet longSet;

	public LongRecorder(Capacity capacity) {
		this.longCounter = MutableMaps.newLongIntHashMap(capacity.value());
		this.longSet = MutableSets.newLongHashSet(capacity);
	}

	public void put(long value) {
		int count = longCounter.get(value);
		if (count == 0)
			longSet.add(value);
		longCounter.put(value, ++count);
	}

	public void remove(long value) {
		int count = longCounter.get(value);
		if (count == 0)
			return;
		if (count == 1) {
			longSet.remove(value);
			longCounter.remove(value);
		} else
			longCounter.put(value, --count);
	}

	public long max() {
		return longSet.maxIfEmpty(Long.MIN_VALUE);
	}

	public long min() {
		return longSet.minIfEmpty(Long.MAX_VALUE);
	}

}

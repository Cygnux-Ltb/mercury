package io.mercury.common.number.recorder;

import io.mercury.common.collections.Capacity;
import io.mercury.common.collections.MutableMaps;
import io.mercury.common.collections.MutableSets;
import org.eclipse.collections.api.map.primitive.MutableIntIntMap;
import org.eclipse.collections.api.set.primitive.MutableIntSet;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public final class IntRecorder {

	private final MutableIntIntMap intCounter;
	private final MutableIntSet intSet;

	public IntRecorder(Capacity capacity) {
		this.intCounter = MutableMaps.newIntIntHashMap(capacity.value());
		this.intSet = MutableSets.newIntHashSet(capacity);
	}

	public void put(int value) {
		int count = intCounter.get(value);
		if (count == 0)
			intSet.add(value);
		intCounter.put(value, ++count);
	}

	public void remove(int value) {
		int count = intCounter.get(value);
		if (count == 0)
			return;
		if (count == 1) {
			intSet.remove(value);
			intCounter.remove(value);
		} else
			intCounter.put(value, --count);
	}

	public int max() {
		return intSet.maxIfEmpty(Integer.MIN_VALUE);
	}

	public int min() {
		return intSet.minIfEmpty(Integer.MAX_VALUE);
	}

}

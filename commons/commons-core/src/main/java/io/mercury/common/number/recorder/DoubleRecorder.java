package io.mercury.common.number.recorder;

import javax.annotation.concurrent.NotThreadSafe;

import org.eclipse.collections.api.map.primitive.MutableDoubleIntMap;
import org.eclipse.collections.api.set.primitive.MutableDoubleSet;

import io.mercury.common.collections.Capacity;
import io.mercury.common.collections.MutableMaps;
import io.mercury.common.collections.MutableSets;

/**
 * 有序的记录N个double数值并排序<br>
 * 用于查询最大和最小的值<br>
 * 可以记录重复的值和删除过期的值<br>
 * 
 * @author yellow013
 * 
 */

@NotThreadSafe
public final class DoubleRecorder {

	private MutableDoubleIntMap doubleCounter;
	private MutableDoubleSet doubleSet;

	public DoubleRecorder(Capacity capacity) {
		this.doubleCounter = MutableMaps.newDoubleIntHashMap(capacity);
		this.doubleSet = MutableSets.newDoubleHashSet(capacity);
	}

	/**
	 * 
	 * @param d
	 */
	public void put(double d) {
		int count = doubleCounter.get(d);
		if (count == 0)
			doubleSet.add(d);
		doubleCounter.put(d, ++count);
	}

	public void remove(double value) {
		int count = doubleCounter.get(value);
		if (count == 0)
			return;
		if (count == 1) {
			doubleSet.remove(value);
			doubleCounter.remove(value);
		} else
			doubleCounter.put(value, --count);
	}

	public double max() {
		return doubleSet.maxIfEmpty(Double.MIN_VALUE);
	}

	public double min() {
		return doubleSet.minIfEmpty(Double.MAX_VALUE);
	}

}

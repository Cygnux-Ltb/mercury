package io.mercury.common.concurrent.map;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import org.eclipse.collections.api.block.procedure.primitive.LongProcedure;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.primitive.MutableLongObjectMap;
import org.eclipse.collections.api.set.primitive.MutableLongSet;

import io.mercury.common.collections.Capacity;
import io.mercury.common.collections.MutableLists;
import io.mercury.common.collections.MutableMaps;
import io.mercury.common.collections.MutableSets;

/**
 *
 * @author yellow013
 *
 * @param <V>
 */
@ThreadSafe
public final class ConcurrentLongRangeMap<V> {

	private MutableLongObjectMap<V> savedMap;
	private MutableLongSet savedKey;

	public ConcurrentLongRangeMap() {
		this(Capacity.L06_SIZE_64);
	}

	public ConcurrentLongRangeMap(Capacity capacity) {
		this.savedMap = MutableMaps.newLongObjectHashMap(capacity);
		this.savedKey = MutableSets.newLongHashSet(capacity);

	}

	@Nonnull
	public synchronized ConcurrentLongRangeMap<V> put(long key, V value) {
		savedMap.put(key, value);
		savedKey.add(key);
		return this;
	}

	@CheckForNull
	public V get(long key) {
		return savedMap.get(key);
	}

	@CheckForNull
	public synchronized V remove(long key) {
		savedKey.remove(key);
		return savedMap.remove(key);
	}

	@Nonnull
	public MutableList<V> getAll() {
		return MutableLists.newFastList(savedMap.values());
	}

	public synchronized void clear() {
		savedMap.clear();
		savedKey.clear();
	}

	@Nonnull
	public synchronized MutableList<V> scan(long startPoint, long endPoint) {
		MutableLongSet selectKey = selectKey(startPoint, endPoint);
		MutableList<V> selected = MutableLists.newFastList(selectKey.size());
		operatingSelect(selectKey, key -> selected.add(get(key)));
		return selected;
	}

	@Nonnull
	public synchronized MutableList<V> remove(long startPoint, long endPoint) {
		MutableLongSet selectKey = selectKey(startPoint, endPoint);
		MutableList<V> removed = MutableLists.newFastList(selectKey.size());
		operatingSelect(selectKey, key -> removed.add(remove(key)));
		return removed;
	}

	// TODO 性能优化
	public MutableLongSet selectKey(long startPoint, long endPoint) {
//		MutableLongIterator longIterator = savedKey.longIterator();
//		MutableLongSet longHashSet = MutableSets.newLongHashSet(512);
//		while (longIterator.hasNext()) {
//			long next = longIterator.next();
//			if (next >= startPoint && next <= endPoint)
//				longHashSet.add(next);
//		}
//		return longHashSet;
		return savedKey.select(key -> key >= startPoint && key <= endPoint, MutableSets.newLongHashSet(Capacity.L06_SIZE_64));
	}

	private void operatingSelect(MutableLongSet selectKey, LongProcedure func) {
		if (!selectKey.isEmpty())
			selectKey.each(key -> func.accept(key));
	}

	public static void main(String[] args) {

		long startNano = System.nanoTime();
		ConcurrentLongRangeMap<String> longRangeMap = new ConcurrentLongRangeMap<>(Capacity.L24_SIZE_16777216);
		for (long l = 0L; l < 10000L; l++) {
			longRangeMap.put(l, "l == " + l);
		}
		long endNano = System.nanoTime();
		System.out.println((endNano - startNano) / 1000000);

		for (int i = 0; i < 200; i++) {
			long startNano1 = System.nanoTime();
			for (long l = 1000L; l < 1300L; l++) {
				@SuppressWarnings("unused")
				MutableLongSet selectKey = longRangeMap.selectKey(0, l);
				// longRangeMap.get(1000, l);
			}
			long endNano1 = System.nanoTime();
			System.out.println((endNano1 - startNano1) / 1000000);
		}

//		MutableMap<LocalDate, String> unifiedMap = MutableMaps.newUnifiedMap();
//
//		unifiedMap.put(LocalDate.now(), "AAAA");
//		unifiedMap.put(LocalDate.now(), "BBBB");
//		System.out.println(unifiedMap.get(LocalDate.now()));
//		System.out.println(LocalDate.now().hashCode());
//		System.out.println(LocalDate.now().hashCode());
//		
//		System.out.println(LocalDate.now() == LocalDate.now());
	}

}

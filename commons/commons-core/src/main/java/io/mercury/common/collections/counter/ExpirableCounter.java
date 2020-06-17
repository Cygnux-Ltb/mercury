package io.mercury.common.collections.counter;

import java.time.Duration;

import javax.annotation.concurrent.NotThreadSafe;

import org.eclipse.collections.api.iterator.MutableLongIterator;
import org.eclipse.collections.api.list.primitive.MutableLongList;
import org.eclipse.collections.api.map.primitive.MutableLongLongMap;

import io.mercury.common.collections.Capacity;
import io.mercury.common.collections.MutableLists;
import io.mercury.common.collections.MutableMaps;

/**
 * 
 * 具备过期特性的累加计数器, 可以清除某个特定delta, 是计算单位时间窗口的閥值时较为有效的结构<br>
 * 累加值失效有两种情况, 调用removeHistoryDelta对value进行修正, 或在过期后自动失效<br>
 * 采用惰性求值, 只在获取值的时候排除已过期的值<br>
 * 未进行堆外缓存, 仅在当前JVM进程内有效, JVM重启后, 计数器归零
 * 
 * @author yellow013
 *
 */
@NotThreadSafe
public final class ExpirableCounter implements Counter<ExpirableCounter> {

	private long value = 0L;

	// TODO 增加强一致性 使用自旋锁
	// private AtomicBoolean spin;

	private MutableLongLongMap timeToTag;
	private MutableLongLongMap tagToDelta;
	private MutableLongList effectiveTimes;

	private long expireNanos;

	public ExpirableCounter(Duration expireTime, Capacity capacity) {
		this.expireNanos = expireTime.toNanos();
		this.timeToTag = MutableMaps.newLongLongHashMap(capacity);
		this.tagToDelta = MutableMaps.newLongLongHashMap(capacity);
		this.effectiveTimes = MutableLists.newLongArrayList(capacity.size());
	}

	private void updateValue(long delta) {
		value += delta;
	}

	@Override
	public ExpirableCounter add(long tag, long delta) {
		if (!tagToDelta.containsKey(tag)) {
			long time = System.nanoTime();
			effectiveTimes.add(time);
			timeToTag.put(time, tag);
			tagToDelta.put(tag, delta);
			updateValue(delta);
		}
		return this;
	}

	@Override
	public long getValue() {
		long baselineTime = System.nanoTime() - expireNanos;
		MutableLongIterator effectiveTimeIterator = effectiveTimes.longIterator();
		while (effectiveTimeIterator.hasNext()) {
			long time = effectiveTimeIterator.next();
			if (time < baselineTime) {
				clear(time);
				effectiveTimeIterator.remove();
			} else
				break;
		}
		return value;
	}

	private void clear(long time) {
		long tag = timeToTag.get(time);
		long delta = tagToDelta.get(tag);
		updateValue(-delta);
		timeToTag.remove(time);
		tagToDelta.remove(tag);
	}

	@Override
	public ExpirableCounter remove(long tag) {
		long delta = tagToDelta.get(tag);
		if (delta == 0)
			return this;
		tagToDelta.remove(tag);
		value -= delta;
		return this;
	}

	@Override
	public ExpirableCounter historyDeltaAdd(long tag, long delta) {
		long savedDelta = tagToDelta.get(tag);
		if (savedDelta == 0)
			return this;
		tagToDelta.put(tag, savedDelta - delta);
		value -= delta;
		return this;
	}

	public static void main(String[] args) {

//		LazyLoadingExpirationCounter counter = new LazyLoadingExpirationCounter(Duration.ofMillis(10000), 1024);
//
//		for (int i = 0; i < 20; i++) {
//			counter.add(i, 10);
//			ThreadUtil.sleep(500);
//		}
//
//		for (int i = 0; i < 20; i++) {
//			System.out.println(counter.getValue());
//			ThreadUtil.sleep(2000);
//		}

		MutableLongLongMap map = MutableMaps.newLongLongHashMap(Capacity.L10_SIZE_1024);
		map.put(1, 10);
		System.out.println(-19 + -15);

	}

}

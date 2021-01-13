package io.mercury.common.collections.counter;

import static io.mercury.common.collections.MutableLists.newLongArrayList;
import static io.mercury.common.collections.MutableMaps.newLongLongHashMap;

import java.time.Duration;

import javax.annotation.concurrent.NotThreadSafe;

import org.eclipse.collections.api.iterator.MutableLongIterator;
import org.eclipse.collections.api.list.primitive.MutableLongList;
import org.eclipse.collections.api.map.primitive.MutableLongLongMap;

import io.mercury.common.collections.Capacity;
import io.mercury.common.collections.MutableMaps;
import io.mercury.common.thread.Threads;

/**
 * 
 * 具备过期特性的累加计数器, 可以清除某个特定delta<br>
 * 用于计算单位时间窗口的閥值, 最初设计用于限制单位时间的订单总量<br>
 * 累加值失效有两种情况, 调用remove对value进行修正, 或在过期后自动失效<br>
 * 采用惰性求值, 只在获取值的时候排除已过期的值<br>
 * 未进行堆外缓存, 仅在当前JVM进程内有效, JVM重启后, 计数器归零
 * 
 * TODO 增加强一致性, 使用自旋锁
 * 
 * @author yellow013
 *
 */
@NotThreadSafe
public final class ExpirableCounter implements Counter<ExpirableCounter> {

	private long value = 0L;

	// 记录时间和Tag
	private final MutableLongLongMap timeToTag;
	// 记录Tag和Delta
	private final MutableLongLongMap tagToDelta;
	// 存储Tag的记录时间
	private final MutableLongList effectiveTimes;

	// 有效时间的纳秒数
	private final long expireNanos;

	public ExpirableCounter(Duration expireTime) {
		this(expireTime, Capacity.L12_SIZE);
	}

	public ExpirableCounter(Duration expireTime, Capacity capacity) {
		this.expireNanos = expireTime.toNanos();
		this.timeToTag = newLongLongHashMap(capacity);
		this.tagToDelta = newLongLongHashMap(capacity);
		this.effectiveTimes = newLongArrayList(capacity.value());
	}

	/**
	 * 更新计算值
	 * 
	 * @param delta
	 */
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
		final long baseline = System.nanoTime() - expireNanos;
		final MutableLongIterator iterator = effectiveTimes.longIterator();
		while (iterator.hasNext()) {
			long time = iterator.next();
			if (time < baseline) {
				clear(time);
				iterator.remove();
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

	/**
	 * 
	 */
	@Override
	public ExpirableCounter deltaRemove(long tag) {
		long delta = tagToDelta.get(tag);
		if (delta == 0)
			return this;
		tagToDelta.remove(tag);
		value -= delta;
		return this;
	}

	/**
	 * 
	 */
	@Override
	public ExpirableCounter deltaAdd(long tag, long delta) {
		long savedDelta = tagToDelta.get(tag);
		if (savedDelta == 0)
			return this;
		tagToDelta.put(tag, savedDelta + delta);
		value += delta;
		return this;
	}

	public static void main(String[] args) {

		ExpirableCounter counter = new ExpirableCounter(Duration.ofMillis(10000), Capacity.L10_SIZE);

		for (int i = 0; i < 20; i++) {
			counter.add(i, 10);
			Threads.sleep(500);
		}

		for (int i = 0; i < 20; i++) {
			System.out.println(counter.getValue());
			Threads.sleep(2000);
		}

		MutableLongLongMap map = MutableMaps.newLongLongHashMap(Capacity.L10_SIZE);
		map.put(1, 10);
		System.out.println(-19 + -15);

	}

}

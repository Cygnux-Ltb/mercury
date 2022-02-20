package io.mercury.common.concurrent.counter;

import javax.annotation.concurrent.ThreadSafe;

import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.set.MutableSet;

import io.mercury.common.annotation.thread.LockHeld;
import io.mercury.common.collections.Capacity;
import io.mercury.common.collections.MutableSets;

/**
 * 去除重复数据的计数器, 使用Set去除重复
 * 
 * @author yellow013
 *
 * @param <T>
 */
@ThreadSafe
public final class DeduplicateCounter<T extends Comparable<T>> {

	private final MutableSet<T> deRepeatSet = MutableSets.newUnifiedSet(Capacity.L06_SIZE);

	private volatile int count;
	private final int initCount;

	private volatile boolean isArchived = false;

	public DeduplicateCounter() {
		this(0);
	}

	public DeduplicateCounter(int initCount) {
		this.initCount = initCount;
	}

	/**
	 * 
	 * @param t
	 * @return
	 */
	@LockHeld
	public synchronized DeduplicateCounter<T> add(T t) {
		if (deRepeatSet.add(t))
			count = deRepeatSet.size();
		return this;
	}

	/**
	 * 
	 * @param t
	 * @return
	 */
	@LockHeld
	public synchronized DeduplicateCounter<T> subtract(T t) {
		if (deRepeatSet.remove(t))
			count = deRepeatSet.size();
		return this;
	}

	/**
	 * 清空计数结果
	 * 
	 * @return
	 */
	@LockHeld
	public synchronized DeduplicateCounter<T> clear() {
		deRepeatSet.clear();
		count = 0;
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isArchived() {
		return isArchived;
	}

	/**
	 * 
	 * @return
	 */
	public ImmutableSet<T> getDeRepeatSet() {
		return deRepeatSet.toImmutable();
	}

	/**
	 * 
	 * @return
	 */
	public long count() {
		return initCount + count;
	}

	/**
	 * 清空数据, 但保留计数结果
	 * 
	 * @return
	 */
	public synchronized void archive() {
		this.isArchived = true;
		this.deRepeatSet.clear();
	}

	public static void main(String[] args) {

		DeduplicateCounter<String> deRepeatCounter = new DeduplicateCounter<>(100);
		System.out.println(deRepeatCounter.add("").add("fsdaf").add("dsfsad").add("").add("aaa").add("aaa").count());

	}

}

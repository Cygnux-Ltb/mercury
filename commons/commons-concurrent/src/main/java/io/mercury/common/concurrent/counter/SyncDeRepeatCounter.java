package io.mercury.common.concurrent.counter;

import javax.annotation.concurrent.ThreadSafe;

import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.set.MutableSet;

import io.mercury.common.annotation.thread.LockHeld;
import io.mercury.common.collections.Capacity;
import io.mercury.common.collections.MutableSets;

/**
 * 
 * 
 * @author yellow013
 *
 * @param <T>
 */
@ThreadSafe
public final class SyncDeRepeatCounter<T> {

	private MutableSet<T> deRepeatSet = MutableSets.newUnifiedSet(Capacity.L06_SIZE_64);
	private volatile int count;
	private final int initCount;

	// private boolean isArchived = false;

	public SyncDeRepeatCounter() {
		this(0);
	}

	public SyncDeRepeatCounter(int initCount) {
		this.initCount = initCount;
	}

	@LockHeld
	public synchronized SyncDeRepeatCounter<T> add(T t) {
		if (deRepeatSet.add(t))
			count = deRepeatSet.size();
		return this;
	}

	@LockHeld
	public synchronized SyncDeRepeatCounter<T> subtract(T t) {
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
	public synchronized SyncDeRepeatCounter<T> clear() {
		deRepeatSet.clear();
		count = 0;
		return this;
	}

	public ImmutableSet<T> getDeRepeatSet() {
		return deRepeatSet.toImmutable();
	}

	public long count() {
		return initCount + count;
	}

	/**
	 * 清空数据, 但保留计数结果
	 * 
	 * @return
	 */
	public synchronized void archive() {
		deRepeatSet.clear();
	}

	public static void main(String[] args) {

		SyncDeRepeatCounter<String> deRepeatCounter = new SyncDeRepeatCounter<String>(100);
		System.out.println(deRepeatCounter.add("").add("fsdaf").add("dsfsad").add("").add("aaa").add("aaa").count());

	}

}

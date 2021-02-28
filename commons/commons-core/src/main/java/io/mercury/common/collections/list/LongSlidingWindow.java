package io.mercury.common.collections.list;

import static io.mercury.common.collections.MutableLists.newLongArrayList;

import java.util.function.LongConsumer;

import javax.annotation.concurrent.NotThreadSafe;

import org.eclipse.collections.api.list.primitive.ImmutableLongList;
import org.eclipse.collections.api.list.primitive.MutableLongList;

/**
 * 
 * 内部包含Long原始类型的数组, 长度不可变
 * 
 * @author yellow013
 *
 */
@NotThreadSafe
public final class LongSlidingWindow {

	private final int capacity;
	private final MutableLongList list;

	private int tail = -1;
	private int count = 0;

	private boolean isEmpty = true;
	private boolean isFull = false;

	private LongSlidingWindow(int capacity) {
		this.capacity = capacity;
		this.list = newLongArrayList(capacity);
	}

	/**
	 * 
	 * @param capacity
	 * @return
	 */
	public static final LongSlidingWindow newWindow(int capacity) {
		return new LongSlidingWindow(capacity);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return isEmpty;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isFull() {
		return isFull;
	}

	/**
	 * 
	 * @return
	 */
	public long tail() {
		if (isEmpty)
			return 0L;
		return list.get(tail);
	}

	/**
	 * 
	 * @return
	 */
	public long head() {
		if (isEmpty)
			return 0L;
		if (isFull)
			return list.get(tail + 1 == capacity ? 0 : tail + 1);
		return list.get(tail - count + 1);
	}

	/**
	 * 
	 * @return
	 */
	public int count() {
		return count;
	}

	/**
	 * 
	 * @return
	 */
	public long sum() {
		return list.sum();
	}

	/**
	 * 
	 * @return
	 */
	public long max() {
		return list.max();
	}

	/**
	 * 
	 * @return
	 */
	public long min() {
		return list.min();
	}

	/**
	 * 
	 * @return
	 */
	public long average() {
		return (long) list.average();
	}

	/**
	 * 
	 * @return
	 */
	public long median() {
		return (long) list.median();
	}

	/**
	 * 
	 * @return
	 */
	public ImmutableLongList snapshot() {
		return list.toImmutable();
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public LongSlidingWindow addTail(long value) {
		updateTail(value);
		return this;
	}

	/**
	 * 
	 * @param value
	 */
	private void updateTail(long value) {
		updateTailIndex();
		updateStatus();
		if (isFull)
			list.set(tail, value);
		else
			list.add(value);
	}

	/**
	 * 
	 */
	private void updateTailIndex() {
		if (++tail == capacity)
			tail = 0;
	}

	/**
	 * 
	 */
	private void updateStatus() {
		if (!isFull) {
			if (count == capacity) {
				isFull = true;
				return;
			}
			count++;
		}
		if (isEmpty) {
			isEmpty = false;
		}
	}

	/**
	 * 
	 * @param consumer
	 */
	public void each(LongConsumer consumer) {
		list.each(value -> consumer.accept(value));
	}

	public static void main(String[] args) {

		LongSlidingWindow window = newWindow(10);

		for (int i = 1; i < 30; i++) {
			window.addTail(i);
			System.out.println("count -> " + window.count);
			System.out.print("values -> ");
			window.each(value -> System.out.print(value + " , "));
			System.out.println();
			System.out.println("head -> " + window.head());
			System.out.println("tail -> " + window.tail());
			System.out.println();
		}

	}

}

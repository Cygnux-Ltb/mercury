package io.mercury.common.collections.list;

import static io.mercury.common.collections.MutableLists.newLongArrayList;

import javax.annotation.concurrent.NotThreadSafe;

import org.eclipse.collections.api.list.primitive.ImmutableLongList;
import org.eclipse.collections.api.list.primitive.MutableLongList;

/**
 * 
 * @author yellow013
 *
 */
@NotThreadSafe
public class LongFixedLengthList {

	private int tail = -1;
	private int count = 0;

	private int capacity;

	private MutableLongList list;

	private boolean isEmpty = true;
	private boolean isFull = false;

	private LongFixedLengthList(int capacity) {
		this.capacity = capacity;
		this.list = newLongArrayList(capacity);
	}

	/**
	 * 
	 * @param cycle
	 * @return
	 */
	public static LongFixedLengthList newList(int size) {
		return new LongFixedLengthList(size);
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
	public ImmutableLongList toImmutable() {
		return list.toImmutable();
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
	 * @param value
	 * @return
	 */
	public LongFixedLengthList addTail(long value) {
		updateTail(value);
		return this;
	}

	/**
	 * 
	 * @param value
	 */
	private void updateTail(long value) {
		updateTailIndex();
		updateCount();
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
	private void updateCount() {
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

	public static void main(String[] args) {

		LongFixedLengthList list = newList(10);

		for (int i = 1; i < 30; i++) {
			list.addTail(i);
			System.out.println("Count -> " + list.count);
			System.out.print("Value -> ");
			list.list.each(value -> System.out.print(value + " , "));
			System.out.println();
			System.out.println("Head -> " + list.head());
			System.out.println("Tail -> " + list.tail());
		}

	}

}

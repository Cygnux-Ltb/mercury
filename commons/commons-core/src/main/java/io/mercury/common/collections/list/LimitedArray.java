package io.mercury.common.collections.list;

import java.util.function.IntFunction;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public final class LimitedArray<E> extends LimitedContainer<E> {

	private final E[] array;

	@SuppressWarnings("unchecked")
	public LimitedArray(int capacity) {
		super(capacity);
		this.array = (E[]) new Object[capacity];
	}

	public LimitedArray(int capacity, IntFunction<E[]> supplier) {
		super(capacity);
		final E[] array = supplier.apply(capacity);
		if (array.length != capacity)
			throw new IllegalStateException("setting capacity and array length not equal");
		this.array = array;
	}

	@Override
	protected void setTail(int tail, E e) {
		array[tail] = e;
	}

	@Override
	public E getTail() {
		return array[tailIndex()];
	}

	@Override
	public E getHead() {
		return array[headIndex()];
	}

}

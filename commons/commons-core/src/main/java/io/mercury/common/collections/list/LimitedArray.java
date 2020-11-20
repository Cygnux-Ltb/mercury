package io.mercury.common.collections.list;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public final class LimitedArray<E> extends LimitedContainer<E> {

	private E[] array;

	@SuppressWarnings("unchecked")
	public LimitedArray(int capacity) {
		super(capacity);
		this.array = (E[]) new Object[capacity];
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

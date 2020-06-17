package io.mercury.common.collections.list;

import java.lang.reflect.Array;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public final class LimitedArray<E> extends LimitedContainer<E> {

	private E[] array;

	@SuppressWarnings("unchecked")
	public LimitedArray(Class<E> type, int capacity) {
		super(capacity);
		this.array = (E[]) Array.newInstance(type, capacity);
	}

	@Override
	protected void setTail(int tail, E e) {
		array[tail] = e;
	}

	@Override
	public E getTail() {
		return array[getTailIndex()];
	}

	@Override
	public E getHead() {
		return array[getHeadIndex()];
	}

}

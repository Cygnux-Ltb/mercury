package io.mercury.common.collections.list;

import java.util.List;

import io.mercury.common.annotation.lang.ProtectedAbstractMethod;

public abstract class LimitedList<L extends List<E>, E> extends LimitedContainer<E> {

	private L innerList;

	public LimitedList(int capacity) {
		super(capacity);
		this.innerList = initList(capacity);
	}

	@ProtectedAbstractMethod
	protected abstract L initList(int capacity);

	@Override
	protected void setTail(int tail, E e) {
		innerList.set(tail, e);
	}

	@Override
	public E getTail() {
		return innerList.get(getTailIndex());
	}

	@Override
	public E getHead() {
		return innerList.get(getHeadIndex());
	}

}

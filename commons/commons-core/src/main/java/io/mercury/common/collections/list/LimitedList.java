package io.mercury.common.collections.list;

import java.util.List;

import io.mercury.common.annotation.lang.AbstractFunction;

public abstract class LimitedList<L extends List<E>, E> extends LimitedContainer<E> {

	private L savedList;

	public LimitedList(int capacity) {
		super(capacity);
		this.savedList = initList(capacity);
	}

	@AbstractFunction
	protected abstract L initList(int capacity);

	@Override
	protected void setTail(int tail, E e) {
		savedList.set(tail, e);
	}

	@Override
	public E getTail() {
		return savedList.get(getTailIndex());
	}

	@Override
	public E getHead() {
		return savedList.get(getHeadIndex());
	}

}

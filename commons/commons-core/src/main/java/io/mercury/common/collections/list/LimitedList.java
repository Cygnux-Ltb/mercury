package io.mercury.common.collections.list;

import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import io.mercury.common.annotation.AbstractFunction;

public abstract class LimitedList<L extends List<E>, E> extends LimitedContainer<E> {

	private L savedList;

	private LimitedList(int capacity) {
		super(capacity);
		this.savedList = initList(capacity);
	}

	/**
	 * 
	 * @param <E>
	 * @param capacity
	 * @return
	 */
	public final static <E> LimitedList<FastList<E>, E> newWithFastList(int capacity) {
		return new LimitedList<FastList<E>, E>(capacity) {
			@Override
			protected FastList<E> initList(int capacity) {
				return new FastList<>(capacity);
			}
		};
	}

	@AbstractFunction
	protected abstract L initList(int capacity);

	@Override
	protected void setTail(int tail, E e) {
		savedList.set(tail, e);
	}

	@Override
	public E getTail() {
		return savedList.get(tailIndex());
	}

	@Override
	public E getHead() {
		return savedList.get(headIndex());
	}

}

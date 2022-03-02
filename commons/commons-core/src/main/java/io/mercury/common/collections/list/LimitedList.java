package io.mercury.common.collections.list;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.collections.impl.list.mutable.FastList;

import io.mercury.common.annotation.AbstractFunction;

public abstract class LimitedList<L extends List<E>, E> extends LimitedContainer<E> {

	private final L list;

	protected LimitedList(int capacity) {
		super(capacity);
		this.list = newList(capacity);
	}

	/**
	 * 
	 * @param <E>
	 * @param capacity
	 * @return
	 */
	public static <E> LimitedFastList<E> newLimitedFastList(int capacity) {
		return new LimitedFastList<E>(capacity);
	}

	/**
	 * 
	 * @param <E>
	 * @param capacity
	 * @return
	 */
	public static <E> LimitedArrayList<E> newLimitedArrayList(int capacity) {
		return new LimitedArrayList<E>(capacity);
	}

	@AbstractFunction
	protected abstract L newList(int capacity);

	@Override
	protected void setTail(int tail, E e) {
		list.set(tail, e);
	}

	@Override
	public E getTail() {
		return list.get(tailIndex());
	}

	@Override
	public E getHead() {
		return list.get(headIndex());
	}

	/**
	 * 
	 * @author yellow013
	 *
	 * @param <E>
	 */
	public static class LimitedFastList<E> extends LimitedList<FastList<E>, E> {

		private LimitedFastList(int capacity) {
			super(capacity);
		}

		@Override
		protected FastList<E> newList(int capacity) {
			return new FastList<>(capacity);
		}

	}

	/**
	 * 
	 * @author yellow013
	 *
	 * @param <E>
	 */
	public static class LimitedArrayList<E> extends LimitedList<ArrayList<E>, E> {

		private LimitedArrayList(int capacity) {
			super(capacity);
		}

		@Override
		protected ArrayList<E> newList(int capacity) {
			return new ArrayList<>(capacity);
		}

	}

}

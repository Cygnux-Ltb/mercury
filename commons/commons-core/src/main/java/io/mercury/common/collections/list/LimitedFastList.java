package io.mercury.common.collections.list;

import javax.annotation.concurrent.NotThreadSafe;

import org.eclipse.collections.impl.list.mutable.FastList;

@NotThreadSafe
public final class LimitedFastList<E> extends LimitedList<FastList<E>, E> {

	public LimitedFastList(int capacity) {
		super(capacity);
	}

	@Override
	protected FastList<E> initList(int capacity) {
		return new FastList<>(capacity);
	}

}

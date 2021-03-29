package io.mercury.common.collections;

import org.eclipse.collections.api.factory.list.ImmutableListFactory;
import org.eclipse.collections.api.factory.list.primitive.ImmutableIntListFactory;
import org.eclipse.collections.api.factory.list.primitive.ImmutableLongListFactory;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.primitive.ImmutableIntList;
import org.eclipse.collections.api.list.primitive.ImmutableLongList;
import org.eclipse.collections.impl.list.immutable.ImmutableListFactoryImpl;
import org.eclipse.collections.impl.list.immutable.primitive.ImmutableIntListFactoryImpl;
import org.eclipse.collections.impl.list.immutable.primitive.ImmutableLongListFactoryImpl;

import io.mercury.common.util.ArrayUtil;

public final class ImmutableLists {

	private ImmutableLists() {
	}

	/**
	 * 
	 * @return ImmutableIntListFactory
	 */
	public static final ImmutableIntListFactory getIntListFactory() {
		return ImmutableIntListFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * @param values
	 * @return ImmutableIntList
	 */
	public static final ImmutableIntList newImmutableIntList(int... values) {
		if (ArrayUtil.isNullOrEmpty(values))
			return ImmutableIntListFactoryImpl.INSTANCE.empty();
		return ImmutableIntListFactoryImpl.INSTANCE.with(values);
	}

	/**
	 * 
	 * @return ImmutableLongListFactory
	 */
	public static final ImmutableLongListFactory getLongListFactory() {
		return ImmutableLongListFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * @param values
	 * @return ImmutableLongList
	 */
	public static final ImmutableLongList newImmutableLongList(long... values) {
		if (ArrayUtil.isNullOrEmpty(values))
			return ImmutableLongListFactoryImpl.INSTANCE.empty();
		return ImmutableLongListFactoryImpl.INSTANCE.with(values);
	}

	/**
	 * 
	 * @return ImmutableListFactory
	 */
	public static final ImmutableListFactory getListFactory() {
		return ImmutableListFactoryImpl.INSTANCE;
	}

	/**
	 * 
	 * @param <E>
	 * @param iterable
	 * @return ImmutableList
	 */
	public static final <E> ImmutableList<E> newImmutableList(Iterable<E> iterable) {
		if (iterable == null)
			return ImmutableListFactoryImpl.INSTANCE.empty();
		return ImmutableListFactoryImpl.INSTANCE.withAll(iterable);
	}

	/**
	 * 
	 * @param <E>
	 * @param e
	 * @return ImmutableList
	 */
	public static final <E> ImmutableList<E> newImmutableList(E e) {
		if (e == null)
			return ImmutableListFactoryImpl.INSTANCE.empty();
		return ImmutableListFactoryImpl.INSTANCE.with(e);
	}

	/**
	 * 
	 * @param <E>
	 * @param values
	 * @return ImmutableList
	 */
	@SafeVarargs
	public static final <E> ImmutableList<E> newImmutableList(E... values) {
		if (ArrayUtil.isNullOrEmpty(values))
			return ImmutableListFactoryImpl.INSTANCE.empty();
		return ImmutableListFactoryImpl.INSTANCE.with(values);
	}

}

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
	 * ImmutableIntListFactory
	 */
	public static ImmutableIntListFactory IntListFactory() {
		return ImmutableIntListFactoryImpl.INSTANCE;
	}

	public static ImmutableIntList newIntList(int... values) {
		if (ArrayUtil.isNullOrEmpty(values))
			return ImmutableIntListFactoryImpl.INSTANCE.empty();
		return ImmutableIntListFactoryImpl.INSTANCE.with(values);
	}

	/**
	 * 
	 * ImmutableLongListFactory
	 */
	public static ImmutableLongListFactory LongListFactory() {
		return ImmutableLongListFactoryImpl.INSTANCE;
	}

	public static ImmutableLongList newLongList(long... values) {
		if (ArrayUtil.isNullOrEmpty(values))
			return ImmutableLongListFactoryImpl.INSTANCE.empty();
		return ImmutableLongListFactoryImpl.INSTANCE.with(values);
	}

	/**
	 * 
	 * ImmutableListFactory
	 */
	public static ImmutableListFactory ListFactory() {
		return ImmutableListFactoryImpl.INSTANCE;
	}

	public static <E> ImmutableList<E> newList(Iterable<E> iterable) {
		if (iterable == null)
			return ImmutableListFactoryImpl.INSTANCE.empty();
		return ImmutableListFactoryImpl.INSTANCE.withAll(iterable);
	}

	public static <E> ImmutableList<E> newList(E e) {
		if (e == null)
			return ImmutableListFactoryImpl.INSTANCE.empty();
		return ImmutableListFactoryImpl.INSTANCE.with(e);
	}

	@SafeVarargs
	public static <E> ImmutableList<E> newList(E... values) {
		if (ArrayUtil.isNullOrEmpty(values))
			return ImmutableListFactoryImpl.INSTANCE.empty();
		return ImmutableListFactoryImpl.INSTANCE.with(values);
	}

}

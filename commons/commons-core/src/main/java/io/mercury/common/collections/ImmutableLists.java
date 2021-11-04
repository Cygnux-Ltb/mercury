package io.mercury.common.collections;

import javax.annotation.Nullable;

import org.eclipse.collections.api.factory.list.ImmutableListFactory;
import org.eclipse.collections.api.factory.list.primitive.ImmutableDoubleListFactory;
import org.eclipse.collections.api.factory.list.primitive.ImmutableIntListFactory;
import org.eclipse.collections.api.factory.list.primitive.ImmutableLongListFactory;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.primitive.ImmutableIntList;
import org.eclipse.collections.api.list.primitive.ImmutableLongList;
import org.eclipse.collections.impl.list.immutable.ImmutableListFactoryImpl;
import org.eclipse.collections.impl.list.immutable.primitive.ImmutableDoubleListFactoryImpl;
import org.eclipse.collections.impl.list.immutable.primitive.ImmutableIntListFactoryImpl;
import org.eclipse.collections.impl.list.immutable.primitive.ImmutableLongListFactoryImpl;

import io.mercury.common.util.ArrayUtil;

public final class ImmutableLists {

	private ImmutableLists() {
	}

	/**
	 * @return ImmutableListFactory Instance
	 */
	public static final ImmutableListFactory getListFactory() {
		return ImmutableListFactoryImpl.INSTANCE;
	}

	/**
	 * @return ImmutableIntListFactory Instance
	 */
	public static final ImmutableIntListFactory getIntListFactory() {
		return ImmutableIntListFactoryImpl.INSTANCE;
	}

	/**
	 * @return ImmutableLongListFactory Instance
	 */
	public static final ImmutableLongListFactory getLongListFactory() {
		return ImmutableLongListFactoryImpl.INSTANCE;
	}

	/**
	 * @return ImmutableDoubleListFactory Instance
	 */
	public static final ImmutableDoubleListFactory getDoubleListFactory() {
		return ImmutableDoubleListFactoryImpl.INSTANCE;
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

	/**
	 * 
	 * @param <E>
	 * @param list
	 * @return
	 */
	public static final <E> boolean isNullOrEmpty(@Nullable ImmutableList<E> list) {
		if (list == null || list.isEmpty())
			return true;
		return false;
	}

	/**
	 * 
	 * @param <E>
	 * @param list
	 * @return
	 */
	public static final <E> boolean notNullAndEmpty(@Nullable ImmutableList<E> list) {
		if (list != null && list.notEmpty())
			return true;
		return false;
	}

}

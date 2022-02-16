package io.mercury.common.collections;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.MutableByteList;
import org.eclipse.collections.api.list.primitive.MutableCharList;
import org.eclipse.collections.api.list.primitive.MutableDoubleList;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.api.list.primitive.MutableLongList;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.list.mutable.primitive.ByteArrayList;
import org.eclipse.collections.impl.list.mutable.primitive.CharArrayList;
import org.eclipse.collections.impl.list.mutable.primitive.DoubleArrayList;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;
import org.eclipse.collections.impl.list.mutable.primitive.LongArrayList;

import io.mercury.common.util.ArrayUtil;

public final class MutableLists {

	private MutableLists() {
	}

	/**
	 **************** primitive list ****************
	 */
	/**
	 * 
	 * @return MutableByteList
	 */
	public static final MutableByteList newByteArrayList() {
		return new ByteArrayList();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableByteList
	 */
	public static final MutableByteList newByteArrayList(int capacity) {
		return new ByteArrayList(capacity);
	}

	/**
	 * 
	 * @param bytes
	 * @return MutableByteList
	 */
	public static final MutableByteList newByteArrayListWith(byte... bytes) {
		if (ArrayUtil.isNullOrEmpty(bytes))
			return new ByteArrayList();
		return new ByteArrayList(bytes);
	}

	/**
	 * 
	 * @return MutableCharList
	 */
	public static final MutableCharList newCharArrayList() {
		return new CharArrayList();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableCharList
	 */
	public static final MutableCharList newCharArrayList(int capacity) {
		return new CharArrayList(capacity);
	}

	/**
	 * 
	 * @param chars
	 * @return MutableCharList
	 */
	public static final MutableCharList newCharArrayList(char... chars) {
		if (ArrayUtil.isNullOrEmpty(chars))
			return new CharArrayList();
		return new CharArrayList(chars);
	}

	/**
	 * 
	 * @return MutableIntList
	 */
	public static final MutableIntList newIntArrayList() {
		return new IntArrayList();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableIntList
	 */
	public static final MutableIntList newIntArrayList(int capacity) {
		return new IntArrayList(capacity);
	}

	/**
	 * 
	 * @param ints
	 * @return MutableIntList
	 */
	public static final MutableIntList newIntArrayListWith(int... ints) {
		if (ArrayUtil.isNullOrEmpty(ints))
			return newIntArrayList();
		return new IntArrayList(ints);
	}

	/**
	 * 
	 * @return MutableLongList
	 */
	public static final MutableLongList newLongArrayList() {
		return new LongArrayList();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableLongList
	 */
	public static final MutableLongList newLongArrayList(int capacity) {
		return new LongArrayList(capacity);
	}

	/**
	 * 
	 * @param longs
	 * @return MutableLongList
	 */
	public static final MutableLongList newLongArrayListWith(long... longs) {
		if (ArrayUtil.isNullOrEmpty(longs))
			return newLongArrayList();
		return new LongArrayList(longs);
	}

	/**
	 * 
	 * @return MutableDoubleList
	 */
	public static final MutableDoubleList newDoubleArrayList() {
		return new DoubleArrayList();
	}

	/**
	 * 
	 * @param capacity
	 * @return MutableDoubleList
	 */
	public static final MutableDoubleList newDoubleArrayList(int capacity) {
		return new DoubleArrayList(capacity);
	}

	/**
	 * 
	 * @param doubles
	 * @return MutableDoubleList
	 */
	public static final MutableDoubleList newDoubleArrayListWith(double... doubles) {
		if (ArrayUtil.isNullOrEmpty(doubles))
			return newDoubleArrayList();
		return new DoubleArrayList(doubles);
	}

	/**
	 **************** object list ****************
	 */
	/**
	 * 
	 * @param <E>
	 * @return MutableList
	 */
	public static final <E> MutableList<E> emptyFastList() {
		return new FastList<>();
	}

	/**
	 * 
	 * @param <E>
	 * @return MutableList
	 */
	public static final <E> MutableList<E> newFastList() {
		return new FastList<>();
	}

	/**
	 * 
	 * @param <E>
	 * @param capacity
	 * @return MutableList
	 */
	public static final <E> MutableList<E> newFastList(int capacity) {
		return new FastList<>(capacity);
	}

	/**
	 * 
	 * @param <E>
	 * @param collection
	 * @return MutableList
	 */
	public static final <E> MutableList<E> newFastList(Collection<E> collection) {
		return new FastList<>(collection);
	}

	/**
	 * 
	 * @param <E>
	 * @param iterator
	 * @return MutableList
	 */
	public static final <E> MutableList<E> newFastList(Iterator<E> iterator) {
		if (iterator != null && iterator.hasNext()) {
			MutableList<E> list = newFastList(Capacity.DEFAULT_SIZE);
			while (iterator.hasNext())
				list.add(iterator.next());
			return list;
		} else
			return newFastList();
	}

	/**
	 * 
	 * @param <E>
	 * @param iterable
	 * @return MutableList
	 */
	public static final <E> MutableList<E> newFastList(Iterable<E> iterable) {
		if (iterable == null)
			return newFastList();
		return FastList.newList(iterable);
	}

	/**
	 * 
	 * @param <E>
	 * @param values
	 * @return MutableList
	 */
	@SafeVarargs
	public static final <E> MutableList<E> newFastList(E... values) {
		if (ArrayUtil.isNullOrEmpty(values))
			return newFastList();
		return FastList.newListWith(values);
	}

}

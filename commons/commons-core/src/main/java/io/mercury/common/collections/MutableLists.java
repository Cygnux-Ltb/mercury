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
	 * primitive list
	 */
	public static MutableByteList newByteArrayList() {
		return new ByteArrayList();
	}

	public static MutableByteList newByteArrayList(int capacity) {
		return new ByteArrayList(capacity);
	}

	public static MutableByteList newByteArrayListWith(byte... bytes) {
		if (ArrayUtil.isNullOrEmpty(bytes))
			return new ByteArrayList();
		return new ByteArrayList(bytes);
	}

	public static MutableCharList newCharArrayList() {
		return new CharArrayList();
	}

	public static MutableCharList newCharArrayList(int capacity) {
		return new CharArrayList(capacity);
	}

	public static MutableCharList newCharArrayList(char... chars) {
		if (ArrayUtil.isNullOrEmpty(chars))
			return new CharArrayList();
		return new CharArrayList(chars);
	}

	public static MutableIntList newIntArrayList() {
		return new IntArrayList();
	}

	public static MutableIntList newIntArrayList(int capacity) {
		return new IntArrayList(capacity);
	}

	public static MutableIntList newIntArrayListWith(int... ints) {
		if (ArrayUtil.isNullOrEmpty(ints))
			return newIntArrayList();
		return new IntArrayList(ints);
	}

	public static MutableLongList newLongArrayList() {
		return new LongArrayList();
	}

	public static MutableLongList newLongArrayList(int capacity) {
		return new LongArrayList(capacity);
	}

	public static MutableLongList newLongArrayListWith(long... longs) {
		if (ArrayUtil.isNullOrEmpty(longs))
			return newLongArrayList();
		return new LongArrayList(longs);
	}

	public static MutableDoubleList newDoubleArrayList() {
		return new DoubleArrayList();
	}

	public static MutableDoubleList newDoubleArrayList(int capacity) {
		return new DoubleArrayList(capacity);
	}

	public static MutableDoubleList newDoubleArrayListWith(double... doubles) {
		if (ArrayUtil.isNullOrEmpty(doubles))
			return newDoubleArrayList();
		return new DoubleArrayList(doubles);
	}

	private static final int DEFAULT_CAPACITY = 16;

	/**
	 * list
	 */
	public static <E> MutableList<E> emptyFastList() {
		return new FastList<>();
	}

	public static <E> MutableList<E> newFastList() {
		return new FastList<>();
	}

	public static <E> MutableList<E> newFastList(int capacity) {
		return new FastList<>(capacity > DEFAULT_CAPACITY ? capacity : DEFAULT_CAPACITY);
	}

	public static <E> MutableList<E> newFastList(Collection<E> collection) {
		return new FastList<>(collection);
	}

	public static <E> MutableList<E> newFastList(Iterator<E> iterator) {
		if (iterator != null && iterator.hasNext()) {
			MutableList<E> list = newFastList(DEFAULT_CAPACITY);
			while (iterator.hasNext())
				list.add(iterator.next());
			return list;
		} else
			return newFastList();
	}

	public static <E> MutableList<E> newFastList(Iterable<E> iterable) {
		if (iterable == null)
			return newFastList();
		return FastList.newList(iterable);
	}

	@SafeVarargs
	public static <E> MutableList<E> newFastList(E... values) {
		if (ArrayUtil.isNullOrEmpty(values))
			return newFastList();
		return FastList.newListWith(values);
	}

}

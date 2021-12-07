package io.mercury.common.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

public final class Assertor {

	private Assertor() {
	}

	/**
	 * 
	 * @param i
	 * @param min
	 * @param objName
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static int greaterThan(int i, int min, String objName) throws IllegalArgumentException {
		if (i > min)
			return i;
		throw new IllegalArgumentException(objName + " must greater than " + min);
	}

	/**
	 * 
	 * @param l
	 * @param min
	 * @param objName
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static long greaterThan(long l, long min, String objName) throws IllegalArgumentException {
		if (l > min)
			return l;
		throw new IllegalArgumentException(objName + " must greater than " + min);
	}

	/**
	 * 
	 * @param i
	 * @param max
	 * @param objName
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static int lessThan(int i, int max, String objName) throws IllegalArgumentException {
		if (i < max)
			return i;
		throw new IllegalArgumentException(objName + " must less than " + max);
	}

	/**
	 * 
	 * @param l
	 * @param max
	 * @param objName
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static long lessThan(long l, long max, String objName) throws IllegalArgumentException {
		if (l < max)
			return l;
		throw new IllegalArgumentException(objName + " must less than " + max);
	}

	/**
	 * 
	 * @param i
	 * @param min
	 * @param max
	 * @param objName
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static int atWithinRange(int i, int min, int max, String objName) throws IllegalArgumentException {
		if (i >= min && i <= max)
			return i;
		throw new IllegalArgumentException(
				"argument : [" + objName + "] must in the range of [" + min + "] to [" + max + "]");
	}

	/**
	 * 
	 * @param l
	 * @param min
	 * @param max
	 * @param objName
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static long atWithinRange(long l, long min, long max, String objName) throws IllegalArgumentException {
		if (l >= min && l <= max)
			return l;
		throw new IllegalArgumentException(objName + " must in the range of [" + min + "] to [" + max + "]");
	}

	/**
	 * 
	 * @param <T>
	 * @param t
	 * @return
	 * @throws NullPointerException
	 */
	public static <T> T nonNull(T t) throws NullPointerException {
		return nonNull(t, "");
	}

	/**
	 * 
	 * @param <T>
	 * @param t
	 * @param objName
	 * @return
	 * @throws NullPointerException
	 */
	public static <T> T nonNull(T t, @Nonnull String objName) throws NullPointerException {
		if (t == null)
			if (StringSupport.isNullOrEmpty(objName))
				throw new NullPointerException("param cannot be null");
			else
				throw new NullPointerException("param [" + objName + "] cannot be null");
		return t;
	}

	/**
	 * 
	 * @param <T>
	 * @param <E>
	 * @param t
	 * @param e
	 * @return
	 * @throws E
	 */
	public static <T, E extends Throwable> T nonNull(T t, E e) throws E {
		if (t == null)
			throw e;
		return t;
	}

	/**
	 * 
	 * @param str
	 * @param objName
	 * @return
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public static String nonEmpty(String str, String objName) throws NullPointerException, IllegalArgumentException {
		if (str == null)
			throw new NullPointerException(objName + " can not be null");
		if (str.length() <= 0)
			throw new IllegalArgumentException(objName + " can not be empty");
		return str;
	}

	/**
	 * 
	 * @param <T>
	 * @param collection
	 * @param objName
	 * @return
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public static <T extends Collection<E>, E> T nonEmptyCollection(T collection, String objName)
			throws NullPointerException, IllegalArgumentException {
		if (collection == null)
			throw new NullPointerException(objName + " can not be null");
		if (collection.isEmpty())
			throw new IllegalArgumentException(objName + " can not be empty");
		return collection;
	}

	/**
	 * 
	 * @param <T>
	 * @param map
	 * @param objName
	 * @return
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public static <T extends Map<?, ?>> T nonEmptyMap(T map, String objName)
			throws NullPointerException, IllegalArgumentException {
		if (map == null)
			throw new NullPointerException(objName + " can not be null");
		if (map.isEmpty())
			throw new IllegalArgumentException(objName + " can not be empty");
		return map;
	}

	/**
	 * 
	 * @param <T>
	 * @param array
	 * @param requiredLength
	 * @param arrayName
	 * @return
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public static <T> T[] requiredLength(T[] array, int requiredLength, String arrayName)
			throws NullPointerException, IllegalArgumentException {
		if (array == null)
			throw new NullPointerException(arrayName + " can not be null");
		if (array.length < requiredLength)
			throw new IllegalArgumentException(arrayName + " length must be greater than " + requiredLength);
		return array;
	}

	/**
	 * 
	 * @param <T>
	 * @param list
	 * @param requiredLength
	 * @param listName
	 * @return
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public static <T> List<T> requiredLength(List<T> list, int requiredLength, String listName)
			throws NullPointerException, IllegalArgumentException {
		if (list == null)
			throw new NullPointerException(listName + " can not be null");
		if (list.size() < requiredLength)
			throw new IllegalArgumentException(listName + " length must be greater than " + requiredLength);
		return list;
	}

	/**
	 * 
	 * @param array
	 * @param requiredLength
	 * @param arrayName
	 * @return
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public static boolean[] requiredLength(boolean[] array, int requiredLength, String arrayName)
			throws NullPointerException, IllegalArgumentException {
		if (array == null)
			throw new NullPointerException(arrayName + " can not be null");
		if (array.length < requiredLength)
			throw new IllegalArgumentException(arrayName + " length must be greater than " + requiredLength);
		return array;
	}

	/**
	 * 
	 * @param array
	 * @param requiredLength
	 * @param arrayName
	 * @return
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public static byte[] requiredLength(byte[] array, int requiredLength, String arrayName)
			throws NullPointerException, IllegalArgumentException {
		if (array == null)
			throw new NullPointerException(arrayName + " can not be null");
		if (array.length < requiredLength)
			throw new IllegalArgumentException(arrayName + " length must be greater than " + requiredLength);
		return array;
	}

	/**
	 * 
	 * @param array
	 * @param requiredLength
	 * @param arrayName
	 * @return
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public static char[] requiredLength(char[] array, int requiredLength, String arrayName)
			throws NullPointerException, IllegalArgumentException {
		if (array == null)
			throw new NullPointerException(arrayName + " can not be null");
		if (array.length < requiredLength)
			throw new IllegalArgumentException(arrayName + " length must be greater than " + requiredLength);
		return array;
	}

	/**
	 * 
	 * @param array
	 * @param requiredLength
	 * @param arrayName
	 * @return
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public static int[] requiredLength(int[] array, int requiredLength, String arrayName)
			throws NullPointerException, IllegalArgumentException {
		if (array == null)
			throw new NullPointerException(arrayName + " can not be null");
		if (array.length < requiredLength)
			throw new IllegalArgumentException(arrayName + " length must be greater than " + requiredLength);
		return array;
	}

	/**
	 * 
	 * @param array
	 * @param requiredLength
	 * @param arrayName
	 * @return
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public static long[] requiredLength(long[] array, int requiredLength, String arrayName)
			throws NullPointerException, IllegalArgumentException {
		if (array == null)
			throw new NullPointerException(arrayName + " can not be null");
		if (array.length < requiredLength)
			throw new IllegalArgumentException(arrayName + " length must be greater than " + requiredLength);
		return array;
	}

	/**
	 * 
	 * @param <T>
	 * @param array
	 * @param requiredLength
	 * @param arrayName
	 * @return
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public static float[] requiredLength(float[] array, int requiredLength, String arrayName)
			throws NullPointerException, IllegalArgumentException {
		if (array == null)
			throw new NullPointerException(arrayName + " can not be null");
		if (array.length < requiredLength)
			throw new IllegalArgumentException(arrayName + " length must be greater than " + requiredLength);
		return array;
	}

	/**
	 * 
	 * @param array
	 * @param requiredLength
	 * @param arrayName
	 * @return
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public static double[] requiredLength(double[] array, int requiredLength, String arrayName)
			throws NullPointerException, IllegalArgumentException {
		if (array == null)
			throw new NullPointerException(arrayName + " can not be null");
		if (array.length < requiredLength)
			throw new IllegalArgumentException(arrayName + " length must be greater than " + requiredLength);
		return array;
	}

}

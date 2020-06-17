package io.mercury.common.util;

import static java.lang.System.arraycopy;

import javax.annotation.Nonnull;

public final class ArrayUtil {

	/**
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isNullOrEmpty(byte[] array) {
		return array == null ? true : array.length == 0 ? true : false;
	}

	/**
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isNullOrEmpty(char[] array) {
		return array == null ? true : array.length == 0 ? true : false;
	}

	/**
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isNullOrEmpty(int[] array) {
		return array == null ? true : array.length == 0 ? true : false;
	}

	/**
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isNullOrEmpty(long[] array) {
		return array == null ? true : array.length == 0 ? true : false;
	}

	/**
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isNullOrEmpty(double[] array) {
		return array == null ? true : array.length == 0 ? true : false;
	}

	/**
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isNullOrEmpty(Object[] array) {
		return array == null ? true : array.length == 0 ? true : false;
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static boolean[] newOf(@Nonnull boolean[] origin) {
		boolean[] newArray = new boolean[origin.length];
		arraycopy(origin, 0, newArray, 0, origin.length);
		return newArray;
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static byte[] newOf(@Nonnull byte[] origin) {
		byte[] newArray = new byte[origin.length];
		arraycopy(origin, 0, newArray, 0, origin.length);
		return newArray;
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static char[] newOf(@Nonnull char[] origin) {
		char[] newArray = new char[origin.length];
		arraycopy(origin, 0, newArray, 0, origin.length);
		return newArray;
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static int[] newOf(@Nonnull int[] origin) {
		int[] newArray = new int[origin.length];
		arraycopy(origin, 0, newArray, 0, origin.length);
		return newArray;
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static long[] newOf(@Nonnull long[] origin) {
		long[] newArray = new long[origin.length];
		arraycopy(origin, 0, newArray, 0, origin.length);
		return newArray;
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static float[] newOf(@Nonnull float[] origin) {
		float[] newArray = new float[origin.length];
		arraycopy(origin, 0, newArray, 0, origin.length);
		return newArray;
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static double[] newOf(@Nonnull double[] origin) {
		double[] newArray = new double[origin.length];
		arraycopy(origin, 0, newArray, 0, origin.length);
		return newArray;
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static String[] newOf(@Nonnull String[] origin) {
		String[] newArray = new String[origin.length];
		arraycopy(origin, 0, newArray, 0, origin.length);
		return newArray;
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static Object[] newOf(@Nonnull Object[] origin) {
		Object[] newArray = new Object[origin.length];
		arraycopy(origin, 0, newArray, 0, origin.length);
		return newArray;
	}

	/**
	 * 
	 * @param origin
	 * @param newArray
	 * @return
	 */
	public static boolean[] copy(@Nonnull boolean[] origin, @Nonnull boolean[] newArray) {
		if (newArray == null)
			newArray = new boolean[origin.length];
		arraycopy(origin, 0, newArray, 0, newArray.length < origin.length ? newArray.length : origin.length);
		return newArray;
	}

	/**
	 * 
	 * @param origin
	 * @param newArray
	 * @return
	 */
	public static byte[] copy(@Nonnull byte[] origin, @Nonnull byte[] newArray) {
		if (newArray == null)
			newArray = new byte[origin.length];
		arraycopy(origin, 0, newArray, 0, newArray.length < origin.length ? newArray.length : origin.length);
		return newArray;
	}

	/**
	 * 
	 * @param origin
	 * @param newArray
	 * @return
	 */
	public static char[] copy(@Nonnull char[] origin, @Nonnull char[] newArray) {
		if (newArray == null)
			newArray = new char[origin.length];
		arraycopy(origin, 0, newArray, 0, newArray.length < origin.length ? newArray.length : origin.length);
		return newArray;
	}

	/**
	 * 
	 * @param origin
	 * @param newArray
	 * @return
	 */
	public static int[] copy(@Nonnull int[] origin, @Nonnull int[] newArray) {
		if (newArray == null)
			newArray = new int[origin.length];
		arraycopy(origin, 0, newArray, 0, newArray.length < origin.length ? newArray.length : origin.length);
		return newArray;
	}

	/**
	 * 
	 * @param origin
	 * @param newArray
	 * @return
	 */
	public static long[] copy(@Nonnull long[] origin, @Nonnull long[] newArray) {
		if (newArray == null)
			newArray = new long[origin.length];
		arraycopy(origin, 0, newArray, 0, newArray.length < origin.length ? newArray.length : origin.length);
		return newArray;
	}

	/**
	 * 
	 * @param origin
	 * @param newArray
	 * @return
	 */
	public static float[] copy(@Nonnull float[] origin, @Nonnull float[] newArray) {
		if (newArray == null)
			newArray = new float[origin.length];
		arraycopy(origin, 0, newArray, 0, newArray.length < origin.length ? newArray.length : origin.length);
		return newArray;
	}

	/**
	 * 
	 * @param origin
	 * @param newArray
	 * @return
	 */
	public static double[] copy(@Nonnull double[] origin, @Nonnull double[] newArray) {
		if (newArray == null)
			newArray = new double[origin.length];
		arraycopy(origin, 0, newArray, 0, newArray.length < origin.length ? newArray.length : origin.length);
		return newArray;
	}

	/**
	 * 
	 * @param origin
	 * @param newArray
	 * @return
	 */
	public static String[] copy(@Nonnull String[] origin, @Nonnull String[] newArray) {
		if (newArray == null)
			newArray = new String[origin.length];
		arraycopy(origin, 0, newArray, 0, newArray.length < origin.length ? newArray.length : origin.length);
		return newArray;
	}

	/**
	 * 
	 * @param origin
	 * @param newArray
	 * @return
	 */
	public static Object[] copy(@Nonnull Object[] origin, @Nonnull Object[] newArray) {
		if (newArray == null)
			newArray = new Object[origin.length];
		arraycopy(origin, 0, newArray, 0, newArray.length < origin.length ? newArray.length : origin.length);
		return newArray;
	}

}

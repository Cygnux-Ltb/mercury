package io.mercury.common.util;

import static java.lang.System.arraycopy;

import javax.annotation.Nonnull;

public final class ArrayUtil {

	/**
	 * 
	 * @param booleans
	 * @return
	 */
	public static boolean isNullOrEmpty(boolean[] booleans) {
		return booleans == null ? true : booleans.length == 0 ? true : false;
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static boolean isNullOrEmpty(byte[] bytes) {
		return bytes == null ? true : bytes.length == 0 ? true : false;
	}

	/**
	 * 
	 * @param chars
	 * @return
	 */
	public static boolean isNullOrEmpty(char[] chars) {
		return chars == null ? true : chars.length == 0 ? true : false;
	}

	/**
	 * 
	 * @param ints
	 * @return
	 */
	public static boolean isNullOrEmpty(int[] ints) {
		return ints == null ? true : ints.length == 0 ? true : false;
	}

	/**
	 * 
	 * @param longs
	 * @return
	 */
	public static boolean isNullOrEmpty(long[] longs) {
		return longs == null ? true : longs.length == 0 ? true : false;
	}

	/**
	 * 
	 * @param floats
	 * @return
	 */
	public static boolean isNullOrEmpty(float[] floats) {
		return floats == null ? true : floats.length == 0 ? true : false;
	}

	/**
	 * 
	 * @param doubles
	 * @return
	 */
	public static boolean isNullOrEmpty(double[] doubles) {
		return doubles == null ? true : doubles.length == 0 ? true : false;
	}

	/**
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isNullOrEmpty(String[] array) {
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
		Assertor.requiredLength(origin, 1, "origin");
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
		Assertor.requiredLength(origin, 1, "origin");
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
		Assertor.requiredLength(origin, 1, "origin");
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
		Assertor.requiredLength(origin, 1, "origin");
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
		Assertor.requiredLength(origin, 1, "origin");
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
		Assertor.requiredLength(origin, 1, "origin");
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
		Assertor.requiredLength(origin, 1, "origin");
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
		Assertor.requiredLength(origin, 1, "origin");
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
		Assertor.requiredLength(origin, 1, "origin");
		if (newArray == null)
			newArray = new Object[origin.length];
		arraycopy(origin, 0, newArray, 0, newArray.length < origin.length ? newArray.length : origin.length);
		return newArray;
	}

}

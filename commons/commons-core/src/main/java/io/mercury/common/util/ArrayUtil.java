package io.mercury.common.util;

import static java.lang.System.arraycopy;

import javax.annotation.Nonnull;

import io.mercury.common.lang.Assertor;

public final class ArrayUtil {

	/**
	 * 
	 * @param length
	 * @return
	 */
	public static boolean illegalLength(int length) {
		if (length < 1)
			return false;
		return true;
	}

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
		var target = new boolean[origin.length];
		arraycopy(origin, 0, target, 0, origin.length);
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static byte[] newOf(@Nonnull byte[] origin) {
		var target = new byte[origin.length];
		arraycopy(origin, 0, target, 0, origin.length);
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static char[] newOf(@Nonnull char[] origin) {
		var target = new char[origin.length];
		arraycopy(origin, 0, target, 0, origin.length);
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static int[] newOf(@Nonnull int[] origin) {
		var target = new int[origin.length];
		arraycopy(origin, 0, target, 0, origin.length);
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static long[] newOf(@Nonnull long[] origin) {
		var target = new long[origin.length];
		arraycopy(origin, 0, target, 0, origin.length);
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static float[] newOf(@Nonnull float[] origin) {
		var target = new float[origin.length];
		arraycopy(origin, 0, target, 0, origin.length);
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static double[] newOf(@Nonnull double[] origin) {
		double[] target = new double[origin.length];
		arraycopy(origin, 0, target, 0, origin.length);
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static String[] newOf(@Nonnull String[] origin) {
		var target = new String[origin.length];
		arraycopy(origin, 0, target, 0, origin.length);
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static Object[] newOf(@Nonnull Object[] origin) {
		var target = new Object[origin.length];
		arraycopy(origin, 0, target, 0, origin.length);
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @param target
	 * @return
	 */
	public static boolean[] copy(@Nonnull boolean[] origin, @Nonnull boolean[] target) {
		Assertor.requiredLength(origin, 1, "origin");
		if (target == null)
			target = new boolean[origin.length];
		arraycopy(origin, 0, target, 0, target.length < origin.length ? target.length : origin.length);
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @param target
	 * @return
	 */
	public static byte[] copy(@Nonnull byte[] origin, @Nonnull byte[] target) {
		Assertor.requiredLength(origin, 1, "origin");
		if (target == null)
			target = new byte[origin.length];
		arraycopy(origin, 0, target, 0, target.length < origin.length ? target.length : origin.length);
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @param target
	 * @return
	 */
	public static char[] copy(@Nonnull char[] origin, @Nonnull char[] target) {
		Assertor.requiredLength(origin, 1, "origin");
		if (target == null)
			target = new char[origin.length];
		arraycopy(origin, 0, target, 0, target.length < origin.length ? target.length : origin.length);
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @param target
	 * @return
	 */
	public static int[] copy(@Nonnull int[] origin, @Nonnull int[] target) {
		Assertor.requiredLength(origin, 1, "origin");
		if (target == null)
			target = new int[origin.length];
		arraycopy(origin, 0, target, 0, target.length < origin.length ? target.length : origin.length);
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @param target
	 * @return
	 */
	public static long[] copy(@Nonnull long[] origin, @Nonnull long[] target) {
		Assertor.requiredLength(origin, 1, "origin");
		if (target == null)
			target = new long[origin.length];
		arraycopy(origin, 0, target, 0, target.length < origin.length ? target.length : origin.length);
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @param target
	 * @return
	 */
	public static float[] copy(@Nonnull float[] origin, @Nonnull float[] target) {
		Assertor.requiredLength(origin, 1, "origin");
		if (target == null)
			target = new float[origin.length];
		arraycopy(origin, 0, target, 0, target.length < origin.length ? target.length : origin.length);
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @param target
	 * @return
	 */
	public static double[] copy(@Nonnull double[] origin, @Nonnull double[] target) {
		Assertor.requiredLength(origin, 1, "origin");
		if (target == null)
			target = new double[origin.length];
		arraycopy(origin, 0, target, 0, target.length < origin.length ? target.length : origin.length);
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @param target
	 * @return
	 */
	public static String[] copy(@Nonnull String[] origin, @Nonnull String[] target) {
		Assertor.requiredLength(origin, 1, "origin");
		if (target == null)
			target = new String[origin.length];
		arraycopy(origin, 0, target, 0, target.length < origin.length ? target.length : origin.length);
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @param target
	 * @return
	 */
	public static Object[] copy(@Nonnull Object[] origin, @Nonnull Object[] target) {
		Assertor.requiredLength(origin, 1, "origin");
		if (target == null)
			target = new Object[origin.length];
		arraycopy(origin, 0, target, 0, target.length < origin.length ? target.length : origin.length);
		return target;
	}

}

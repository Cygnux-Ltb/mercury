package io.mercury.common.util;

import static java.lang.System.arraycopy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.mercury.common.lang.Assertor;

public final class ArrayUtil {

	/**
	 * 
	 * @param length
	 * @return
	 */
	public static boolean illegalLength(int length) {
		return length >= 1;
	}

	/**
	 * 
	 * @param booleans
	 * @return
	 */
	public static boolean isNullOrEmpty(boolean[] booleans) {
		return booleans == null || booleans.length == 0;
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static boolean isNullOrEmpty(byte[] bytes) {
		return bytes == null || bytes.length == 0;
	}

	/**
	 * 
	 * @param chars
	 * @return
	 */
	public static boolean isNullOrEmpty(char[] chars) {
		return chars == null || chars.length == 0;
	}

	/**
	 * 
	 * @param ints
	 * @return
	 */
	public static boolean isNullOrEmpty(int[] ints) {
		return ints == null || ints.length == 0;
	}

	/**
	 * 
	 * @param longs
	 * @return
	 */
	public static boolean isNullOrEmpty(long[] longs) {
		return longs == null || longs.length == 0;
	}

	/**
	 * 
	 * @param floats
	 * @return
	 */
	public static boolean isNullOrEmpty(float[] floats) {
		return floats == null || floats.length == 0;
	}

	/**
	 * 
	 * @param doubles
	 * @return
	 */
	public static boolean isNullOrEmpty(double[] doubles) {
		return doubles == null || doubles.length == 0;
	}

	/**
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isNullOrEmpty(String[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isNullOrEmpty(Object[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static boolean[] newOf(@Nonnull boolean[] origin) {
		boolean[] target = new boolean[origin.length];
		arraycopy(origin, 0, target, 0, origin.length);
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static byte[] newOf(@Nonnull byte[] origin) {
		byte[] target = new byte[origin.length];
		arraycopy(origin, 0, target, 0, origin.length);
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static char[] newOf(@Nonnull char[] origin) {
		char[] target = new char[origin.length];
		arraycopy(origin, 0, target, 0, origin.length);
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static int[] newOf(@Nonnull int[] origin) {
		int[] target = new int[origin.length];
		arraycopy(origin, 0, target, 0, origin.length);
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static long[] newOf(@Nonnull long[] origin) {
		long[] target = new long[origin.length];
		arraycopy(origin, 0, target, 0, origin.length);
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static float[] newOf(@Nonnull float[] origin) {
		float[] target = new float[origin.length];
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
		String[] target = new String[origin.length];
		arraycopy(origin, 0, target, 0, origin.length);
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @return
	 */
	public static Object[] newOf(@Nonnull Object[] origin) {
		Object[] target = new Object[origin.length];
		arraycopy(origin, 0, target, 0, origin.length);
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @param target
	 * @return
	 */
	public static boolean[] copy(@Nonnull boolean[] origin, @Nullable boolean[] target) {
		Assertor.requiredLength(origin, 1, "origin");
		if (target == null)
			target = new boolean[origin.length];
		arraycopy(origin, 0, target, 0, Math.min(target.length, origin.length));
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @param target
	 * @return
	 */
	public static byte[] copy(@Nonnull byte[] origin, @Nullable byte[] target) {
		Assertor.requiredLength(origin, 1, "origin");
		if (target == null)
			target = new byte[origin.length];
		arraycopy(origin, 0, target, 0, Math.min(target.length, origin.length));
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @param target
	 * @return
	 */
	public static char[] copy(@Nonnull char[] origin, @Nullable char[] target) {
		Assertor.requiredLength(origin, 1, "origin");
		if (target == null)
			target = new char[origin.length];
		arraycopy(origin, 0, target, 0, Math.min(target.length, origin.length));
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @param target
	 * @return
	 */
	public static int[] copy(@Nonnull int[] origin, @Nullable int[] target) {
		Assertor.requiredLength(origin, 1, "origin");
		if (target == null)
			target = new int[origin.length];
		arraycopy(origin, 0, target, 0, Math.min(target.length, origin.length));
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @param target
	 * @return
	 */
	public static long[] copy(@Nonnull long[] origin, @Nullable long[] target) {
		Assertor.requiredLength(origin, 1, "origin");
		if (target == null)
			target = new long[origin.length];
		arraycopy(origin, 0, target, 0, Math.min(target.length, origin.length));
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @param target
	 * @return
	 */
	public static float[] copy(@Nonnull float[] origin, @Nullable float[] target) {
		Assertor.requiredLength(origin, 1, "origin");
		if (target == null)
			target = new float[origin.length];
		arraycopy(origin, 0, target, 0, Math.min(target.length, origin.length));
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @param target
	 * @return
	 */
	public static double[] copy(@Nonnull double[] origin, @Nullable double[] target) {
		Assertor.requiredLength(origin, 1, "origin");
		if (target == null)
			target = new double[origin.length];
		arraycopy(origin, 0, target, 0, Math.min(target.length, origin.length));
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @param target
	 * @return
	 */
	public static String[] copy(@Nonnull String[] origin, @Nullable String[] target) {
		Assertor.requiredLength(origin, 1, "origin");
		if (target == null)
			target = new String[origin.length];
		arraycopy(origin, 0, target, 0, Math.min(target.length, origin.length));
		return target;
	}

	/**
	 * 
	 * @param origin
	 * @param target
	 * @return
	 */
	public static Object[] copy(@Nonnull Object[] origin, @Nullable Object[] target) {
		Assertor.requiredLength(origin, 1, "origin");
		if (target == null)
			target = new Object[origin.length];
		arraycopy(origin, 0, target, 0, Math.min(target.length, origin.length));
		return target;
	}

}

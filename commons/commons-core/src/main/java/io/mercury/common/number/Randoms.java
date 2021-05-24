package io.mercury.common.number;

import java.security.SecureRandom;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public final class Randoms {

	private static final SecureRandom Random = new SecureRandom();

	/**
	 * @NotThreadSafe
	 * @return int
	 */
	public static int nextInt() {
		return Random.nextInt();
	}

	/**
	 * @NotThreadSafe
	 * @return unsigned int
	 */

	public static int nextUnsignedInt() {
		int next;
		do {
			next = Random.nextInt();
		} while (next == Integer.MIN_VALUE);
		return Math.abs(next);
	}

	/**
	 * @NotThreadSafe
	 * @return long
	 */
	public static long nextLong() {
		return Random.nextLong();
	}

	/**
	 * @NotThreadSafe
	 * @return unsigned long
	 */
	public static long nextUnsignedLong() {
		long next;
		do {
			next = Random.nextLong();
		} while (next == Long.MIN_VALUE);
		return Math.abs(next);
	}

	/**
	 * @NotThreadSafe
	 * @return double
	 */
	public static double nextDouble() {
		return Random.nextDouble();
	}

	/**
	 * @NotThreadSafe
	 * @return unsigned double
	 */
	public static double nextUnsignedDouble() {
		return Math.abs(Random.nextDouble());
	}

}

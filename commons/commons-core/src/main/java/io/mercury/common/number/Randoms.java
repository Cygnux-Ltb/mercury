package io.mercury.common.number;

import java.util.Random;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public final class Randoms {

	private static final Random STATIC_RANDOM = new Random();

	/**
	 * @NotThreadSafe
	 * @return int
	 */
	public static int randomInt() {
		return STATIC_RANDOM.nextInt();
	}

	/**
	 * @NotThreadSafe
	 * @return unsigned int
	 */

	public static int randomUnsignedInt() {
		return Math.abs(STATIC_RANDOM.nextInt());
	}

	/**
	 * @NotThreadSafe
	 * @return long
	 */
	public static long randomLong() {
		return STATIC_RANDOM.nextLong();
	}

	/**
	 * @NotThreadSafe
	 * @return unsigned long
	 */
	public static long randomUnsignedLong() {
		return Math.abs(STATIC_RANDOM.nextLong());
	}

	/**
	 * @NotThreadSafe
	 * @return double
	 */
	public static double randomDouble() {
		return STATIC_RANDOM.nextDouble();
	}

	/**
	 * @NotThreadSafe
	 * @return unsigned double
	 */
	public static double randomUnsignedDouble() {
		return Math.abs(STATIC_RANDOM.nextDouble());
	}



}

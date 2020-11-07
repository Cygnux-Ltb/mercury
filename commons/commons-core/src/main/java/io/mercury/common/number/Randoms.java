package io.mercury.common.number;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public enum Randoms {

	;

	private static final Random RANDOM = new Random();

	/**
	 * @NotThreadSafe
	 * @return int
	 */
	public static int randomInt() {
		return RANDOM.nextInt();
	}

	/**
	 * @NotThreadSafe
	 * @return unsigned int
	 */

	public static int randomUnsignedInt() {
		return Math.abs(RANDOM.nextInt());
	}

	/**
	 * @NotThreadSafe
	 * @return long
	 */
	public static long randomLong() {
		return RANDOM.nextLong();
	}

	/**
	 * @NotThreadSafe
	 * @return unsigned long
	 */
	public static long randomUnsignedLong() {
		return Math.abs(RANDOM.nextLong());
	}

	/**
	 * @NotThreadSafe
	 * @return double
	 */
	public static double randomDouble() {
		return RANDOM.nextDouble();
	}

	/**
	 * @NotThreadSafe
	 * @return unsigned double
	 */
	public static double randomUnsignedDouble() {
		return Math.abs(RANDOM.nextDouble());
	}

	/**
	 * @ThreadSafe
	 * @return int
	 */
	public static int threadSafeRandomInt() {
		return ThreadLocalRandom.current().nextInt();
	}

	/**
	 * @ThreadSafe
	 * @return unsigned int
	 */
	public static int threadSafeRandomUnsignedInt() {
		return Math.abs(ThreadLocalRandom.current().nextInt());
	}

	/**
	 * @ThreadSafe
	 * @return long
	 */
	public static long threadSafeRandomLong() {
		return ThreadLocalRandom.current().nextLong();
	}

	/**
	 * @ThreadSafe
	 * @return unsigned long
	 */
	public static long threadSafeRandomUnsignedLong() {
		return Math.abs(ThreadLocalRandom.current().nextLong());
	}

	/**
	 * @ThreadSafe
	 * @return double
	 */
	public static double threadSafeRandomDouble() {
		return ThreadLocalRandom.current().nextDouble();
	}

	/**
	 * @ThreadSafe
	 * @return unsigned double
	 */
	public static double threadSafeRandomUnsignedDouble() {
		return Math.abs(ThreadLocalRandom.current().nextDouble());
	}

}

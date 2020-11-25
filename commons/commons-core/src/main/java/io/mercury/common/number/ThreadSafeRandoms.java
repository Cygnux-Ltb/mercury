package io.mercury.common.number;

import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class ThreadSafeRandoms {

	/**
	 * @ThreadSafe
	 * @return int
	 */
	public static int randomInt() {
		return ThreadLocalRandom.current().nextInt();
	}

	/**
	 * @ThreadSafe
	 * @return unsigned int
	 */
	public static int randomUnsignedInt() {
		return Math.abs(ThreadLocalRandom.current().nextInt());
	}

	/**
	 * @ThreadSafe
	 * @return long
	 */
	public static long randomLong() {
		return ThreadLocalRandom.current().nextLong();
	}

	/**
	 * @ThreadSafe
	 * @return unsigned long
	 */
	public static long randomUnsignedLong() {
		return Math.abs(ThreadLocalRandom.current().nextLong());
	}

	/**
	 * @ThreadSafe
	 * @return double
	 */
	public static double randomDouble() {
		return ThreadLocalRandom.current().nextDouble();
	}

	/**
	 * @ThreadSafe
	 * @return unsigned double
	 */
	public static double randomUnsignedDouble() {
		return Math.abs(ThreadLocalRandom.current().nextDouble());
	}

}

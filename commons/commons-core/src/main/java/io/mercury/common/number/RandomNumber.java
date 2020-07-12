package io.mercury.common.number;

import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class RandomNumber {

	/**
	 * 
	 * @return int
	 */
	public static int randomInt() {
		return ThreadLocalRandom.current().nextInt();
	}

	/**
	 * 
	 * @return unsigned int
	 */
	public static int randomUnsignedInt() {
		return Math.abs(ThreadLocalRandom.current().nextInt());
	}

	/**
	 * 
	 * @return long
	 */
	public static long randomLong() {
		return ThreadLocalRandom.current().nextLong();
	}

	/**
	 * 
	 * @return unsigned long
	 */
	public static long randomUnsignedLong() {
		return Math.abs(ThreadLocalRandom.current().nextLong());
	}

	/**
	 * 
	 * @return double
	 */
	public static double randomDouble() {
		return ThreadLocalRandom.current().nextDouble();
	}

	/**
	 * 
	 * @return unsigned double
	 */
	public static double randomUnsignedDouble() {
		return Math.abs(ThreadLocalRandom.current().nextDouble());
	}

}

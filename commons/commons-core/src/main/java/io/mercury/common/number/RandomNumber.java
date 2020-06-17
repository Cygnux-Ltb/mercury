package io.mercury.common.number;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class RandomNumber {

	private static final Random RandomInstance = new Random();

	public static void setSeed(long seed) {
		RandomInstance.setSeed(seed);
	}

	public static int randomInt() {
		return RandomInstance.nextInt();
	}

	public static int randomUnsignedInt() {
		int nextInt = RandomInstance.nextInt();
		if (nextInt == Integer.MIN_VALUE)
			return nextInt >>> 1;
		return Math.abs(nextInt);
	}

	public static long randomLong() {
		return RandomInstance.nextLong();
	}

	public static long randomUnsignedLong() {
		long nextLong = RandomInstance.nextLong();
		if (nextLong == Long.MIN_VALUE)
			return nextLong >>> 1;
		return Math.abs(nextLong);
	}

	public static double randomDouble() {
		return RandomInstance.nextDouble();
	}

	public static double randomUnsignedDouble() {
		return Math.abs(RandomInstance.nextDouble());
	}

	public static void setSeedOfSafe(long seed) {
		ThreadLocalRandom.current().setSeed(seed);
	}

	public static int randomUnsignedIntOfSafe() {
		return Math.abs(ThreadLocalRandom.current().nextInt());
	}

	public static int randomIntOfSafe() {
		return ThreadLocalRandom.current().nextInt();
	}

	public static long randomUnsignedLongOfSafe() {
		return Math.abs(ThreadLocalRandom.current().nextLong());
	}

	public static long randomLongOfSafe() {
		return ThreadLocalRandom.current().nextLong();
	}

	public static double randomUnsignedDoubleOfSafe() {
		return Math.abs(ThreadLocalRandom.current().nextDouble());
	}

	public static double randomDoubleOfSafe() {
		return ThreadLocalRandom.current().nextDouble();
	}

}

package io.mercury.common.number;

/**
 * 
 * @author yellow013
 *
 */
public final class DecimalSupporter {

	/**
	 * 
	 */
	public static final long L_MULTIPLIER_1 = 1L;

	/**
	 * 
	 */
	public static final double D_MULTIPLIER_1 = 1.0D;

	/**
	 * 
	 */
	public static final long L_MULTIPLIER_100 = 100L;

	/**
	 * 
	 */
	public static final double D_MULTIPLIER_100 = 100.0D;

	/**
	 * 
	 */
	public static final long L_MULTIPLIER_10000 = 10_000L;

	/**
	 * 
	 */
	public static final double D_MULTIPLIER_10000 = 10_000.0D;

	/**
	 * 
	 */
	public static final long L_MULTIPLIER_1000000 = 1_000_000L;

	/**
	 * 
	 */
	public static final double D_MULTIPLIER_1000000 = 1_000_000.0D;

	/**
	 * 
	 */
	public static final long L_MULTIPLIER_100000000 = 100_000_000L;

	/**
	 * 
	 */
	public static final double D_MULTIPLIER_100000000 = 100_000_000.0D;

	/**
	 * 
	 * @param price
	 * @return
	 */
	public final static long doubleToLong2(double d) {
		return (long) (d * L_MULTIPLIER_100);
	}

	/**
	 * 
	 * @param price
	 * @return
	 */
	public final static double longToDouble2(long l) {
		return l / D_MULTIPLIER_100;
	}

	/**
	 * 
	 * @param price
	 * @return
	 */
	public final static long doubleToLong4(double d) {
		return (long) (d * L_MULTIPLIER_10000);
	}

	/**
	 * 
	 * @param price
	 * @return
	 */
	public final static double longToDouble4(long l) {
		return l / D_MULTIPLIER_10000;
	}

	/**
	 * 
	 * @param price
	 * @return
	 */
	public final static long doubleToLong6(double d) {
		return (long) (d * L_MULTIPLIER_1000000);
	}

	/**
	 * 
	 * @param price
	 * @return
	 */
	public final static double longToDouble6(long l) {
		return l / D_MULTIPLIER_1000000;
	}

	/**
	 * 
	 * @param price
	 * @return
	 */
	public final static long doubleToLong8(double d) {
		return (long) (d * L_MULTIPLIER_100000000);
	}

	/**
	 * 
	 * @param price
	 * @return
	 */
	public final static double longToDouble8(long l) {
		return l / D_MULTIPLIER_100000000;
	}

	public static void main(String[] args) {

		System.out.println();

		System.out.println(longToDouble8(doubleToLong8(4.981312)));

	}

}

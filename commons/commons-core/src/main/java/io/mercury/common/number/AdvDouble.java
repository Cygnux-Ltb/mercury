package io.mercury.common.number;

/**
 * 
 * @author yellow013
 * 
 *         Use two long value expressed advanced double
 *
 */
public final class AdvDouble {

	private final long integer;
	private final long decimal;

	public final static AdvDouble newDouble() {
		return new AdvDouble(0, 0);
	}

	public final static AdvDouble newDouble(double doubleValue) {
		DoubleArithmetic.remainder(doubleValue, 1);
		return new AdvDouble(0, 0);
	}

	public final static AdvDouble newDouble(long integerPart, long decimalPart) {
		return new AdvDouble(integerPart, decimalPart);
	}

	public AdvDouble(long integer, long decimal) {
		this.integer = integer;
		this.decimal = decimal;
	}

	public long getIntegerPart() {
		return integer;
	}

	public long getDecimalPart() {
		return decimal;
	}

}

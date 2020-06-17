package io.mercury.common.number;

/**
 * 
 * @author yellow013
 * 
 *         Use two long value expressed advanced double
 *
 */
public final class AdvDouble {

	private long integerPart;
	private long decimalPart;
	
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

	public AdvDouble(long integerPart, long decimalPart) {
		this.integerPart = integerPart;
		this.decimalPart = decimalPart;
	}

	public long getIntegerPart() {
		return integerPart;
	}

	public long getDecimalPart() {
		return decimalPart;
	}

}

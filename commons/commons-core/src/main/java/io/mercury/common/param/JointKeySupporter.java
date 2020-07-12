package io.mercury.common.param;

import io.mercury.common.util.BitOperator;

public final class JointKeySupporter {

	/**
	 * 将两个int value合并为long value<br>
	 * long value高32位为第一个int值, 低32位为第二个int值
	 */
	public static long mergeJointKey(int highPos, int lowPos) {
		return BitOperator.mergeInt(highPos, lowPos);
	}

	/**
	 * 取出高位数值
	 * 
	 * @param jointKey
	 * @return
	 */
	public static int getHighPos(long jointKey) {
		return BitOperator.splitLongWithHighPos(jointKey);
	}

	/**
	 * 取出低位数值
	 * 
	 * @param jointId
	 * @return
	 */
	public static int getLowPos(long jointKey) {
		return BitOperator.splitLongWithLowPos(jointKey);
	}

}
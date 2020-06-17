package io.mercury.common.param;

import io.mercury.common.util.BitOperator;

public final class JointIdSupporter {

	/**
	 * 将两个int value合并为long value<br>
	 * long value高32位为第一个int值, 低32位为第二个int值
	 */
	public static long mergeJointId(int highPos, int lowPos) {
		return BitOperator.mergeInt(highPos, lowPos);
	}

	/**
	 * 取出高位数值
	 * 
	 * @param jointId
	 * @return
	 */
	public static int getHighPos(long jointId) {
		return BitOperator.splitLongWithHighPos(jointId);
	}

	/**
	 * 取出低位数值
	 * 
	 * @param jointId
	 * @return
	 */
	public static int getLowPos(long jointId) {
		return BitOperator.splitLongWithLowPos(jointId);
	}

}
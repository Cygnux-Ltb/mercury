package io.mercury.common.util;

public final class BitFormatter {

	private BitFormatter() {
	}

	/**
	 * 将[byte]转换为二进制输出
	 * 
	 * @param b
	 * @return
	 */
	public static final String byteBinary(byte b) {
		String binary = Integer.toBinaryString(b);
		return highPosFill(Byte.SIZE, Byte.SIZE - binary.length(), binary);
	}

	/**
	 * 将[char]转换为二进制输出, 高位补0
	 * 
	 * @param c
	 * @return
	 */
	public static final String charBinary(char c) {
		String binary = Integer.toBinaryString(c);
		return highPosFill(Character.SIZE, Character.SIZE - binary.length(), binary);
	}

	/**
	 * 将[char]转换为二进制输出并格式化, 高位补0
	 * 
	 * @param c
	 * @return
	 */
	public static final String charBinaryFormat(char c) {
		return new StringBuilder(17).append(charBinary(c)).insert(8, ' ').toString();
	}

	/**
	 * 将[short]转换为二进制输出, 高位补0
	 * 
	 * @param s
	 * @return
	 */
	public static final String shortBinary(short s) {
		String binary = Integer.toBinaryString(s);
		return highPosFill(Short.SIZE, Short.SIZE - binary.length(), binary);
	}

	/**
	 * 将[short]转换为二进制输出并格式化, 高位补0
	 * 
	 * @param s
	 * @return
	 */
	public static final String shortBinaryFormat(short s) {
		return new StringBuilder(17).append(shortBinary(s)).insert(8, ' ').toString();
	}

	/**
	 * 将[int]转换为二进制输出, 高位补0
	 * 
	 * @param i
	 * @return
	 */
	public static final String intBinary(int i) {
		String binary = Integer.toBinaryString(i);
		return highPosFill(Integer.SIZE, Integer.SIZE - binary.length(), binary);
	}

	/**
	 * 将[int]转换为二进制输出并格式化, 高位补0
	 * 
	 * @param i
	 * @return
	 */
	public static final String intBinaryFormat(int i) {
		return new StringBuilder(35).append(intBinary(i)).insert(24, ' ').insert(16, ' ').insert(8, ' ').toString();
	}

	/**
	 * 将[long]转换为二进制输出, 高位补0
	 * 
	 * @param l
	 * @return
	 */
	public static final String longBinary(long l) {
		String binary = Long.toBinaryString(l);
		return highPosFill(Long.SIZE, Long.SIZE - binary.length(), binary);
	}

	/**
	 * 将[long]转换为二进制输出并格式化, 高位补0
	 * 
	 * @param l
	 * @return
	 */
	public static final String longBinaryFormat(long l) {
		return new StringBuilder(71).append(longBinary(l)).insert(56, ' ').insert(48, ' ').insert(40, ' ')
				.insert(32, ' ').insert(24, ' ').insert(16, ' ').insert(8, ' ').toString();
	}

	/**
	 * 指定总长度, 空白长度, 实际值, 返回的字符串填充指定长度的0
	 * 
	 * @param sumLen
	 * @param blankLen
	 * @param binaryStr
	 * @return
	 */
	private static final String highPosFill(int sumLen, int blankLen, String binaryStr) {
		var builder = new StringBuilder(sumLen);
		for (int i = 0; i < blankLen; i++)
			builder.append('0');
		return builder.append(binaryStr).toString();
	}

}

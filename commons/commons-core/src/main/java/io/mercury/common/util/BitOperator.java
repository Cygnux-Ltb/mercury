package io.mercury.common.util;

import static java.lang.Integer.toBinaryString;

import javax.annotation.Nonnull;

public final class BitOperator {

	private BitOperator() {
	}

	/**
	 * 将[byte]转换为二进制输出, 高位补0
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
		String binaryStr = Integer.toBinaryString(c);
		return highPosFill(Character.SIZE, Character.SIZE - binaryStr.length(), binaryStr);
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
		String binaryStr = Integer.toBinaryString(s);
		return highPosFill(Short.SIZE, Short.SIZE - binaryStr.length(), binaryStr);
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
		String binaryStr = Integer.toBinaryString(i);
		return highPosFill(Integer.SIZE, Integer.SIZE - binaryStr.length(), binaryStr);
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
		String binaryStr = Long.toBinaryString(l);
		return highPosFill(Long.SIZE, Long.SIZE - binaryStr.length(), binaryStr);
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
		StringBuilder builder = new StringBuilder(sumLen);
		for (int i = 0; i < blankLen; i++)
			builder.append('0');
		return builder.append(binaryStr).toString();
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public static final char bytesToChar(@Nonnull byte[] bytes) throws ArrayIndexOutOfBoundsException {
		Assertor.requiredLength(bytes, 2, "bytes array");
		return (char) (((bytes[0] & 0xFF) << 8) | ((bytes[1] & 0xFF)));
	}

	/**
	 * 
	 * @param bytes
	 * @param offset
	 * @return
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public static final char bytesToChar(@Nonnull byte[] bytes, int offset) throws ArrayIndexOutOfBoundsException {
		if (bytes == null || bytes.length < offset + 2)
			throw new ArrayIndexOutOfBoundsException("byte array length must be greater than [offset + 2]");
		return (char) (((bytes[offset] & 0xFF) << 8) | ((bytes[offset + 1] & 0xFF)));
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public static final int bytesToInt(@Nonnull byte[] bytes) throws ArrayIndexOutOfBoundsException {
		Assertor.requiredLength(bytes, 4, "bytes array");
		return ((bytes[0] & 0xFF) << 24) | ((bytes[1] & 0xFF) << 16) | ((bytes[2] & 0xFF) << 8) | ((bytes[3] & 0xFF));
	}

	/**
	 * 
	 * @param bytes
	 * @param offset
	 * @return
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public static final int bytesToInt(@Nonnull byte[] bytes, int offset) throws ArrayIndexOutOfBoundsException {
		if (bytes == null || bytes.length < offset + 4)
			throw new ArrayIndexOutOfBoundsException("byte array length must be greater than [offset + 4]");
		return ((bytes[offset] & 0xFF) << 24) | ((bytes[offset + 1] & 0xFF) << 16) | ((bytes[offset + 2] & 0xFF) << 8)
				| ((bytes[offset + 3] & 0xFF));
	}

	/**
	 * 两个[char]合并为[int]
	 * 
	 * @param highPos
	 * @param lowPos
	 * @return
	 */
	public static final int mergeCharToInt(char highPos, char lowPos) {
		return (((int) highPos) << 16) | ((int) lowPos);
	}

	/**
	 * 四个[char]合并为[long]
	 * 
	 * @param highPos
	 * @param second
	 * @param third
	 * @param lowPos
	 * @return
	 */
	public static final long mergeCharToLong(char highPos, char second, char third, char lowPos) {
		return (((long) highPos) << 48) | ((long) second << 32) | ((long) third << 16) | ((int) lowPos);
	}

	/**
	 * 两个[int]合并为[long]
	 * 
	 * @param highPos
	 * @param lowPos
	 * @return
	 */
	public static final long mergeIntToLong(int highPos, int lowPos) {
		return (((long) highPos) << 32) | ((long) lowPos);
	}

	public static final long LongHighPosMask = 0xFFFF_FFFF_0000_0000L;

	/**
	 * 
	 * @param l
	 * @return
	 */
	public static final int splitLongWithHighPos(long l) {
		return (int) ((l & LongHighPosMask) >> 32);
	}

	public static final long LongLowPosMask = 0x0000_0000_FFFF_FFFFL;

	/**
	 * 
	 * @param l
	 * @return
	 */
	public static final int splitLongWithLowPos(long l) {
		return (int) (l & LongLowPosMask);
	}

	/**
	 * 
	 * @param i
	 * @return
	 */
	public static final boolean isOdd(int i) {
		return (i & 1) != 0;
	}

	/**
	 * 
	 * @param i
	 * @return
	 */
	public static final boolean isEven(int i) {
		return !isOdd(i);
	}

	/**
	 * 
	 * @param l
	 * @return
	 */
	public static final boolean isOdd(long l) {
		return (l & 1) != 0;
	}

	/**
	 * 
	 * @param l
	 * @return
	 */
	public static final boolean isEven(long l) {
		return !isOdd(l);
	}

	private static final int MAXIMUM_CAPACITY = 1 << 30;

	/**
	 * 返回参数的最小的2的幂
	 * 
	 * @param i
	 * @return
	 */
	public static final int minPow2(int i) {
		int n = i - 1;
		n |= n >>> 1;
		n |= n >>> 2;
		n |= n >>> 4;
		n |= n >>> 8;
		n |= n >>> 16;
		return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
	}

	public static void main(String[] args) {

		int i1 = 1002;
		int i2 = 10777;

		System.out.println(intBinaryFormat(i1));
		System.out.println(intBinaryFormat(i2));

		System.out.println((mergeIntToLong(i1, i2)));
		System.out.println(longBinaryFormat(mergeIntToLong(i1, i2)));

		System.out.println((splitLongWithHighPos(mergeIntToLong(i1, i2))));
		System.out.println(intBinaryFormat(splitLongWithHighPos(mergeIntToLong(i1, i2))));

		System.out.println((splitLongWithLowPos(mergeIntToLong(i1, i2))));
		System.out.println(intBinaryFormat(splitLongWithLowPos(mergeIntToLong(i1, i2))));

		System.out.println(intBinaryFormat(1));
		System.out.println(intBinaryFormat(~1));

		System.out.println(intBinaryFormat(10));
		System.out.println(intBinaryFormat(20));
		System.out.println(intBinaryFormat(10 ^ 20));
		System.out.println(intBinaryFormat((10 ^ 20) ^ 20));

		System.out.println(intBinaryFormat(10243250));
		System.out.println(intBinaryFormat(1));
		System.out.println(intBinaryFormat(10243250 & 1));
		System.out.println(intBinaryFormat(1123121));
		System.out.println(intBinaryFormat(1));
		System.out.println(intBinaryFormat(1123121 & 1));

		byte b = 3;

		System.out.println(charBinary('b'));
		System.out.println(charBinaryFormat('b'));

		System.out.println(intBinary(10777));
		System.out.println(intBinaryFormat(10777));

		System.out.println(longBinary(106544777L));
		System.out.println(longBinaryFormat(106544777L));

		System.out.println(byteBinary(b));

		System.out.println(longBinary(-3L));
		System.out.println(intBinary(-3));
		System.out.println(charBinary('3'));
		System.out.println(intBinary(2));
		System.out.println(toBinaryString(2));
		System.out.println(intBinary(-10));

		System.out.println(longBinary(Long.MAX_VALUE));
		System.out.println(longBinary(Long.MIN_VALUE));
		System.out.println(longBinary(-1L));
		System.out.println(longBinary(2L << 10));

	}

}

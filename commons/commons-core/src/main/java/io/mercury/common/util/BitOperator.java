package io.mercury.common.util;

import static io.mercury.common.util.BitFormatter.longBinaryFormat;

import javax.annotation.Nonnull;

import io.mercury.common.lang.Assertor;

public final class BitOperator {

	private BitOperator() {
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
	public static final int merge(char highPos, char lowPos) {
		return (((int) highPos) << 16) | ((int) lowPos);
	}

	/**
	 * 两个<b> [char] </b>合并为<b> [int] </b>
	 * 
	 * @param highPos
	 * @param lowPos
	 * @return
	 */
	public static final int merge(short highPos, short lowPos) {
		return (((int) highPos) << 16) | ((int) lowPos);
	}

	/**
	 * 四个<b> [char] </b>合并为<b> [long] </b>
	 * 
	 * @param highPos
	 * @param second
	 * @param third
	 * @param lowPos
	 * @return
	 */
	public static final long merge(char highPos, char second, char third, char lowPos) {
		return (((long) highPos) << 48) | ((long) second << 32) | ((long) third << 16) | ((int) lowPos);
	}

	/**
	 * 四个<b> [short] </b>合并为<b> [long] </b>
	 * 
	 * @param highPos
	 * @param second
	 * @param third
	 * @param lowPos
	 * @return
	 */
	public static final long merge(short highPos, short second, short third, short lowPos) {
		return (((long) highPos) << 48) | ((long) second << 32) | ((long) third << 16) | ((int) lowPos);
	}

	/**
	 * 两个<b> [int] </b>合并为<b> [long] </b>
	 * 
	 * @param highPos
	 * @param lowPos
	 * @return
	 */
	public static final long merge(int highPos, int lowPos) {
		return (((long) highPos) << 32) | ((long) lowPos);
	}

	/*
	 * 高位掩码
	 */
	public static final long LongHighPosMask = 0xFFFF_FFFF_0000_0000L;

	/**
	 * 
	 * @param l
	 * @return
	 */
	public static final int getLongHighPos(long l) {
		return (int) ((l & LongHighPosMask) >> 32);
	}

	/*
	 * 低位掩码
	 */
	public static final long LongLowPosMask = 0x0000_0000_FFFF_FFFFL;

	/**
	 * 
	 * @param l
	 * @return
	 */
	public static final int getLongLowPos(long l) {
		return (int) (l & LongLowPosMask);
	}

	/**
	 * 奇数
	 * 
	 * @param i
	 * @return
	 */
	public static final boolean isOdd(int i) {
		return (i & 1) != 0;
	}

	/**
	 * 偶数
	 * 
	 * @param i
	 * @return
	 */
	public static final boolean isEven(int i) {
		return !isOdd(i);
	}

	/**
	 * 奇数
	 * 
	 * @param l
	 * @return
	 */
	public static final boolean isOdd(long l) {
		return (l & 1) != 0;
	}

	/**
	 * 偶数
	 * 
	 * @param l
	 * @return
	 */
	public static final boolean isEven(long l) {
		return !isOdd(l);
	}

	private static final int MAXIMUM_CAPACITY = 1 << 30;

	/**
	 * 返回最接近参数的2的幂
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

	/**
	 * 
	 * 获取n位所能表示的最大值
	 * 
	 * @param n
	 * @return
	 */
	public static long maxValueOfBit(int n) {
		return n < 1 || n > 63 ? 0 : -1L ^ (-1L << n);
	}

	/**
	 * 清理DirectMemory
	 * 
	 * @param buffer
	 */
//	@SuppressWarnings("restriction")
//	public static boolean cleanDirectMemory(final ByteBuffer buffer) throws RuntimeException {
//		if (buffer.isDirect()) {
//			if (buffer instanceof sun.nio.ch.DirectBuffer) {
//				try {
//					((sun.nio.ch.DirectBuffer) buffer).cleaner().clean();
//				} catch (Exception e) {
//					throw new RuntimeException("call '((sun.nio.ch.DirectBuffer) buffer).cleaner().clean()' exception",
//							e);
//				}
//				return true;
//			}
//		}
//		return false;
//	}

	public static void main(String[] args) {

		int i = 46346;

		System.out.println(longBinaryFormat(-1L));
		System.out.println(longBinaryFormat(-1L << i));
		System.out.println(longBinaryFormat(-1L << Math.abs(i)));
		System.out.println(longBinaryFormat(-1L ^ (-1L << i)));
		System.out.println(maxValueOfBit(i));

	}

}

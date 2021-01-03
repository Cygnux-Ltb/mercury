package io.mercury.common.util;

import static io.mercury.common.util.BitFormatter.byteBinary;
import static io.mercury.common.util.BitFormatter.charBinary;
import static io.mercury.common.util.BitFormatter.charBinaryFormat;
import static io.mercury.common.util.BitFormatter.intBinary;
import static io.mercury.common.util.BitFormatter.intBinaryFormat;
import static io.mercury.common.util.BitFormatter.longBinary;
import static io.mercury.common.util.BitFormatter.longBinaryFormat;
import static java.lang.Integer.toBinaryString;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

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

	/**
	 * 
	 * @param buffer
	 */
	@SuppressWarnings({ "restriction" })
	public static boolean cleanDirectMemory(final ByteBuffer buffer) throws RuntimeException {
		if (buffer.isDirect()) {
			if (buffer instanceof sun.nio.ch.DirectBuffer) {
				try {
					((sun.nio.ch.DirectBuffer) buffer).cleaner().clean();
				} catch (Exception e) {
					throw new RuntimeException("call '((sun.nio.ch.DirectBuffer) buffer).cleaner().clean()' exception",
							e);
				}
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {

		int i1 = 1002;
		int i2 = 10777;

		System.out.println(intBinaryFormat(i1));
		System.out.println(intBinaryFormat(i2));

		System.out.println(merge(i1, i2));
		System.out.println(longBinaryFormat(merge(i1, i2)));

		System.out.println(getLongHighPos(merge(i1, i2)));
		System.out.println(intBinaryFormat(getLongHighPos(merge(i1, i2))));

		System.out.println(getLongLowPos(merge(i1, i2)));
		System.out.println(intBinaryFormat(getLongLowPos(merge(i1, i2))));

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

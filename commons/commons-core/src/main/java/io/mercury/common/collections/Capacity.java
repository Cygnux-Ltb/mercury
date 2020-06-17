package io.mercury.common.collections;

import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

import io.mercury.common.util.BitOperator;

public enum Capacity {

	/**
	 * 1 << 4 : 16
	 */
	L04_SIZE_16(1 << 4),

	/**
	 * 1 << 5 : 32
	 */
	L05_SIZE_32(1 << 5),

	/**
	 * 1 << 6 : 64
	 */
	L06_SIZE_64(1 << 6),

	/**
	 * 1 << 7 : 128
	 */
	L07_SIZE_128(1 << 7),

	/**
	 * 1 << 8 : 256
	 */
	L08_SIZE_256(1 << 8),

	/**
	 * 1 << 9 : 512
	 */
	L09_SIZE_512(1 << 9),

	/**
	 * 1 << 10 : 1024
	 */
	L10_SIZE_1024(1 << 10),

	/**
	 * 1 << 11 : 2048
	 */
	L11_SIZE_2048(1 << 11),

	/**
	 * 1 << 12 : 4096
	 */
	L12_SIZE_4096(1 << 12),

	/**
	 * 1 << 13 : 8192
	 */
	L13_SIZE_8192(1 << 13),

	/**
	 * 1 << 14 : 16384
	 */
	L14_SIZE_16384(1 << 14),

	/**
	 * 1 << 15 : 32768
	 */
	L15_SIZE_32768(1 << 15),

	/**
	 * 1 << 16 : 65536
	 */
	L16_SIZE_65536(1 << 16),

	/**
	 * 1 << 17 : 131072
	 */
	L17_SIZE_131072(1 << 17),

	/**
	 * 1 << 18 : 262144
	 * 
	 */
	L18_SIZE_262144(1 << 18),

	/**
	 * 1 << 19 : 524288
	 */
	L19_SIZE_524288(1 << 19),

	/**
	 * 1 << 20 : 1048576
	 */
	L20_SIZE_1048576(1 << 20),

	/**
	 * 1 << 21 : 2097152
	 */
	L21_SIZE_2097152(1 << 21),

	/**
	 * 1 << 22 : 4194304
	 */
	L22_SIZE_4194304(1 << 22),

	/**
	 * 1 << 23 : 8388608
	 */
	L23_SIZE_8388608(1 << 23),

	/**
	 * 1 << 24 : 16777216
	 */
	L24_SIZE_16777216(1 << 24),

	/**
	 * 1 << 25 : 33554432
	 */
	L25_SIZE_33554432(1 << 25),

	/**
	 * 1 << 26 : 67108864
	 */
	L26_SIZE_67108864(1 << 26),

	/**
	 * 1 << 27 : 134217728
	 */
	L27_SIZE_134217728(1 << 27),

	/**
	 * 1 << 28 : 268435456
	 */
	L28_SIZE_268435456(1 << 28),

	/**
	 * 1 << 29 : 536870912
	 */
	L29_SIZE_536870912(1 << 29),

	/**
	 * 1 << 30 : 1073741824
	 */
	L30_SIZE_1073741824(1 << 30),

	;

	private static final MutableIntObjectMap<Capacity> ValueMap = new IntObjectHashMap<Capacity>();

	static {
		for (Capacity capacity : Capacity.values()) {
			ValueMap.put(capacity.size, capacity);
		}
	}

	private final int size;

	private Capacity(int size) {
		this.size = size;
	}

	public int size() {
		return size;
	}

	/**
	 * 
	 * @return min size 16
	 */
	public Capacity half() {
		return get(size >> 1);
	}

	/**
	 * 
	 * @return min size 16
	 */
	public Capacity quarter() {
		return get(size >> 2);
	}

	public Capacity get(int size) {
		int pow2 = BitOperator.minPow2(size);
		return pow2 >= 16 ? Capacity.L04_SIZE_16 : ValueMap.get(pow2);
	}

	public static void main(String[] args) {

		System.out.println(1 << 30);
		System.out.println(BitOperator.intBinaryFormat(1 << 30));
		System.out.println(BitOperator.intBinaryFormat(Integer.MIN_VALUE << 65));

		System.out.println(Capacity.L09_SIZE_512.quarter());

	}

}

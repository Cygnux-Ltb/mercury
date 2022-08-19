package io.mercury.common.collections;

import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

import io.mercury.common.util.BitFormatter;
import io.mercury.common.util.BitOperator;

public enum Capacity {

    /**
     * <pre>
     * Size : 1 << 2 == 4
     * </pre>
     */
    L02_SIZE(1 << 2),

    /**
     * <pre>
     * Size : 1 << 3 == 8
     * </pre>
     */
    L03_SIZE(1 << 3),

    /**
     * <pre>
     * Size : 1 << 4 == 16
     * </pre>
     */
    L04_SIZE(1 << 4),

    /**
     * <pre>
     * Size : 1 << 5 == 32
     * </pre>
     */
    L05_SIZE(1 << 5),

    /**
     * <pre>
     * Size : 1 << 6 == 64
     * </pre>
     */
    L06_SIZE(1 << 6),

    /**
     * <pre>
     * Size : 1 << 7 == 128
     * </pre>
     */
    L07_SIZE(1 << 7),

    /**
     * <pre>
     * Size : 1 << 8 == 256
     * </pre>
     */
    L08_SIZE(1 << 8),

    /**
     * <pre>
     * Size : 1 << 9 == 512
     * </pre>
     */
    L09_SIZE(1 << 9),

    /**
     * <pre>
     * Size : 1 << 10 == 1024
     * </pre>
     */
    L10_SIZE(1 << 10),

    /**
     * <pre>
     * Size : 1 << 11 == 2048
     * </pre>
     */
    L11_SIZE(1 << 11),

    /**
     * <pre>
     * Size : 1 << 12 == 4096
     * </pre>
     */
    L12_SIZE(1 << 12),

    /**
     * <pre>
     * Size : 1 << 13 == 8192
     * </pre>
     */
    L13_SIZE(1 << 13),

    /**
     * <pre>
     * Size : 1 << 14 == 16384
     * </pre>
     */
    L14_SIZE(1 << 14),

    /**
     * <pre>
     * Size : 1 << 15 == 32768
     * </pre>
     */
    L15_SIZE(1 << 15),

    /**
     * <pre>
     * Size : 1 << 16 == 65536
     * </pre>
     */
    L16_SIZE(1 << 16),

    /**
     * <pre>
     * Size : 1 << 17 == 131072
     * </pre>
     */
    L17_SIZE(1 << 17),

    /**
     * <pre>
     * Size : 1 << 18 == 262144
     * </pre>
     */
    L18_SIZE(1 << 18),

    /**
     * <pre>
     * Size : 1 << 19 == 524288
     * </pre>
     */
    L19_SIZE(1 << 19),

    /**
     * <pre>
     * Size : 1 << 20 == 1048576
     * </pre>
     */
    L20_SIZE(1 << 20),

    /**
     * <pre>
     * Size : 1 << 21 == 2097152
     * </pre>
     */
    L21_SIZE(1 << 21),

    /**
     * <pre>
     * Size : 1 << 22 == 4194304
     * </pre>
     */
    L22_SIZE(1 << 22),

    /**
     * <pre>
     * Size : 1 << 23 == 8388608
     * </pre>
     */
    L23_SIZE(1 << 23),

    /**
     * <pre>
     * Size : 1 << 24 == 16777216
     * </pre>
     */
    L24_SIZE(1 << 24),

    /**
     * <pre>
     * Size : 1 << 25 == 33554432
     * </pre>
     */
    L25_SIZE(1 << 25),

    /**
     * <pre>
     * Size : 1 << 26 == 67108864
     * </pre>
     */
    L26_SIZE(1 << 26),

    /**
     * <pre>
     * Size : 1 << 27 == 134217728
     * </pre>
     */
    L27_SIZE(1 << 27),

    /**
     * <pre>
     * Size : 1 << 28 == 268435456
     * </pre>
     */
    L28_SIZE(1 << 28),

    /**
     * <pre>
     * Size : 1 << 29 == 536870912
     * </pre>
     */
    L29_SIZE(1 << 29),

    /**
     * <pre>
     * Size : 1 << 30 == 1073741824
     * </pre>
     */
    L30_SIZE(1 << 30),

    ;

    public static final int DEFAULT_SIZE = 16;

    private static final MutableIntObjectMap<Capacity> ValueMap = new IntObjectHashMap<Capacity>();

    static {
        for (Capacity capacity : Capacity.values())
            ValueMap.put(capacity.value, capacity);
    }

    private final int value;

    Capacity(int value) {
        this.value = value;
    }

    /**
     * @param capacity Capacity
     * @return int
     */
    public static int checkAndGet(Capacity capacity) {
        return capacity == null ? Capacity.DEFAULT_SIZE : capacity.value();
    }

    /**
     * @return int
     */
    public int value() {
        return value;
    }

    /**
     * @return min size 16
     */
    public Capacity half() {
        return get(value >> 1);
    }

    /**
     * @return min size 16
     */
    public Capacity quarter() {
        return get(value >> 2);
    }

    /**
     * @param value int
     * @return Capacity
     */
    public Capacity get(int value) {
        int pow2 = BitOperator.minPow2(value);
        return pow2 >= DEFAULT_SIZE ? Capacity.L04_SIZE : ValueMap.get(pow2);
    }

    public static void main(String[] args) {

        System.out.println(1 << 30);
        System.out.println(BitFormatter.intBinaryFormat(1 << 30));
        System.out.println(1 << 31);
        System.out.println(BitFormatter.intBinaryFormat(1 << 31));
        System.out.println(BitFormatter.intBinaryFormat(Integer.MIN_VALUE << 65));
        System.out.println(Capacity.L09_SIZE.quarter());

    }

}

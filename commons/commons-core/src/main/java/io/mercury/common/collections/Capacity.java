package io.mercury.common.collections;

import io.mercury.common.util.BitFormatter;
import io.mercury.common.util.BitOperator;
import org.eclipse.collections.api.map.primitive.ImmutableIntObjectMap;

import static io.mercury.common.collections.MutableMaps.newIntObjectMap;

public enum Capacity {

    /**
     * <pre>
     * Size : 1 << 2 == 4
     * </pre>
     */
    L02_4(1 << 2),

    /**
     * <pre>
     * Size : 1 << 3 == 8
     * </pre>
     */
    L03_8(1 << 3),

    /**
     * <pre>
     * Size : 1 << 4 == 16
     * </pre>
     */
    L04_16(1 << 4),

    /**
     * <pre>
     * Size : 1 << 5 == 32
     * </pre>
     */
    L05_32(1 << 5),

    /**
     * <pre>
     * Size : 1 << 6 == 64
     * </pre>
     */
    L06_64(1 << 6),

    /**
     * <pre>
     * Size : 1 << 7 == 128
     * </pre>
     */
    L07_128(1 << 7),

    /**
     * <pre>
     * Size : 1 << 8 == 256
     * </pre>
     */
    L08_256(1 << 8),

    /**
     * <pre>
     * Size : 1 << 9 == 512
     * </pre>
     */
    L09_512(1 << 9),

    /**
     * <pre>
     * Size : 1 << 10 == 1024
     * </pre>
     */
    L10_1024(1 << 10),

    /**
     * <pre>
     * Size : 1 << 11 == 2048
     * </pre>
     */
    L11_2048(1 << 11),

    /**
     * <pre>
     * Size : 1 << 12 == 4096
     * </pre>
     */
    L12_4096(1 << 12),

    /**
     * <pre>
     * Size : 1 << 13 == 8192
     * </pre>
     */
    L13_8192(1 << 13),

    /**
     * <pre>
     * Size : 1 << 14 == 16384
     * </pre>
     */
    L14_16384(1 << 14),

    /**
     * <pre>
     * Size : 1 << 15 == 32768
     * </pre>
     */
    L15_32768(1 << 15),

    /**
     * <pre>
     * Size : 1 << 16 == 65536
     * </pre>
     */
    L16_65536(1 << 16),

    /**
     * <pre>
     * Size : 1 << 17 == 131072
     * </pre>
     */
    L17_131072(1 << 17),

    /**
     * <pre>
     * Size : 1 << 18 == 262144
     * </pre>
     */
    L18_262144(1 << 18),

    /**
     * <pre>
     * Size : 1 << 19 == 524288
     * </pre>
     */
    L19_524288(1 << 19),

    /**
     * <pre>
     * Size : 1 << 20 == 1048576
     * </pre>
     */
    L20_1048576(1 << 20),

    /**
     * <pre>
     * Size : 1 << 21 == 2097152
     * </pre>
     */
    L21_2097152(1 << 21),

    /**
     * <pre>
     * Size : 1 << 22 == 4194304
     * </pre>
     */
    L22_4194304(1 << 22),

    /**
     * <pre>
     * Size : 1 << 23 == 8388608
     * </pre>
     */
    L23_8388608(1 << 23),

    /**
     * <pre>
     * Size : 1 << 24 == 16777216
     * </pre>
     */
    L24_16777216(1 << 24),

    /**
     * <pre>
     * Size : 1 << 25 == 33554432
     * </pre>
     */
    L25_33554432(1 << 25),

    /**
     * <pre>
     * Size : 1 << 26 == 67108864
     * </pre>
     */
    L26_67108864(1 << 26),

    /**
     * <pre>
     * Size : 1 << 27 == 134217728
     * </pre>
     */
    L27_134217728(1 << 27),

    /**
     * <pre>
     * Size : 1 << 28 == 268435456
     * </pre>
     */
    L28_268435456(1 << 28),

    /**
     * <pre>
     * Size : 1 << 29 == 536870912
     * </pre>
     */
    L29_536870912(1 << 29),

    /**
     * <pre>
     * Size : 1 << 30 == 1073741824
     * </pre>
     */
    L30_1073741824(1 << 30),

    ;

    public static final int DEFAULT_SIZE = 16;

    private static final ImmutableIntObjectMap<Capacity> VALUE_MAP =
            newIntObjectMap(Capacity::size, Capacity.values()).toImmutable();

    private final int size;

    Capacity(int size) {
        this.size = size;
    }

    /**
     * @param capacity Capacity
     * @return int
     */
    public static int checkAndGet(Capacity capacity) {
        return capacity == null ? Capacity.DEFAULT_SIZE : capacity.size();
    }

    /**
     * @return int
     */
    public int size() {
        return size;
    }

    /**
     * @return min size 16
     */
    public Capacity halfSize() {
        return get(size >> 1);
    }


    /**
     * @param value int
     * @return Capacity
     */
    public Capacity get(int value) {
        int pow2 = BitOperator.minPow2(value);
        return pow2 >= DEFAULT_SIZE ? Capacity.L04_16 : VALUE_MAP.get(pow2);
    }

    public static void main(String[] args) {

        System.out.println(1 << 30);
        System.out.println(BitFormatter.intBinaryFormat(1 << 30));
        System.out.println(1 << 31);
        System.out.println(BitFormatter.intBinaryFormat(1 << 31));
        System.out.println(BitFormatter.intBinaryFormat(Integer.MIN_VALUE));

    }

}

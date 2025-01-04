package io.mercury.common.collections.list;

public final class SortedFixedArray {

    private SortedFixedArray() {
        throw new IllegalAccessError("Utility class");
    }

    public static SortedFixedIntArray newIntArray(int length) {
        return new SortedFixedIntArray(length);
    }

    public static SortedFixedLongArray newLongArray(int length) {
        return new SortedFixedLongArray(length);
    }

    public static SortedFixedDoubleArray newDoubleArray(int length) {
        return new SortedFixedDoubleArray(length);
    }

}

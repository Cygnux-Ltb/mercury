package io.mercury.common.lang;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public final class Predicates {

    private Predicates() {
    }

    /**
     * @param value int
     * @param min   int
     * @param max   int
     * @return boolean
     */
    public static boolean atWithinRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**
     * @param l   long
     * @param min long
     * @param max long
     * @return boolean
     */
    public static boolean atWithinRange(long l, long min, long max) {
        return (l >= min && l <= max);
    }

    /**
     * @param <E>            Collection element type
     * @param collection     Collection<E>
     * @param requiredLength int
     * @return boolean
     */
    public static <E> boolean requiredLength(Collection<E> collection, int requiredLength) {
        return collection != null && collection.size() >= requiredLength;
    }

    /**
     * @param <E>            List element type
     * @param list           List<E>
     * @param requiredLength int
     * @return boolean
     */
    public static <E> boolean requiredLength(List<E> list, int requiredLength) {
        return list != null && list.size() >= requiredLength;
    }


    /**
     * @param <K>            Map key type
     * @param <V>            Map value type
     * @param map            List<E>
     * @param requiredLength int
     * @return boolean
     */
    public static <K, V> boolean requiredLength(Map<K, V> map, int requiredLength) {
        return map != null && map.size() >= requiredLength;
    }

    /**
     * @param <T>            array element type
     * @param array          T[]
     * @param requiredLength int
     * @return boolean
     */
    public static <T> boolean requiredLength(T[] array, int requiredLength) {
        return array != null && array.length >= requiredLength;
    }

    /**
     * @param array          boolean[]
     * @param requiredLength int
     * @return boolean
     */
    public static boolean requiredLength(boolean[] array, int requiredLength) {
        return array != null && array.length >= requiredLength;
    }

    /**
     * @param array          byte[]
     * @param requiredLength int
     * @return boolean
     */
    public static boolean requiredLength(byte[] array, int requiredLength) {
        return array != null && array.length >= requiredLength;
    }

    /**
     * @param array          char[]
     * @param requiredLength int
     * @return boolean
     */
    public static boolean requiredLength(char[] array, int requiredLength) {
        return array != null && array.length >= requiredLength;
    }

    /**
     * @param array          int[]
     * @param requiredLength int
     * @return boolean
     */
    public static boolean requiredLength(int[] array, int requiredLength) {
        return array != null && array.length >= requiredLength;
    }

    /**
     * @param array          long[]
     * @param requiredLength int
     * @return boolean
     */
    public static boolean requiredLength(long[] array, int requiredLength) {
        return array != null && array.length >= requiredLength;
    }

    /**
     * @param array          float[]
     * @param requiredLength int
     * @return boolean
     */
    public static boolean requiredLength(float[] array, int requiredLength) {
        return array != null && array.length >= requiredLength;
    }

    /**
     * @param array          double[]
     * @param requiredLength int
     * @return boolean
     */
    public static boolean requiredLength(double[] array, int requiredLength) {
        return array != null && array.length >= requiredLength;
    }

    /**
     * @param <T>       T
     * @param param     T
     * @param predicate Predicate<T>
     * @return boolean
     */
    public static <T> boolean isValid(T param, Predicate<T> predicate) {
        return predicate.test(param);
    }

}

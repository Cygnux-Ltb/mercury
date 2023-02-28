package io.mercury.common.lang;

import io.mercury.common.util.StringSupport;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public final class Asserter {

    private Asserter() {
    }

    /**
     * @param i
     * @param min
     * @param objName
     * @return
     * @throws IllegalArgumentException exception
     */
    public static int greaterThan(int i, int min, String objName)
            throws IllegalArgumentException {
        if (i > min)
            return i;
        throw new IllegalArgumentException("Param: [" + objName + "] must greater than " + min);
    }

    /**
     * @param l
     * @param min
     * @param objName
     * @return
     * @throws IllegalArgumentException exception
     */
    public static long greaterThan(long l, long min, String objName)
            throws IllegalArgumentException {
        if (l > min)
            return l;
        throw new IllegalArgumentException("Param: [" + objName + "] must greater than " + min);
    }

    /**
     * @param i
     * @param min
     * @param objName
     * @return
     * @throws IllegalArgumentException exception
     */
    public static int greaterOrEqualThan(int i, int min, String objName)
            throws IllegalArgumentException {
        if (i >= min)
            return i;
        throw new IllegalArgumentException("Param: [" + objName + "] must greater or equal than " + min);
    }

    /**
     * @param l
     * @param min
     * @param objName
     * @return
     * @throws IllegalArgumentException exception
     */
    public static long greaterOrEqualThan(long l, long min, String objName)
            throws IllegalArgumentException {
        if (l >= min)
            return l;
        throw new IllegalArgumentException("Param: [" + objName + "] must greater or equal than " + min);
    }

    /**
     * @param i
     * @param max
     * @param objName
     * @return
     * @throws IllegalArgumentException exception
     */
    public static int lessThan(int i, int max, String objName)
            throws IllegalArgumentException {
        if (i < max)
            return i;
        throw new IllegalArgumentException("Param: [" + objName + "] must less than " + max);
    }

    /**
     * @param l
     * @param max
     * @param objName
     * @return
     * @throws IllegalArgumentException exception
     */
    public static long lessThan(long l, long max, String objName)
            throws IllegalArgumentException {
        if (l < max)
            return l;
        throw new IllegalArgumentException("Param: [" + objName + "] must less than " + max);
    }

    /**
     * @param i
     * @param max
     * @param objName
     * @return
     * @throws IllegalArgumentException exception
     */
    public static int lessOrEqualThan(int i, int max, String objName)
            throws IllegalArgumentException {
        if (i <= max)
            return i;
        throw new IllegalArgumentException("Param: [" + objName + "] must less or equal than " + max);
    }

    /**
     * @param l       long
     * @param max     long
     * @param objName String
     * @return long
     * @throws IllegalArgumentException exception
     */
    public static long lessOrEqualThan(long l, long max, String objName)
            throws IllegalArgumentException {
        if (l <= max)
            return l;
        throw new IllegalArgumentException("Param: [" + objName + "] must less or equal than " + max);
    }

    /**
     * @param i       int
     * @param min     int
     * @param max     int
     * @param objName String
     * @return int
     * @throws IllegalArgumentException exception
     */
    public static int atWithinRange(int i, int min, int max, String objName)
            throws IllegalArgumentException {
        if (i >= min && i <= max)
            return i;
        throw new IllegalArgumentException(
                "Param: [" + objName + "] must in the range of [" + min + "] to [" + max + "]");
    }

    /**
     * @param l       long
     * @param min     long
     * @param max     long
     * @param objName String
     * @return long
     * @throws IllegalArgumentException exception
     */
    public static long atWithinRange(long l, long min, long max, String objName)
            throws IllegalArgumentException {
        if (l >= min && l <= max)
            return l;
        throw new IllegalArgumentException(objName + " must in the range of [" + min + "] to [" + max + "]");
    }

    /**
     * @param <T> T
     * @param t   T
     * @return T
     * @throws NullPointerException exception
     */
    public static <T> T nonNull(T t) throws NullPointerException {
        return nonNull(t, "");
    }

    /**
     * @param <T>     T
     * @param t       T
     * @param objName String
     * @return T
     * @throws NullPointerException exception
     */
    public static <T> T nonNull(T t, @Nonnull String objName)
            throws NullPointerException {
        if (t == null)
            if (StringSupport.isNullOrEmpty(objName))
                throw new NullPointerException("param cannot be null");
            else
                throw new NullPointerException("Param: [" + objName + "] cannot be null");
        return t;
    }

    /**
     * @param <T>
     * @param <E>
     * @param t
     * @param e
     * @return
     * @throws E exception
     */
    public static <T, E extends Throwable> T nonNull(T t, E e) throws E {
        if (t == null)
            throw e;
        return t;
    }

    /**
     * @param str
     * @param objName
     * @return
     * @throws NullPointerException     exception
     * @throws IllegalArgumentException exception
     */
    public static String nonEmpty(String str, String objName)
            throws NullPointerException, IllegalArgumentException {
        if (str == null)
            throw new NullPointerException("Param: [" + objName + "] can not be null");
        if (str.length() == 0)
            throw new IllegalArgumentException("Param: [" + objName + "] can not be empty");
        return str;
    }

    /**
     * @param <C>        C
     * @param collection C
     * @param objName    String
     * @return C
     * @throws NullPointerException     exception
     * @throws IllegalArgumentException exception
     */
    public static <C extends Collection<E>, E> C nonEmptyCollection(C collection,
                                                                    String objName)
            throws NullPointerException, IllegalArgumentException {
        if (collection == null)
            throw new NullPointerException("Param: [" + objName + "] can not be null");
        if (collection.isEmpty())
            throw new IllegalArgumentException("Param: [" + objName + "] can not be empty");
        return collection;
    }

    /**
     * @param <T>
     * @param map
     * @param objName
     * @return
     * @throws NullPointerException     exception
     * @throws IllegalArgumentException exception
     */
    public static <T extends Map<?, ?>> T nonEmptyMap(T map, String objName)
            throws NullPointerException, IllegalArgumentException {
        if (map == null)
            throw new NullPointerException("Param: [" + objName + "] can not be null");
        if (map.isEmpty())
            throw new IllegalArgumentException("Param: [" + objName + "] can not be empty");
        return map;
    }

    /**
     * @param <T>
     * @param array
     * @param requiredLength
     * @param arrayName
     * @return
     * @throws NullPointerException     exception
     * @throws IllegalArgumentException exception
     */
    public static <T> T[] requiredLength(T[] array, int requiredLength, String arrayName)
            throws NullPointerException, IllegalArgumentException {
        if (array == null)
            throw new NullPointerException("Param: [" + arrayName + "] can not be null");
        if (array.length < requiredLength)
            throw new IllegalArgumentException(
                    "Param: [" + arrayName + "] length must be greater than " + requiredLength);
        return array;
    }

    /**
     * @param <E>
     * @param collection
     * @param requiredLength
     * @param arrayName
     * @return
     * @throws NullPointerException     exception
     * @throws IllegalArgumentException exception
     */
    public static <E> Collection<E> requiredLength(Collection<E> collection,
                                                   int requiredLength,
                                                   String arrayName)
            throws NullPointerException, IllegalArgumentException {
        if (collection == null)
            throw new NullPointerException("Param: [" + arrayName + "] can not be null");
        if (collection.size() < requiredLength)
            throw new IllegalArgumentException(
                    "Param: [" + arrayName + "] length must be greater than " + requiredLength);
        return collection;
    }

    /**
     * @param <T>
     * @param list
     * @param requiredLength
     * @param listName
     * @return
     * @throws NullPointerException     exception
     * @throws IllegalArgumentException exception
     */
    public static <T> List<T> requiredLength(List<T> list,
                                             int requiredLength,
                                             String listName)
            throws NullPointerException, IllegalArgumentException {
        if (list == null)
            throw new NullPointerException("Param: [" + listName + "] can not be null");
        if (list.size() < requiredLength)
            throw new IllegalArgumentException(
                    "Param: [" + listName + "] length must be greater than " + requiredLength);
        return list;
    }

    /**
     * @param array
     * @param requiredLength
     * @param arrayName
     * @return
     * @throws NullPointerException     exception
     * @throws IllegalArgumentException exception
     */
    public static boolean[] requiredLength(boolean[] array,
                                           int requiredLength,
                                           String arrayName)
            throws NullPointerException, IllegalArgumentException {
        if (array == null)
            throw new NullPointerException("Param: [" + arrayName + "] can not be null");
        if (array.length < requiredLength)
            throw new IllegalArgumentException(
                    "Param: [" + arrayName + "] length must be greater than " + requiredLength);
        return array;
    }

    /**
     * @param array
     * @param requiredLength
     * @param arrayName
     * @return
     * @throws NullPointerException     exception
     * @throws IllegalArgumentException exception
     */
    public static byte[] requiredLength(byte[] array, int requiredLength, String arrayName)
            throws NullPointerException, IllegalArgumentException {
        if (array == null)
            throw new NullPointerException("Param: [" + arrayName + "] can not be null");
        if (array.length < requiredLength)
            throw new IllegalArgumentException(
                    "Param: [" + arrayName + "] length must be greater than " + requiredLength);
        return array;
    }

    /**
     * @param array
     * @param requiredLength
     * @param arrayName
     * @return
     * @throws NullPointerException     exception
     * @throws IllegalArgumentException exception
     */
    public static char[] requiredLength(char[] array, int requiredLength, String arrayName)
            throws NullPointerException, IllegalArgumentException {
        if (array == null)
            throw new NullPointerException("Param: [" + arrayName + "] can not be null");
        if (array.length < requiredLength)
            throw new IllegalArgumentException(
                    "Param: [" + arrayName + "] length must be greater than " + requiredLength);
        return array;
    }

    /**
     * @param array
     * @param requiredLength
     * @param arrayName
     * @return
     * @throws NullPointerException     exception
     * @throws IllegalArgumentException exception
     */
    public static int[] requiredLength(int[] array, int requiredLength, String arrayName)
            throws NullPointerException, IllegalArgumentException {
        if (array == null)
            throw new NullPointerException("Param: [" + arrayName + "] can not be null");
        if (array.length < requiredLength)
            throw new IllegalArgumentException(
                    "Param: [" + arrayName + "] length must be greater than " + requiredLength);
        return array;
    }

    /**
     * @param array
     * @param requiredLength
     * @param arrayName
     * @return
     * @throws NullPointerException     exception
     * @throws IllegalArgumentException exception
     */
    public static long[] requiredLength(long[] array, int requiredLength, String arrayName)
            throws NullPointerException, IllegalArgumentException {
        if (array == null)
            throw new NullPointerException("Param: [" + arrayName + "] can not be null");
        if (array.length < requiredLength)
            throw new IllegalArgumentException(
                    "Param: [" + arrayName + "] length must be greater than " + requiredLength);
        return array;
    }

    /**
     * @param array
     * @param requiredLength
     * @param arrayName
     * @return
     * @throws NullPointerException     exception
     * @throws IllegalArgumentException exception
     */
    public static float[] requiredLength(float[] array, int requiredLength, String arrayName)
            throws NullPointerException, IllegalArgumentException {
        if (array == null)
            throw new NullPointerException("Param: [" + arrayName + "] can not be null");
        if (array.length < requiredLength)
            throw new IllegalArgumentException(
                    "Param: [" + arrayName + "] length must be greater than " + requiredLength);
        return array;
    }

    /**
     * @param array
     * @param requiredLength
     * @param arrayName
     * @return
     * @throws NullPointerException     exception
     * @throws IllegalArgumentException exception
     */
    public static double[] requiredLength(double[] array, int requiredLength, String arrayName)
            throws NullPointerException, IllegalArgumentException {
        if (array == null)
            throw new NullPointerException("Param: [" + arrayName + " can not be null");
        if (array.length < requiredLength)
            throw new IllegalArgumentException(
                    "Param: [" + arrayName + " length must be greater than " + requiredLength);
        return array;
    }

    /**
     * @param <T>
     * @param param
     * @param predicate
     * @param paramName
     * @return
     * @throws IllegalArgumentException exception
     */
    public static <T> T isValid(T param, Predicate<T> predicate, String paramName) throws IllegalArgumentException {
        return isValid(param, predicate, new IllegalArgumentException("Param: [" + paramName + "] is illegal"));
    }

    /**
     * @param <T>
     * @param <E>
     * @param param
     * @param predicate
     * @param exception
     * @return
     * @throws E exception
     */
    public static <T, E extends Exception> T isValid(T param, Predicate<T> predicate, E exception) throws E {
        if (predicate.test(param))
            return param;
        throw exception;
    }

}

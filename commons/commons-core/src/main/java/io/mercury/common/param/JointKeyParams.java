package io.mercury.common.param;

import io.mercury.common.util.BitOperator;
import org.eclipse.collections.api.map.primitive.MutableLongObjectMap;
import org.eclipse.collections.impl.map.mutable.primitive.LongObjectHashMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static io.mercury.common.datetime.DateTimeUtil.date;
import static io.mercury.common.datetime.DateTimeUtil.datetimeOfSecond;
import static io.mercury.common.datetime.DateTimeUtil.timeOfSecond;
import static io.mercury.common.datetime.DateTimeUtil.toLocalDate;
import static io.mercury.common.datetime.DateTimeUtil.toLocalDateTime;
import static io.mercury.common.datetime.DateTimeUtil.toLocalTime;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class JointKeyParams<K extends JointKey> {

    private final MutableLongObjectMap<String> param = new LongObjectHashMap<>();

    /**
     * @param key K
     * @param b   boolean
     * @return JointKeyParams<K>
     */
    public JointKeyParams<K> put(K key, boolean b) {
        param.put(mergeJointKey(key.key0(), key.key1()), Boolean.toString(b));
        return this;
    }

    /**
     * @param key K
     * @return boolean
     */
    public boolean getBoolean(K key) {
        return parseBoolean(param.get(mergeJointKey(key.key0(), key.key1())));
    }

    /**
     * @param key K
     * @param i   int
     * @return JointKeyParams<K>
     */
    public JointKeyParams<K> put(K key, int i) {
        param.put(mergeJointKey(key.key0(), key.key1()), Integer.toString(i));
        return this;
    }

    /**
     * @param key K
     * @return int
     */
    public int getInt(K key) {
        return parseInt(param.get(mergeJointKey(key.key0(), key.key1())));
    }

    /**
     * @param key K
     * @param l   long
     * @return JointKeyParams<K>
     */
    public JointKeyParams<K> put(K key, long l) {
        param.put(mergeJointKey(key.key0(), key.key1()), Long.toString(l));
        return this;
    }

    /**
     * @param key K
     * @return long
     */
    public long getLong(K key) {
        return parseLong(param.get(mergeJointKey(key.key0(), key.key1())));
    }

    /**
     * @param key K
     * @param d   double
     * @return JointKeyParams<K>
     */
    public JointKeyParams<K> put(K key, double d) {
        param.put(mergeJointKey(key.key0(), key.key1()), Double.toString(d));
        return this;
    }

    /**
     * @param key K
     * @return double
     */
    public double getDouble(K key) {
        return parseDouble(param.get(mergeJointKey(key.key0(), key.key1())));
    }

    /**
     * @param key K
     * @param str String
     * @return JointKeyParams<K>
     */
    public JointKeyParams<K> put(K key, String str) {
        param.put(mergeJointKey(key.key0(), key.key1()), str);
        return this;
    }

    /**
     * @param key K
     * @return String
     */
    public String getString(K key) {
        return param.get(mergeJointKey(key.key0(), key.key1()));
    }

    /**
     * @param key  K
     * @param date LocalDate
     * @return JointKeyParams<K>
     */
    public JointKeyParams<K> put(K key, LocalDate date) {
        put(key, date(date));
        return this;
    }

    /**
     * @param key K
     * @return LocalDate
     */
    public LocalDate getLocalDate(K key) {
        return toLocalDate(parseInt(param.get(mergeJointKey(key.key0(), key.key1()))));
    }

    /**
     * @param key  K
     * @param time LocalTime
     * @return JointKeyParams<K>
     */
    public JointKeyParams<K> put(K key, LocalTime time) {
        put(key, timeOfSecond(time));
        return this;
    }

    /**
     * @param key K
     * @return LocalTime
     */
    public LocalTime getLocalTime(K key) {
        return toLocalTime(parseInt(param.get(mergeJointKey(key.key0(), key.key1()))));
    }

    /**
     * @param key      K
     * @param datetime LocalDateTime
     * @return JointKeyParams<K>
     */
    public JointKeyParams<K> put(K key, LocalDateTime datetime) {
        put(key, datetimeOfSecond(datetime));
        return this;
    }

    /**
     * @param key K
     * @return LocalDateTime
     */
    public LocalDateTime getLocalDateTime(K key) {
        return toLocalDateTime(parseInt(param.get(mergeJointKey(key.key0(), key.key1()))));
    }

    /**
     * 将两个int value合并为long value<br>
     * long value高32位为第一个int值, 低32位为第二个int值
     *
     * @param highPos int
     * @param lowPos  int
     * @return long
     */
    public static long mergeJointKey(int highPos, int lowPos) {
        return BitOperator.merge(highPos, lowPos);
    }

    /**
     * 取出高位数值
     *
     * @param jointKey long
     * @return int
     */
    public static int getHighPos(long jointKey) {
        return BitOperator.getLongHighPos(jointKey);
    }

    /**
     * 取出低位数值
     *
     * @param jointKey long
     * @return int
     */
    public static int getLowPos(long jointKey) {
        return BitOperator.getLongLowPos(jointKey);
    }

}

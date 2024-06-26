package io.mercury.common.param;

import io.mercury.common.collections.MutableMaps;
import io.mercury.common.collections.MutableSets;
import io.mercury.common.lang.Throws;
import org.eclipse.collections.api.map.primitive.MutableIntBooleanMap;
import org.eclipse.collections.api.map.primitive.MutableIntDoubleMap;
import org.eclipse.collections.api.map.primitive.MutableIntIntMap;
import org.eclipse.collections.api.map.primitive.MutableIntLongMap;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.api.set.MutableSet;

import javax.annotation.Nonnull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public final class MutableParams<K extends ParamKey> implements Params<K> {

    private final MutableIntBooleanMap booleanParams = MutableMaps.newIntBooleanMap();

    private final MutableIntIntMap intParams = MutableMaps.newIntIntMap();

    private final MutableIntLongMap longParams = MutableMaps.newIntLongMap();

    private final MutableIntDoubleMap doubleParams = MutableMaps.newIntDoubleMap();

    private final MutableIntObjectMap<String> stringParams = MutableMaps.newIntObjectMap();

    private final MutableIntObjectMap<Temporal> temporalParams = MutableMaps.newIntObjectMap();

    private final MutableSet<K> keys = MutableSets.newUnifiedSet();

    public MutableParams() {
        this(() -> null);
    }

    /**
     * @param initializer Supplier<Map<K, ?>>
     */
    public MutableParams(@Nonnull Supplier<Map<K, ?>> initializer) {
        this(initializer.get());
    }

    /**
     * @param map Map<K, ?>
     */
    public MutableParams(Map<K, ?> map) {
        if (map != null) {
            map.forEach((K key, Object value) -> {
                switch (key.getValueType()) {
                    case BOOLEAN -> putParam(key, (boolean) value);
                    case INT -> putParam(key, (int) value);
                    case LONG -> putParam(key, (long) value);
                    case DOUBLE -> putParam(key, (double) value);
                    case STRING -> putParam(key, (String) value);
                    case DATE -> putParam(key, (LocalDate) value);
                    case TIME -> putParam(key, (LocalTime) value);
                    case DATETIME -> putParam(key, (LocalDateTime) value);
                    case ZONED_DATETIME -> putParam(key, (ZonedDateTime) value);
                    default -> Throws.illegalArgument("param: " + key.getParamName());
                }
            });
        }
    }

    /**
     * @param key K
     * @return boolean
     */
    @Override
    public boolean getBoolean(K key) {
        if (key.getValueType() != ValueType.BOOLEAN)
            Throws.illegalArgument(
                    "Key -> " + key + " paramType is not BOOLEAN, paramType==" + key.getValueType());
        return booleanParams.get(key.getParamId());
    }

    /**
     * @param key K
     * @return int
     */
    @Override
    public int getInt(K key) {
        if (key.getValueType() != ValueType.INT)
            Throws.illegalArgument(
                    "Key -> " + key + " paramType is not INT, paramType==" + key.getValueType());
        return intParams.get(key.getParamId());
    }

    /**
     * @param key K
     * @return long
     */
    @Override
    public long getLong(K key) {
        if (key.getValueType() != ValueType.LONG)
            Throws.illegalArgument(
                    "Key -> " + key + " paramType is not LONG, paramType==" + key.getValueType());
        return longParams.get(key.getParamId());
    }

    /**
     * @param key K
     * @return double
     */
    @Override
    public double getDouble(K key) {
        if (key.getValueType() != ValueType.DOUBLE)
            Throws.illegalArgument(
                    "Key -> " + key + " paramType is not DOUBLE, paramType==" + key.getValueType());
        return doubleParams.get(key.getParamId());
    }

    /**
     * @param key K
     * @return String
     */
    @Override
    public String getString(K key) {
        if (key.getValueType() != ValueType.STRING)
            Throws.illegalArgument(
                    "Key -> " + key + " paramType is not STRING, paramType==" + key.getValueType());
        return stringParams.get(key.getParamId());
    }

    /**
     * @param key K
     * @return LocalDate
     */
    @Override
    public LocalDate getDate(K key) {
        if (key.getValueType() != ValueType.DATE)
            Throws.illegalArgument(
                    "Key -> " + key + " paramType is not DATE, paramType==" + key.getValueType());
        return (LocalDate) temporalParams.get(key.getParamId());
    }

    /**
     * @param key K
     * @return LocalTime
     */
    @Override
    public LocalTime getTime(K key) {
        if (key.getValueType() != ValueType.TIME)
            Throws.illegalArgument(
                    "Key -> " + key + " paramType is not TIME, getParamType==" + key.getValueType());
        return (LocalTime) temporalParams.get(key.getParamId());
    }

    /**
     * @param key K
     * @return LocalDateTime
     */
    @Override
    public LocalDateTime getDateTime(K key) {
        if (key.getValueType() != ValueType.DATETIME)
            Throws.illegalArgument(
                    "Key -> [" + key + "] paramType is not DATETIME, paramType==" + key.getValueType());
        return (LocalDateTime) temporalParams.get(key.getParamId());
    }

    /**
     * @param key K
     * @return ZonedDateTime
     */
    @Override
    public ZonedDateTime getZonedDateTime(K key) {
        if (key.getValueType() != ValueType.ZONED_DATETIME)
            Throws.illegalArgument(
                    "Key -> [" + key + "] paramType is not ZONED_DATETIME, paramType==" + key.getValueType());
        return (ZonedDateTime) temporalParams.get(key.getParamId());
    }

    /**
     * @param key   K
     * @param value boolean
     */
    public void putParam(K key, boolean value) {
        keys.add(key);
        booleanParams.put(key.getParamId(), value);
    }

    /**
     * @param key   K
     * @param value int
     */
    public void putParam(K key, int value) {
        keys.add(key);
        intParams.put(key.getParamId(), value);
    }

    /**
     * @param key   K
     * @param value long
     */
    public void putParam(K key, long value) {
        keys.add(key);
        doubleParams.put(key.getParamId(), value);
    }

    /**
     * @param key   K
     * @param value double
     */
    public void putParam(K key, double value) {
        keys.add(key);
        doubleParams.put(key.getParamId(), value);
    }

    /**
     * @param key   K
     * @param value String
     */
    public void putParam(K key, String value) {
        keys.add(key);
        stringParams.put(key.getParamId(), value);
    }

    /**
     * @param key   K
     * @param value LocalDate
     */
    public void putParam(K key, LocalDate value) {
        keys.add(key);
        temporalParams.put(key.getParamId(), value);
    }

    /**
     * @param key   K
     * @param value LocalTime
     */
    public void putParam(K key, LocalTime value) {
        keys.add(key);
        temporalParams.put(key.getParamId(), value);
    }

    /**
     * @param key   K
     * @param value LocalDateTime
     */
    public void putParam(K key, LocalDateTime value) {
        keys.add(key);
        temporalParams.put(key.getParamId(), value);
    }

    /**
     * @param key   K
     * @param value ZonedDateTime
     */
    public void putParam(K key, ZonedDateTime value) {
        keys.add(key);
        temporalParams.put(key.getParamId(), value);
    }

    @Override
    public Set<K> getParamKeys() {
        return keys;
    }

}

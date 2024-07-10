package io.mercury.common.param;

import io.mercury.common.collections.MutableMaps;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.set.ImmutableSet;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static io.mercury.common.collections.ImmutableSets.newImmutableSet;
import static io.mercury.common.lang.Asserter.nonEmptyMap;
import static io.mercury.common.lang.Asserter.nonNull;
import static io.mercury.common.lang.Asserter.requiredLength;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class ImmutableParams<K extends ParamKey> implements Params<K> {

    private final ImmutableMap<K, String> params;

    private final ImmutableSet<K> keys;

    /**
     * 根据传入的Key获取Map中的相应字段
     *
     * @param map  Map<?, ?>
     * @param keys K[]
     */
    public ImmutableParams(@Nonnull Map<String, ?> map, @Nonnull K[] keys) {
        nonEmptyMap(map, "map");
        requiredLength(keys, 1, "keys");
        MutableMap<K, String> mutableMap = MutableMaps.newUnifiedMap();
        for (K key : keys) {
            if (map.containsKey(key.getParamName()))
                mutableMap.put(key, map.get(key.getParamName()).toString());
        }
        this.params = mutableMap.toImmutable();
        this.keys = newImmutableSet(keys);
    }

    /**
     * 根据传入的Key获取Properties中的相应字段
     *
     * @param prop Properties
     * @param keys K[]
     */
    public ImmutableParams(@Nonnull Properties prop, @Nonnull K[] keys) {
        requiredLength(keys, 1, "keys");
        nonNull(prop, "prop");
        var map = MutableMaps.<K, String>newUnifiedMap();
        for (K key : keys) {
            if (prop.containsKey(key.getParamName()))
                map.put(key, prop.get(key.getParamName()).toString());
        }
        this.params = map.toImmutable();
        this.keys = newImmutableSet(keys);
    }

    /**
     * @param key K
     * @return boolean
     * @throws IllegalArgumentException e
     * @throws NullPointerException     e
     */
    @Override
    public boolean getBoolean(K key) throws IllegalArgumentException, NullPointerException {
        if (key.getValueType() != ValueType.BOOLEAN)
            throw new IllegalArgumentException(
                    "Key -> [" + key + "], ValueType is not BOOLEAN, valueType==" + key.getValueType());
        return parseBoolean(nonNull(params.get(key), key.getParamName()));
    }

    /**
     * @param key K
     * @return int
     * @throws IllegalArgumentException e
     * @throws NullPointerException     e
     * @throws NumberFormatException    e
     */
    @Override
    public int getInt(K key) throws IllegalArgumentException, NullPointerException, NumberFormatException {
        if (key.getValueType() != ValueType.INT)
            throw new IllegalArgumentException(
                    "Key -> [" + key + "], ValueType is not [INT]. valueType==" + key.getValueType());
        return parseInt(nonNull(params.get(key), key.getParamName()));
    }

    /**
     * @param key K
     * @return long
     * @throws IllegalArgumentException e
     * @throws NullPointerException     e
     * @throws NumberFormatException    e
     */
    @Override
    public long getLong(K key) throws IllegalArgumentException, NullPointerException, NumberFormatException {
        if (key.getValueType() != ValueType.LONG)
            throw new IllegalArgumentException(
                    "Key -> [" + key + "], ValueType is not [LONG]. valueType==" + key.getValueType());
        return parseLong(nonNull(params.get(key), key.getParamName()));
    }

    /**
     * @param key K
     * @return double
     * @throws IllegalArgumentException e
     * @throws NullPointerException     e
     * @throws NumberFormatException    e
     */
    @Override
    public double getDouble(K key) throws IllegalArgumentException, NullPointerException, NumberFormatException {
        if (key.getValueType() != ValueType.DOUBLE)
            throw new IllegalArgumentException(
                    "Key -> [" + key + "], ValueType is not [DOUBLE], valueType==" + key.getValueType());
        return parseDouble(nonNull(params.get(key), key.getParamName()));
    }

    /**
     * @param key K
     * @return String
     * @throws IllegalArgumentException e
     * @throws NullPointerException     e
     */
    @Override
    public String getString(K key) throws IllegalArgumentException, NullPointerException {
        if (key.getValueType() != ValueType.STRING)
            throw new IllegalArgumentException(
                    "Key -> [" + key + "] ValueType is not [STRING], paramType==" + key.getValueType());
        return nonNull(params.get(key), key.getParamName());
    }

    @Override
    public LocalDate getDate(K key) {
        throw new UnsupportedOperationException("getDate not overloaded");
    }

    @Override
    public LocalTime getTime(K key) {
        throw new UnsupportedOperationException("getTime not overloaded");
    }

    @Override
    public LocalDateTime getDateTime(K key) {
        throw new UnsupportedOperationException("getDateTime not overloaded");
    }

    @Override
    public ZonedDateTime getZonedDateTime(K key) {
        throw new UnsupportedOperationException("getZonedDateTime not overloaded");
    }

    @Override
    public Set<K> getParamKeys() {
        return keys.toSet();
    }

    @Override
    public void printParams(Logger log) {
        if (log == null)
            params.forEachKeyValue((key, value) -> System.out.println(
                    "Param ID==" + key.getParamId() + ", ParamName -> " + key.getParamName() + ", Value -> " + value));
        else
            params.forEachKeyValue((key, value) -> log.info("Param ID=={}, ParamName=={}, Value -> {}",
                    key.getParamId(), key.getParamName(), value));
    }

}

package io.mercury.common.param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.map.primitive.MutableIntBooleanMap;
import org.eclipse.collections.api.map.primitive.MutableIntDoubleMap;
import org.eclipse.collections.api.map.primitive.MutableIntIntMap;
import org.eclipse.collections.api.map.primitive.MutableIntLongMap;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.api.set.MutableSet;

import io.mercury.common.collections.MutableMaps;
import io.mercury.common.collections.MutableSets;

public final class MutableParams<K extends ParamKey> implements Params<K> {

	private final MutableIntBooleanMap booleanParams = MutableMaps.newIntBooleanHashMap();
	private final MutableIntIntMap intParams = MutableMaps.newIntIntHashMap();
	private final MutableIntLongMap longParams = MutableMaps.newIntLongHashMap();
	private final MutableIntDoubleMap doubleParams = MutableMaps.newIntDoubleHashMap();
	private final MutableIntObjectMap<String> stringParams = MutableMaps.newIntObjectHashMap();
	private final MutableIntObjectMap<Temporal> temporalParams = MutableMaps.newIntObjectHashMap();

	private final MutableSet<K> keys = MutableSets.newUnifiedSet();

	public MutableParams() {
		this(() -> null);
	}

	/**
	 * 
	 * @param initializer
	 */
	public MutableParams(@Nonnull Supplier<Map<K, ?>> initializer) {
		this(initializer.get());
	}

	/**
	 * 
	 * @param map
	 */
	public MutableParams(Map<K, ?> map) {
		if (map != null) {
			map.forEach((K key, Object value) -> {
				switch (key.getValueType()) {
				case BOOLEAN:
					putParam(key, (boolean) value);
					break;
				case INT:
					putParam(key, (int) value);
					break;
				case LONG:
					putParam(key, (long) value);
					break;
				case DOUBLE:
					putParam(key, (double) value);
					break;
				case STRING:
					putParam(key, (String) value);
					break;
				case DATE:
					putParam(key, (LocalDate) value);
					break;
				case TIME:
					putParam(key, (LocalTime) value);
					break;
				case DATETIME:
					putParam(key, (LocalDateTime) value);
					break;
				case ZONED_DATETIME:
					putParam(key, (ZonedDateTime) value);
					break;
				default:
					throw new IllegalArgumentException("param name -> " + key.getParamName() + " illegal argument");
				}
			});
		}
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public boolean getBoolean(K key) {
		if (key.getValueType() != ValueType.BOOLEAN)
			throw new IllegalArgumentException(
					"Key -> " + key + " paramType is not BOOLEAN, paramType==" + key.getValueType());
		return booleanParams.get(key.getParamId());
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public int getInt(K key) {
		if (key.getValueType() != ValueType.INT)
			throw new IllegalArgumentException(
					"Key -> " + key + " paramType is not INT, paramType==" + key.getValueType());
		return intParams.get(key.getParamId());
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public long getLong(K key) {
		if (key.getValueType() != ValueType.LONG)
			throw new IllegalArgumentException(
					"Key -> " + key + " paramType is not LONG, paramType==" + key.getValueType());
		return longParams.get(key.getParamId());
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public double getDouble(K key) {
		if (key.getValueType() != ValueType.DOUBLE)
			throw new IllegalArgumentException(
					"Key -> " + key + " paramType is not DOUBLE, paramType==" + key.getValueType());
		return doubleParams.get(key.getParamId());
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public String getString(K key) {
		if (key.getValueType() != ValueType.STRING)
			throw new IllegalArgumentException(
					"Key -> " + key + " paramType is not STRING, paramType==" + key.getValueType());
		return stringParams.get(key.getParamId());
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public LocalDate getDate(K key) {
		if (key.getValueType() != ValueType.DATE)
			throw new IllegalArgumentException(
					"Key -> " + key + " paramType is not DATE, paramType==" + key.getValueType());
		return (LocalDate) temporalParams.get(key.getParamId());
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public LocalTime getTime(K key) {
		if (key.getValueType() != ValueType.TIME)
			throw new IllegalArgumentException(
					"Key -> " + key + " paramType is not TIME, getParamType==" + key.getValueType());
		return (LocalTime) temporalParams.get(key.getParamId());
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public LocalDateTime getDateTime(K key) {
		if (key.getValueType() != ValueType.DATETIME)
			throw new IllegalArgumentException(
					"Key -> " + key + " paramType is not DATETIME, paramType==" + key.getValueType());
		return (LocalDateTime) temporalParams.get(key.getParamId());
	}

	@Override
	public ZonedDateTime getZonedDateTime(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putParam(K key, boolean value) {
		keys.add(key);
		booleanParams.put(key.getParamId(), value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putParam(K key, int value) {
		keys.add(key);
		intParams.put(key.getParamId(), value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putParam(K key, long value) {
		keys.add(key);
		doubleParams.put(key.getParamId(), value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putParam(K key, double value) {
		keys.add(key);
		doubleParams.put(key.getParamId(), value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putParam(K key, String value) {
		keys.add(key);
		stringParams.put(key.getParamId(), value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putParam(K key, LocalDate value) {
		keys.add(key);
		temporalParams.put(key.getParamId(), value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putParam(K key, LocalTime value) {
		keys.add(key);
		temporalParams.put(key.getParamId(), value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putParam(K key, LocalDateTime value) {
		keys.add(key);
		temporalParams.put(key.getParamId(), value);
	}

	/**
	 * 
	 * @param key
	 * @param value
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

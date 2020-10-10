package io.mercury.common.param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.map.primitive.MutableIntBooleanMap;
import org.eclipse.collections.api.map.primitive.MutableIntDoubleMap;
import org.eclipse.collections.api.map.primitive.MutableIntIntMap;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntBooleanHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntDoubleHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntIntHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

public final class MutableParams<K extends ParamKey> {

	private MutableIntBooleanMap booleanParams = new IntBooleanHashMap();
	private MutableIntIntMap intParams = new IntIntHashMap();
	private MutableIntDoubleMap doubleParams = new IntDoubleHashMap();
	private MutableIntObjectMap<String> stringParams = new IntObjectHashMap<>();
	private MutableIntObjectMap<Temporal> temporalParams = new IntObjectHashMap<>();

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
	 * @param initMap
	 */
	public MutableParams(Map<K, ?> initMap) {
		if (initMap != null) {
			initMap.forEach((K key, Object value) -> {
				switch (key.type()) {
				case BOOLEAN:
					putParam(key, (boolean) value);
					break;
				case INT:
					putParam(key, (int) value);
					break;
				case DOUBLE:
					putParam(key, (double) value);
					break;
				case STRING:
					putParam(key, (String) value);
					break;
				case DATETIME:
					putParam(key, (LocalDateTime) value);
					break;
				case DATE:
					putParam(key, (LocalDate) value);
					break;
				case TIME:
					putParam(key, (LocalTime) value);
					break;
				default:
					throw new IllegalArgumentException("param name -> " + key.keyName() + " illegal argument");
				}
			});
		}
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public boolean getBoolean(K key) {
		if (key.type() != ParamType.BOOLEAN)
			throw new IllegalArgumentException(
					"Key -> " + key + " paramType is not BOOLEAN, paramType()==" + key.type());
		return booleanParams.get(key.id());
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public int getInt(K key) {
		if (key.type() != ParamType.INT)
			throw new IllegalArgumentException("Key -> " + key + " paramType is not INT, paramType()==" + key.type());
		return intParams.get(key.id());
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public double getDouble(K key) {
		if (key.type() != ParamType.DOUBLE)
			throw new IllegalArgumentException(
					"Key -> " + key + " paramType is not DOUBLE, paramType()==" + key.type());
		return doubleParams.get(key.id());
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getString(K key) {
		if (key.type() != ParamType.STRING)
			throw new IllegalArgumentException(
					"Key -> " + key + " paramType is not STRING, paramType()==" + key.type());
		return stringParams.get(key.id());
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public LocalDateTime getDateTime(K key) {
		if (key.type() != ParamType.DATETIME)
			throw new IllegalArgumentException(
					"Key -> " + key + " paramType is not DATETIME, paramType()==" + key.type());
		return (LocalDateTime) temporalParams.get(key.id());
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public LocalDate getDate(K key) {
		if (key.type() != ParamType.DATE)
			throw new IllegalArgumentException("Key -> " + key + " paramType is not DATE, paramType()==" + key.type());
		return (LocalDate) temporalParams.get(key.id());
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public LocalTime getTime(K key) {
		if (key.type() != ParamType.TIME)
			throw new IllegalArgumentException(
					"Key -> " + key + " paramType is not TIME, getParamType()==" + key.type());
		return (LocalTime) temporalParams.get(key.id());
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putParam(K key, boolean value) {
		booleanParams.put(key.id(), value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putParam(K key, int value) {
		intParams.put(key.id(), value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putParam(K key, double value) {
		doubleParams.put(key.id(), value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putParam(K key, String value) {
		stringParams.put(key.id(), value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putParam(K key, LocalDateTime value) {
		temporalParams.put(key.id(), value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putParam(K key, LocalDate value) {
		temporalParams.put(key.id(), value);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putParam(K key, LocalTime value) {
		temporalParams.put(key.id(), value);
	}

}

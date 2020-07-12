package io.mercury.common.param.map;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.collections.api.map.primitive.MutableIntBooleanMap;
import org.eclipse.collections.api.map.primitive.MutableIntDoubleMap;
import org.eclipse.collections.api.map.primitive.MutableIntIntMap;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntBooleanHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntDoubleHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntIntHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

import io.mercury.common.param.ParamKey;
import io.mercury.common.param.ParamType;

public final class MutableParamMap<K extends ParamKey> {

	private MutableIntBooleanMap booleanParamMap = new IntBooleanHashMap();
	private MutableIntIntMap intParamMap = new IntIntHashMap();
	private MutableIntDoubleMap doubleParamMap = new IntDoubleHashMap();
	private MutableIntObjectMap<String> stringParamMap = new IntObjectHashMap<>();
	private MutableIntObjectMap<Temporal> temporalParamMap = new IntObjectHashMap<>();

	public MutableParamMap(Supplier<Map<K, Object>> initializer) {
		Map<K, Object> initMap = initializer.get();
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
				throw new IllegalArgumentException("paramId -> " + key.id() + " illegal argument");
			}
		});
	}

	public boolean getBoolean(K key) {
		if (key.type() != ParamType.BOOLEAN)
			throw new IllegalArgumentException(
					"Key -> " + key + " paramType is not BOOLEAN. paramType()==" + key.type());
		return booleanParamMap.get(key.id());
	}

	public int getInt(K key) {
		if (key.type() != ParamType.INT)
			throw new IllegalArgumentException(
					"Key -> " + key + " paramType is not INT. paramType()==" + key.type());
		return intParamMap.get(key.id());
	}

	public double getDouble(K key) {
		if (key.type() != ParamType.DOUBLE)
			throw new IllegalArgumentException(
					"Key -> " + key + " paramType is not DOUBLE. paramType()==" + key.type());
		return doubleParamMap.get(key.id());
	}

	public String getString(K key) {
		if (key.type() != ParamType.STRING)
			throw new IllegalArgumentException(
					"Key -> " + key + " paramType is not STRING. paramType()==" + key.type());
		return stringParamMap.get(key.id());
	}

	public LocalDateTime getDateTime(K key) {
		if (key.type() != ParamType.DATETIME)
			throw new IllegalArgumentException(
					"Key -> " + key + " paramType is not DATETIME. paramType()==" + key.type());
		return (LocalDateTime) temporalParamMap.get(key.id());
	}

	public LocalDate getDate(K key) {
		if (key.type() != ParamType.DATE)
			throw new IllegalArgumentException(
					"Key -> " + key + " paramType is not DATE. paramType()==" + key.type());
		return (LocalDate) temporalParamMap.get(key.id());
	}

	public LocalTime getTime(K key) {
		if (key.type() != ParamType.TIME)
			throw new IllegalArgumentException(
					"Key -> " + key + " paramType is not TIME. getParamType()==" + key.type());
		return (LocalTime) temporalParamMap.get(key.id());
	}

	private void putParam(K key, boolean value) {
		booleanParamMap.put(key.id(), value);
	}

	private void putParam(K key, int value) {
		intParamMap.put(key.id(), value);
	}

	private void putParam(K key, double value) {
		doubleParamMap.put(key.id(), value);
	}

	private void putParam(K key, String value) {
		stringParamMap.put(key.id(), value);
	}

	private void putParam(K key, LocalDateTime value) {
		temporalParamMap.put(key.id(), value);
	}

	private void putParam(K key, LocalDate value) {
		temporalParamMap.put(key.id(), value);
	}

	private void putParam(K key, LocalTime value) {
		temporalParamMap.put(key.id(), value);
	}

}

package io.mercury.common.param;

import java.util.Set;

import org.slf4j.Logger;

import io.mercury.common.param.Params.ParamKey;

public interface Params<K extends ParamKey> {

	boolean getBoolean(K key);

	int getInt(K key);

	long getLong(K key);

	double getDouble(K key);

	String getString(K key);

	Set<K> getParamKeys();

	default void printParams() {
		printParams(null);
	}

	default void printParams(Logger log) {
		throw new UnsupportedOperationException("function -> printParams(log) is not implements");
	}

	/**
	 * 
	 * @author yellow013
	 *
	 */
	public static interface ParamKey {

		int getParamId();

		String getParamName();

		ValueType getValueType();

	}

	/**
	 * 
	 * @author yellow013
	 *
	 */
	public static enum ValueType {

		STRING,

		BOOLEAN,

		DOUBLE,

		INT,

		LONG,

		DATE,

		TIME,

		DATETIME

	}

}

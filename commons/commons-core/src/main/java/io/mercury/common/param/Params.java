package io.mercury.common.param;

import io.mercury.common.param.Params.ParamKey;

public interface Params<K extends ParamKey> {

	boolean getBoolean(K key);

	int getInt(K key);

	double getDouble(K key);

	String getString(K key);

	/**
	 * 
	 * @author yellow013
	 *
	 */
	public static interface ParamKey {

		int getId();

		String getParamName();

		ParamType getType();

	}

	/**
	 * 
	 * @author yellow013
	 *
	 */
	public static enum ParamType {

		STRING,

		BOOLEAN,

		DOUBLE,

		INT,

		DATE,

		TIME,

		DATETIME

	}

}

package io.mercury.common.param;

public interface Params<K extends ParamKey> {

	boolean getBoolean(K key);

	int getInt(K key);

	double getDouble(K key);

	String getString(K key);

}

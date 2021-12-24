package io.mercury.common.param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Set;

import org.slf4j.Logger;

public interface Params<K extends ParamKey> {

	boolean getBoolean(K key);

	int getInt(K key);

	long getLong(K key);

	double getDouble(K key);

	String getString(K key);

	LocalDate getDate(K key);

	LocalTime getTime(K key);

	LocalDateTime getDateTime(K key);

	ZonedDateTime getZonedDateTime(K key);

	Set<K> getParamKeys();

	default void printParams() {
		printParams(null);
	}

	default void printParams(Logger log) {
		var keys = getParamKeys();
		for (var key : keys) {
			switch (key.getValueType()) {
			case STRING:
				if (log != null)
					log.info("Key:{} -> Value:{}", key, getString(key));
				else
					System.out.println("Key:" + key + " -> Value:" + getString(key));
				break;
			case BOOLEAN:
				if (log != null)
					log.info("Key:{} -> Value:{}", key, getBoolean(key));
				else
					System.out.println("Key:" + key + " -> Value:" + getBoolean(key));
				break;
			case DOUBLE:
				if (log != null)
					log.info("Key:{} -> Value:{}", key, getDouble(key));
				else
					System.out.println("Key:" + key + " -> Value:" + getDouble(key));
				break;
			case INT:
				if (log != null)
					log.info("Key:{} -> Value:{}", key, getInt(key));
				else
					System.out.println("Key:" + key + " -> Value:" + getInt(key));
				break;
			case LONG:
				if (log != null)
					log.info("Key:{} -> Value:{}", key, getLong(key));
				else
					System.out.println("Key:" + key + " -> Value:" + getLong(key));
				break;
			case DATE:
				if (log != null)
					log.info("Key:{} -> Value:{}", key, getDate(key));
				else
					System.out.println("Key:" + key + " -> Value:" + getDate(key));
				break;
			case TIME:
				if (log != null)
					log.info("Key:{} -> Value:{}", key, getTime(key));
				else
					System.out.println("Key:" + key + " -> Value:" + getTime(key));
				break;
			case DATETIME:
				if (log != null)
					log.info("Key:{} -> Value:{}", key, getDateTime(key));
				else
					System.out.println("Key:" + key + " -> Value:" + getDateTime(key));
				break;
			case ZONED_DATETIME:
				if (log != null)
					log.info("Key:{} -> Value:{}", key, getZonedDateTime(key));
				else
					System.out.println("Key:" + key + " -> Value:" + getZonedDateTime(key));
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 
	 * @author yellow013
	 */
	public static enum ValueType {

		STRING,

		BOOLEAN,

		DOUBLE,

		INT,

		LONG,

		DATE,

		TIME,

		DATETIME,

		ZONED_DATETIME

	}

}

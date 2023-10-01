package io.mercury.common.param;

import org.slf4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Set;

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
        Set<K> keys = getParamKeys();
        if (log != null)
            for (K key : keys) {
                switch (key.getValueType()) {
                    case STRING -> log.info("Key:{} -> Value:{}", key, getString(key));
                    case BOOLEAN -> log.info("Key:{} -> Value:{}", key, getBoolean(key));
                    case DOUBLE -> log.info("Key:{} -> Value:{}", key, getDouble(key));
                    case INT -> log.info("Key:{} -> Value:{}", key, getInt(key));
                    case LONG -> log.info("Key:{} -> Value:{}", key, getLong(key));
                    case DATE -> log.info("Key:{} -> Value:{}", key, getDate(key));
                    case TIME -> log.info("Key:{} -> Value:{}", key, getTime(key));
                    case DATETIME -> log.info("Key:{} -> Value:{}", key, getDateTime(key));
                    case ZONED_DATETIME -> log.info("Key:{} -> Value:{}", key, getZonedDateTime(key));
                    default -> {
                    }
                }
            }
        else
            for (K key : keys) {
                switch (key.getValueType()) {
                    case STRING -> System.out.println("Key:" + key + " -> Value:" + getString(key));
                    case BOOLEAN -> System.out.println("Key:" + key + " -> Value:" + getBoolean(key));
                    case DOUBLE -> System.out.println("Key:" + key + " -> Value:" + getDouble(key));
                    case INT -> System.out.println("Key:" + key + " -> Value:" + getInt(key));
                    case LONG -> System.out.println("Key:" + key + " -> Value:" + getLong(key));
                    case DATE -> System.out.println("Key:" + key + " -> Value:" + getDate(key));
                    case TIME -> System.out.println("Key:" + key + " -> Value:" + getTime(key));
                    case DATETIME -> System.out.println("Key:" + key + " -> Value:" + getDateTime(key));
                    case ZONED_DATETIME -> System.out.println("Key:" + key + " -> Value:" + getZonedDateTime(key));
                    default -> {
                    }
                }
            }
    }

    /**
     * @author yellow013
     */
    enum ValueType {

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

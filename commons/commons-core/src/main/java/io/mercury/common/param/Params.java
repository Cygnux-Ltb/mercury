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
        for (K key : keys) {
            Object value = switch (key.getValueType()) {
                case STRING -> getString(key);
                case BOOLEAN -> getBoolean(key);
                case DOUBLE -> getDouble(key);
                case INT -> getInt(key);
                case LONG -> getLong(key);
                case DATE -> getDate(key);
                case TIME -> getTime(key);
                case DATETIME -> getDateTime(key);
                case ZONED_DATETIME -> getZonedDateTime(key);
            };
            if (log != null)
                log.info("Key:{} -> Value:{}", key, value);
            else
                System.out.println("Key:" + key + " -> Value:" + value);
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

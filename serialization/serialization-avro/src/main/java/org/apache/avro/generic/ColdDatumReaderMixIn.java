package org.apache.avro.generic;

import com.linkedin.avro.fastserde.coldstart.ColdPrimitiveBooleanList;
import com.linkedin.avro.fastserde.coldstart.ColdPrimitiveDoubleList;
import com.linkedin.avro.fastserde.coldstart.ColdPrimitiveFloatList;
import com.linkedin.avro.fastserde.coldstart.ColdPrimitiveIntList;
import com.linkedin.avro.fastserde.coldstart.ColdPrimitiveLongList;
import org.apache.avro.Schema;

import java.util.Collection;

/**
 * An interface with default implementation in order to defeat the lack of
 * multiple inheritance.
 */
public interface ColdDatumReaderMixIn {

    default Object newArray(Object old, int size, Schema schema, NewArrayFunction fallBackFunction) {
        switch (schema.getElementType().getType()) {
            case BOOLEAN -> {
                if (!(old instanceof ColdPrimitiveBooleanList)) {
                    old = new ColdPrimitiveBooleanList(size);
                }
            }
            case DOUBLE -> {
                if (!(old instanceof ColdPrimitiveDoubleList)) {
                    old = new ColdPrimitiveDoubleList(size);
                }
            }
            case FLOAT -> {
                if (!(old instanceof ColdPrimitiveFloatList)) {
                    old = new ColdPrimitiveFloatList(size);
                }
            }
            case INT -> {
                if (!(old instanceof ColdPrimitiveIntList)) {
                    old = new ColdPrimitiveIntList(size);
                }
            }
            case LONG -> {
                if (!(old instanceof ColdPrimitiveLongList)) {
                    old = new ColdPrimitiveLongList(size);
                }
            }
            default -> {
                return fallBackFunction.newArray(old, size, schema);
            }
        }
        ((Collection<?>) old).clear();
        return old;
    }

    interface NewArrayFunction {
        Object newArray(Object old, int size, Schema schema);
    }
    
}

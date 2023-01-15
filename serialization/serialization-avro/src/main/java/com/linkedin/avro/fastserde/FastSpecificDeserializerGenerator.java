package com.linkedin.avro.fastserde;

import org.apache.avro.Schema;

import java.io.File;

public final class FastSpecificDeserializerGenerator<T> extends FastDeserializerGenerator<T> {

    FastSpecificDeserializerGenerator(Schema writer, Schema reader, File destination, ClassLoader classLoader,
                                      String compileClassPath) {
        super(false, writer, reader, destination, classLoader, compileClassPath);
    }

}

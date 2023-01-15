package com.linkedin.avro.fastserde;

import org.apache.avro.Schema;

import java.io.File;

public final class FastGenericDeserializerGenerator<T> extends FastDeserializerGenerator<T> {

    FastGenericDeserializerGenerator(Schema writer, Schema reader, File destination, ClassLoader classLoader,
                                     String compileClassPath) {
        super(true, writer, reader, destination, classLoader, compileClassPath);
    }

}

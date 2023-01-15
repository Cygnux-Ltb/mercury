package com.linkedin.avro.fastserde;

import org.apache.avro.Schema;

import java.io.File;

public class FastSpecificSerializerGenerator<T> extends FastSerializerGenerator<T> {

    public FastSpecificSerializerGenerator(Schema schema, File destination, ClassLoader classLoader,
                                           String compileClassPath) {
        super(false, schema, destination, classLoader, compileClassPath);
    }
}

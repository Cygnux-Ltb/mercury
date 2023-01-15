package com.linkedin.avro.fastserde;

import org.apache.avro.Schema;

import java.io.File;

public class FastGenericSerializerGenerator<T> extends FastSerializerGenerator<T> {

    public FastGenericSerializerGenerator(Schema schema, File destination, ClassLoader classLoader,
                                          String compileClassPath) {
        super(true, schema, destination, classLoader, compileClassPath);
    }

}

package com.linkedin.avro.fastserde;

import org.apache.avro.io.Decoder;

import java.io.IOException;

public interface FastDeserializer<T> {

    default T deserialize(Decoder d) throws IOException {
        return deserialize(null, d);
    }

    T deserialize(T reuse, Decoder d) throws IOException;

}

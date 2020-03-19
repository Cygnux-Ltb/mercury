package io.mercury.codec.avro;

import java.io.IOException;
import java.util.List;

import javax.annotation.concurrent.NotThreadSafe;

import org.apache.avro.AvroRuntimeException;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.JsonDecoder;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecord;
import org.slf4j.Logger;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.serialization.specific.TextDeserializer;

@NotThreadSafe
public final class AvroTextDeserializer<T extends SpecificRecord> implements TextDeserializer<T> {

    private static final Logger log = CommonLoggerFactory.getLogger(AvroTextDeserializer.class);

    private JsonDecoder decoder;

    private SpecificDatumReader<T> datumReader;

    public AvroTextDeserializer(Class<T> tClass) {
	this.datumReader = new SpecificDatumReader<>(tClass);
	this.decoder = initDecoder();
    }

    private JsonDecoder initDecoder() {
	try {
	    return DecoderFactory.get().jsonDecoder(datumReader.getSchema(), "");
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    public T deserialization(T reuse, String str) {
	try {
	    decoder.configure(str);
	    return datumReader.read(reuse, decoder);
	} catch (IOException e) {
	    log.error(e.getMessage(), e);
	    throw new RuntimeException("AvroTextDeserializer.deserialization(str) -> " + e.getMessage());
	}
    }

    public List<T> deserializationMultiple(String source) {
	throw new AvroRuntimeException("deserializationMultiple() -> " + source);
    }

}

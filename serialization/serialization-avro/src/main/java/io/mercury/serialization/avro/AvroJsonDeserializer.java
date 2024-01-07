package io.mercury.serialization.avro;

import io.mercury.common.log4j2.Log4j2LoggerFactory;
import io.mercury.common.serialization.specific.JsonDeserializer;
import org.apache.avro.AvroRuntimeException;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.JsonDecoder;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecord;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import java.io.IOException;
import java.util.List;

@NotThreadSafe
public final class AvroJsonDeserializer<T extends SpecificRecord> implements JsonDeserializer<T> {

    private static final Logger log = Log4j2LoggerFactory.getLogger(AvroJsonDeserializer.class);

    private final SpecificDatumReader<T> datumReader;

    private final JsonDecoder decoder;

    public AvroJsonDeserializer(Class<T> tClass) {
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

    @Nonnull
    @Override
    public T deserialization(@Nonnull String source, T reuse) {
        try {
            decoder.configure(source);
            return datumReader.read(reuse, decoder);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(STR."AvroTextDeserializer::deserialization(source) -> \{e.getMessage()}");
        }
    }

    public List<T> deserializationMultiple(String source) {
        throw new AvroRuntimeException(STR."deserializationMultiple() -> \{source}");
    }

}

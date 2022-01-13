package io.mercury.serialization.avro;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecord;
import org.apache.avro.util.ReusableByteArrayInputStream;
import org.slf4j.Logger;

import io.mercury.common.annotation.thread.ThreadSafeVariable;
import io.mercury.common.log.Log4j2LoggerFactory;
import io.mercury.common.serialization.BytesDeserializer;

@NotThreadSafe
public final class AvroBinaryDeserializer<T extends SpecificRecord> implements BytesDeserializer<T> {

	private static final Logger log = Log4j2LoggerFactory.getLogger(AvroBinaryDeserializer.class);

	private final Class<T> type;

	@ThreadSafeVariable
	private final DatumReader<T> reader;

	private final ReusableByteArrayInputStream inputStream;

	private BinaryDecoder decoder;

	/**
	 * 
	 * @param type
	 */
	public AvroBinaryDeserializer(Class<T> type) {
		this.type = type;
		this.reader = new SpecificDatumReader<>(type);
		this.inputStream = new ReusableByteArrayInputStream();
	}

	@Override
	public T deserialization(@Nonnull byte[] data, @Nullable T reuse) {
		try {
			inputStream.setByteArray(data, 0, data.length);
			decoder = DecoderFactory.get().binaryDecoder(inputStream, decoder);
			reuse = reader.read(reuse, decoder);
			return reuse;
		} catch (Exception e) {
			log.error("deserialization func -> {}", e.getMessage(), e);
			throw new RuntimeException(
					"Type -> " + type.getName() + ", deserialization func has Exception -> " + e.getMessage());
		}
	}

}

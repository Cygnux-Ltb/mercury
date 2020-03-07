package io.mercury.codec.avro;

import java.io.IOException;

import javax.annotation.concurrent.NotThreadSafe;

import org.apache.avro.io.EncoderFactory;
import org.apache.avro.io.JsonEncoder;
import org.apache.avro.specific.SpecificRecord;

import io.mercury.common.character.Charsets;
import io.mercury.common.serialization.TextSerializer;

@NotThreadSafe
@Deprecated
public class AvroTextSerializer  implements TextSerializer<SpecificRecord> {

	private JsonEncoder encoder;

	/**
	 * Use default ByteArrayOutputStream size
	 */
	public AvroTextSerializer() {
		this(1024 * 8);
	}

	public AvroTextSerializer(int size) {
		super(size);
	}

	@Override
	public String serialization(SpecificRecord record) {
		writer.setSchema(record.getSchema());
		try {
			encoder = EncoderFactory.get().jsonEncoder(record.getSchema(), outputStream);
			writer.write(record, encoder);
			encoder.flush();
			byte[] bytes = outputStream.toByteArray();
			outputStream.reset();
			return new String(bytes, Charsets.UTF8);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			try {
				outputStream.close();
			} catch (IOException e1) {
				logger.error(e1.getMessage(), e);
			}
			throw new RuntimeException("AvroTextSerializer.serialization(record) -> " + e.getMessage());
		}

	}

}

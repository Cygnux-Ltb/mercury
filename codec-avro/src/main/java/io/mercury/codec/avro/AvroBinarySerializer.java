package io.mercury.codec.avro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.annotation.concurrent.NotThreadSafe;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;
import org.slf4j.Logger;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.serialization.specific.BinarySerializer;

@NotThreadSafe
public final class AvroBinarySerializer<T extends SpecificRecord> implements BinarySerializer<T> {

	private static final Logger logger = CommonLoggerFactory.getLogger(AvroBinarySerializer.class);

	private BinaryEncoder encoder;

	private ByteArrayOutputStream outputStream;

	private DatumWriter<T> writer;

	/**
	 * Use default ByteArrayOutputStream size
	 */
	public AvroBinarySerializer(Class<T> recordClass) {
		this(recordClass, 8192);
	}

	/**
	 * 
	 * @param recordClass : object type
	 * @param size        : size is inner ByteArrayOutputStream size
	 */
	protected AvroBinarySerializer(Class<T> recordClass, int size) {
		this.writer = new SpecificDatumWriter<>(recordClass);
		this.outputStream = new ByteArrayOutputStream(size);
	}

	@Override
	public ByteBuffer serialization(T obj) {
		try {
			// TODO 对象可重用
			encoder = EncoderFactory.get().binaryEncoder(outputStream, encoder);
			writer.write(obj, encoder);
			encoder.flush();
			ByteBuffer wrap = ByteBuffer.wrap(outputStream.toByteArray());
			outputStream.reset();
			return wrap;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			try {
				outputStream.close();
			} catch (IOException e1) {
				logger.error(e1.getMessage(), e);
			}
			throw new RuntimeException("AvroBytesSerializer.serialization(record) -> " + e.getMessage());
		}
	}

}

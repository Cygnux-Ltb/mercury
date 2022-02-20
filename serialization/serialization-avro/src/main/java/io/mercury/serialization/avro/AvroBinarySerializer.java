package io.mercury.serialization.avro;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;
import org.apache.avro.util.NonCopyingByteArrayOutputStream;
import org.slf4j.Logger;

import io.mercury.common.annotation.thread.ThreadSafeVariable;
import io.mercury.common.log.Log4j2LoggerFactory;
import io.mercury.common.serialization.ByteBufferSerializer;

@NotThreadSafe
public final class AvroBinarySerializer<T extends SpecificRecord> implements ByteBufferSerializer<T> {

	private static final Logger log = Log4j2LoggerFactory.getLogger(AvroBinarySerializer.class);

	private final Class<T> type;

	@ThreadSafeVariable
	private final DatumWriter<T> writer;

	private final NonCopyingByteArrayOutputStream outputStream;

	private BinaryEncoder encoder;

	/**
	 * Use default ByteArrayOutputStream size : 8192
	 */
	public AvroBinarySerializer(Class<T> type) {
		this(type, 8192);
	}

	/**
	 * 
	 * @param type    : object type
	 * @param bufSize : size is inner OutputStream size
	 */
	public AvroBinarySerializer(Class<T> type, int bufSize) {
		this.type = type;
		this.writer = new SpecificDatumWriter<>(type);
		this.outputStream = new NonCopyingByteArrayOutputStream(bufSize);
	}

	@Override
	public ByteBuffer serialization(@Nonnull T obj) {
		try {
			encoder = EncoderFactory.get().binaryEncoder(outputStream, encoder);
			writer.write(obj, encoder);
			encoder.flush();
			ByteBuffer buffer = outputStream.asByteBuffer();
			outputStream.reset();
			return buffer;
		} catch (IOException e) {
			log.error("serialization func -> {}", e.getMessage(), e);
			throw new RuntimeException(
					"Type -> " + type.getName() + ", deserialization func has Exception -> " + e.getMessage());
		}
	}

}

package io.mercury.serialization.avro;

import static io.mercury.common.concurrent.map.JctConcurrentMaps.newNonBlockingLongMap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.concurrent.ThreadSafe;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;
import org.slf4j.Logger;

import io.mercury.common.annotation.thread.ThreadSafeVariable;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.serialization.ByteBufferSerializer;

@ThreadSafe
public final class AvroBinarySerializer<T extends SpecificRecord> implements ByteBufferSerializer<T> {

	private static final Logger log = CommonLoggerFactory.getLogger(AvroBinarySerializer.class);

	@ThreadSafeVariable
	private final DatumWriter<T> writer;

	private final ConcurrentMap<Long, BinaryEncoder> encoderMap = newNonBlockingLongMap(16);

	private final ConcurrentMap<Long, ByteArrayOutputStream> streamMap = newNonBlockingLongMap(16);

	private final int bufferSize;

	/**
	 * Use default ByteArrayOutputStream size : 8192
	 */
	public AvroBinarySerializer(Class<T> clazz) {
		this(clazz, 8192);
	}

	/**
	 * 
	 * @param clazz      : object type
	 * @param bufferSize : size is inner ByteArrayOutputStream size
	 */
	public AvroBinarySerializer(Class<T> clazz, int bufferSize) {
		this.writer = new SpecificDatumWriter<>(clazz);
		this.bufferSize = bufferSize;
	}

	@Override
	public ByteBuffer serialization(T obj) {
		try {
			final long threadId = Thread.currentThread().getId();
			final ByteArrayOutputStream outputStream = streamMap.putIfAbsent(threadId,
					new ByteArrayOutputStream(bufferSize));
			final BinaryEncoder encoder = encoderMap.putIfAbsent(threadId,
					EncoderFactory.get().binaryEncoder(outputStream, null));
			writer.write(obj, encoder);
			encoder.flush();
			ByteBuffer buffer = ByteBuffer.wrap(outputStream.toByteArray());
			outputStream.reset();
			return buffer;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException("AvroBytesSerializer serialization exception -> " + e.getMessage(), e);
		}
	}

}

package io.mercury.serialization.avro;

import static io.mercury.common.concurrent.map.JctConcurrentMaps.newNonBlockingLongMap;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.concurrent.ThreadSafe;

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

@ThreadSafe
public final class AvroBinarySerializer<T extends SpecificRecord> implements ByteBufferSerializer<T> {

	private static final Logger log = Log4j2LoggerFactory.getLogger(AvroBinarySerializer.class);

	@ThreadSafeVariable
	private final DatumWriter<T> writer;

	private final ConcurrentMap<Long, BinaryEncoder> encoderMap = newNonBlockingLongMap(16);

	private final ConcurrentMap<Long, NonCopyingByteArrayOutputStream> streamMap = newNonBlockingLongMap(16);

	private final int bufSize;

	/**
	 * Use default ByteArrayOutputStream size : 8192
	 */
	public AvroBinarySerializer(Class<T> type) {
		this(type, 8192);
	}

	/**
	 * 
	 * @param type    : object type
	 * @param bufSize : size is inner ByteArrayOutputStream size
	 */
	public AvroBinarySerializer(Class<T> type, int bufSize) {
		this.writer = new SpecificDatumWriter<>(type);
		this.bufSize = bufSize;
	}

	@Override
	public ByteBuffer serialization(T obj) {
		try {
			var threadId = Thread.currentThread().getId();
			var outputStream = streamMap.putIfAbsent(threadId, new NonCopyingByteArrayOutputStream(bufSize));
			var encoder = encoderMap.putIfAbsent(threadId, EncoderFactory.get().binaryEncoder(outputStream, null));
			writer.write(obj, encoder);
			encoder.flush();
			var buffer = outputStream.asByteBuffer();
			outputStream.reset();
			return buffer;
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException("AvroBytesSerializer serialization exception -> " + e.getMessage(), e);
		}
	}

}

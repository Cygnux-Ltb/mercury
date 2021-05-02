package io.mercury.serialization.avro;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecord;
import org.slf4j.Logger;

import io.mercury.common.annotation.lang.ThreadSafeVariable;
import io.mercury.common.concurrent.map.ConcurrentMaps;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.serialization.spec.BytesDeserializer;

@ThreadSafe
public final class AvroBinaryDeserializer<T extends SpecificRecord> implements BytesDeserializer<T> {

	private static final Logger log = CommonLoggerFactory.getLogger(AvroBinaryDeserializer.class);

	@ThreadSafeVariable
	private final DatumReader<T> reader;

	private final ConcurrentMap<Long, BinaryDecoder> decoders = ConcurrentMaps.newNonBlockingLongMap(16);

	public AvroBinaryDeserializer(Class<T> clazz) {
		this.reader = new SpecificDatumReader<>(clazz);
	}

	@Override
	public T deserialization(@Nonnull byte[] source, @Nullable T reuse) {
		try {
			return reader.read(reuse, getDecoder(source));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException("AvroBytesDeserializer.deSerialization(bytes, tClass) -> " + e.getMessage());
		}
	}

	private BinaryDecoder getDecoder(byte[] source) {
		final long threadId = Thread.currentThread().getId();
		BinaryDecoder decoder = decoders.get(threadId);
		decoder = DecoderFactory.get().binaryDecoder(source, decoder);
		return decoders.put(threadId, decoder);
	}

	private int offset;
	private byte remainingBytes[];

	public List<T> deserializationMultiple(byte[] bytes) {
		byte[] allBytes;
		if (remainingBytes != null) {
			log.warn("Incomplete bytes encountered from previous packet, now trying to concat");
			ByteArrayOutputStream os = new ByteArrayOutputStream(remainingBytes.length + bytes.length);
			try {
				os.write(remainingBytes);
				os.write(bytes);
			} catch (IOException e) {
				log.error(e.getMessage(), e);
				throw new RuntimeException("Error concat incomplete bytes -> " + e.getMessage());
			}
			allBytes = os.toByteArray();
		} else {
			allBytes = bytes;
		}
		List<T> resultList = new ArrayList<T>();
		try {
			int countSize = allBytes.length;

			remainingBytes = null; // Comment for testing
			offset = 0;
			BinaryDecoder decoder = getDecoder(allBytes);// Comment for testing

			InputStream inputStream = decoder.inputStream();

			while (inputStream.available() != 0) {
				T t = reader.read(null, decoder);
				resultList.add(t);
				offset = countSize - inputStream.available();
			}
		} catch (EOFException e) {
			remainingBytes = Arrays.copyOfRange(allBytes, offset, allBytes.length);
			log.debug("remainingBytes.length -> " + remainingBytes.length);
			log.debug("recvBytes.length -> " + allBytes.length);
			log.warn("Incomplete bytes packet encountered: " + e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException("AvroBytesDeserializer.deSerialization(bytes, tClass) -> " + e.getMessage());
		}
		return resultList;
	}

}

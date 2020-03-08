package io.mercury.codec.avro;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.concurrent.NotThreadSafe;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecord;
import org.slf4j.Logger;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.serialization.specific.BinaryDeserializer;

@NotThreadSafe
public final class AvroBinaryDeserializer<T extends SpecificRecord> implements BinaryDeserializer<T> {

	private static final Logger logger = CommonLoggerFactory.getLogger(AvroBinaryDeserializer.class);

	private BinaryDecoder decoder;

	private SpecificDatumReader<T> datumReader;

	public AvroBinaryDeserializer(Class<T> tClass) {
		this.datumReader = new SpecificDatumReader<>(tClass);
	}

	@Override
	public T deserialization(T reuse, ByteBuffer source) {
		try {
			return datumReader.read(reuse, initDecoder(source));
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException("AvroBytesDeserializer.deSerialization(bytes, tClass) -> " + e.getMessage());
		}
	}

	private BinaryDecoder initDecoder(ByteBuffer source) {
		return DecoderFactory.get().binaryDecoder(source.array(), decoder);
	}

	private int offset;
	private byte remainingBytes[];

	public List<T> deserializationMultiple(byte[] bytes) {
		byte[] allBytes;
		if (remainingBytes != null) {
			logger.warn("Incomplete bytes encountered from previous packet, now trying to concat");
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(remainingBytes.length + bytes.length);
			try {
				outputStream.write(remainingBytes);
				outputStream.write(bytes);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw new RuntimeException("Error concat incomplete bytes -> " + e.getMessage());
			}
			allBytes = outputStream.toByteArray();
		} else {
			allBytes = bytes;
		}
		List<T> resultList = new ArrayList<T>();
		try {
			int countSize = allBytes.length;

			remainingBytes = null; // Comment for testing
			offset = 0;
			decoder = initDecoder(ByteBuffer.wrap(allBytes));// Comment for testing

			InputStream inputStream = decoder.inputStream();

			while (inputStream.available() != 0) {
				T t = datumReader.read(null, decoder);
				resultList.add(t);
				offset = countSize - inputStream.available();
			}
		} catch (EOFException e) {
			remainingBytes = Arrays.copyOfRange(allBytes, offset, allBytes.length);
			logger.debug("remainingBytes.length -> " + remainingBytes.length);
			logger.debug("recvBytes.length -> " + allBytes.length);
			logger.warn("Incomplete bytes packet encountered: " + e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException("AvroBytesDeserializer.deSerialization(bytes, tClass) -> " + e.getMessage());
		}
		return resultList;
	}

}

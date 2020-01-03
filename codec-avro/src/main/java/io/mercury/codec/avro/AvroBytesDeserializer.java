package io.mercury.codec.avro;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.concurrent.NotThreadSafe;

import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecord;

import io.mercury.common.serialization.BytesDeserializer;

@NotThreadSafe
public final class AvroBytesDeserializer<T extends SpecificRecord> extends BaseAvroDeserializer<T>
		implements BytesDeserializer<T> {

	private BinaryDecoder decoder;

	private Class<T> tClass;

	public AvroBytesDeserializer(Class<T> tClass) {
		this.tClass = tClass;
	}

	@Override
	public T deSerializationSingle(byte[] bytes) {
		try {
			return getDatumReader(tClass).read(null, decoder);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException("AvroBytesDeserializer.deSerialization(bytes, tClass) -> " + e.getMessage());
		}
	}

	private BinaryDecoder initDecoder(byte[] bytes) {
		return DecoderFactory.get().binaryDecoder(bytes, decoder);
	}

	private int offset;
	private byte remainingBytes[];

	@Override
	public List<T> deSerializationMultiple(byte[] bytes) {
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
			decoder = initDecoder(allBytes);// Comment for testing

			InputStream inputStream = decoder.inputStream();
			SpecificDatumReader<T> datumReader = getDatumReader(tClass);
			
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

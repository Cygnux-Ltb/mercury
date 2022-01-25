package io.mercury.serialization.avro;

import static io.mercury.common.concurrent.map.JctConcurrentMaps.newNonBlockingLongMap;

import java.util.concurrent.ConcurrentMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import org.apache.avro.specific.SpecificRecord;
import org.slf4j.Logger;

import io.mercury.common.log.Log4j2LoggerFactory;
import io.mercury.common.serialization.BytesDeserializer;

/**
 * 
 * @author yellow013
 *
 * @param <T>
 */
@ThreadSafe
public final class AvroBinaryDeserializerBuf<T extends SpecificRecord> implements BytesDeserializer<T> {

	private static final Logger log = Log4j2LoggerFactory.getLogger(AvroBinaryDeserializerBuf.class);

	private final Class<T> type;

	private final ConcurrentMap<Long, AvroBinaryDeserializer<T>> deserializers = newNonBlockingLongMap(16);

	/**
	 * 
	 * @param type
	 */
	public AvroBinaryDeserializerBuf(Class<T> type) {
		this.type = type;
	}

	@Override
	public T deserialization(@Nonnull byte[] source, @Nullable T reuse) {
		try {
			return getDeserializer().deserialization(source, reuse);
		} catch (Exception e) {
			log.error("deserialization func -> {}", e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 根据线程ID获取Deserializer
	 * 
	 * @param source
	 * @return
	 */
	private AvroBinaryDeserializer<T> getDeserializer() {
		long threadId = Thread.currentThread().getId();
		return deserializers.putIfAbsent(threadId, new AvroBinaryDeserializer<>(type));
	}

//	private int offset;
//	private byte remainingBytes[];
//
//	public List<T> deserializationMultiple(byte[] bytes) {
//		byte[] allBytes;
//		if (remainingBytes != null) {
//			log.warn("Incomplete bytes encountered from previous packet, now trying to concat");
//			ByteArrayOutputStream os = new ByteArrayOutputStream(remainingBytes.length + bytes.length);
//			try {
//				os.write(remainingBytes);
//				os.write(bytes);
//			} catch (IOException e) {
//				log.error(e.getMessage(), e);
//				throw new RuntimeException("Error concat incomplete bytes -> " + e.getMessage());
//			}
//			allBytes = os.toByteArray();
//		} else {
//			allBytes = bytes;
//		}
//		List<T> resultList = new ArrayList<T>();
//		try {
//			int countSize = allBytes.length;
//
//			remainingBytes = null; // Comment for testing
//			offset = 0;
//			BinaryDecoder decoder = getDecoder(allBytes);// Comment for testing
//
//			try (final InputStream inputStream = decoder.inputStream()) {
//				while (inputStream.available() != 0) {
//					T t = reader.read(null, decoder);
//					resultList.add(t);
//					offset = countSize - inputStream.available();
//				}
//			}
//		} catch (EOFException e) {
//			remainingBytes = Arrays.copyOfRange(allBytes, offset, allBytes.length);
//			log.debug("remainingBytes.length -> " + remainingBytes.length);
//			log.debug("recvBytes.length -> " + allBytes.length);
//			log.warn("Incomplete bytes packet encountered: " + e.getMessage());
//		} catch (IOException e) {
//			log.error(e.getMessage(), e);
//			throw new RuntimeException("AvroBytesDeserializer.deSerialization(bytes, tClass) -> " + e.getMessage());
//		}
//		return resultList;
//	}

}

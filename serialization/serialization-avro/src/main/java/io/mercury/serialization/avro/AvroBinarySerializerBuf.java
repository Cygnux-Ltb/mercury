package io.mercury.serialization.avro;

import static io.mercury.common.concurrent.map.JctConcurrentMaps.newNonBlockingLongMap;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.concurrent.ThreadSafe;

import org.apache.avro.specific.SpecificRecord;
import org.slf4j.Logger;

import io.mercury.common.log.Log4j2LoggerFactory;
import io.mercury.common.serialization.ByteBufferSerializer;

@ThreadSafe
public final class AvroBinarySerializerBuf<T extends SpecificRecord> implements ByteBufferSerializer<T> {

	private static final Logger log = Log4j2LoggerFactory.getLogger(AvroBinarySerializerBuf.class);

	private final Class<T> type;

	private final ConcurrentMap<Long, AvroBinarySerializer<T>> serializers = newNonBlockingLongMap(16);

	private final int bufSize;

	/**
	 * Use default ByteArrayOutputStream size : 8192
	 */
	public AvroBinarySerializerBuf(Class<T> type) {
		this(type, 8192);
	}

	/**
	 * 
	 * @param type    : object type
	 * @param bufSize : size is inner ByteArrayOutputStream size
	 */
	public AvroBinarySerializerBuf(Class<T> type, int bufSize) {
		this.type = type;
		this.bufSize = bufSize;
	}

	@Override
	public ByteBuffer serialization(T obj) {
		try {
			return getSerializer().serialization(obj);
		} catch (Exception e) {
			log.error("serialization func -> {}", e.getMessage(), e);
			throw e;
		}
	}

	private AvroBinarySerializer<T> getSerializer() {
		long threadId = Thread.currentThread().getId();
		return serializers.putIfAbsent(threadId, new AvroBinarySerializer<>(type, bufSize));
	}

}

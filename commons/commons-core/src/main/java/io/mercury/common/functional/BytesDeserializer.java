package io.mercury.common.functional;

import java.util.function.Function;

/**
 * 
 * @author yellow013
 *
 * @param <R>
 */
@FunctionalInterface
public interface BytesDeserializer<R> extends Function<byte[], R> {

	/**
	 * convert byte[] to R
	 *
	 * @param bytes bytes
	 * @return R
	 */
	default R deserialization(byte[] bytes) {
		return apply(bytes);
	}
	
}

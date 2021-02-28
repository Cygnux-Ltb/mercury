package io.mercury.common.codec.api;

import java.util.function.Function;

/**
 * 编码器
 * 
 * @author yellow013
 *
 * @param <T>
 * @param <R>
 */
@FunctionalInterface
public interface Encoder<T, R> extends Function<T, R> {

	/**
	 * 
	 * @param t
	 * @return
	 */
	R encode(T t);

	@Override
	default R apply(T t) {
		return encode(t);
	}

}

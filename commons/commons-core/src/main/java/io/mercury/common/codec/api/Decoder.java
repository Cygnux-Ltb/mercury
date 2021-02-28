package io.mercury.common.codec.api;

import java.util.function.Function;

/**
 * 
 * 解码器
 * 
 * @author yellow013
 *
 * @param <T>
 * @param <R>
 */
@FunctionalInterface
public interface Decoder<T, R> extends Function<T, R> {

	/**
	 * 
	 * @param t
	 * @return
	 */
	R decode(T t);

	@Override
	default R apply(T t) {
		return decode(t);
	}

}

package io.mercury.common.codec.api;

import java.util.function.Function;

/**
 * 定义可解码的对象
 * 
 * @author yellow013
 *
 * @param <T>
 * @param <R>
 */
@FunctionalInterface
public interface Decodable<T extends Decodable<T, R>, R> extends Function<T, R> {

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

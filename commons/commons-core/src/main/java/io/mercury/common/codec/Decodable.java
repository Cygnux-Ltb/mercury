package io.mercury.common.codec;

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
public interface Decodable<T, R> extends Function<T, R> {

	R decode(T t);

	@Override
	default R apply(T t) {
		return decode(t);
	}

}

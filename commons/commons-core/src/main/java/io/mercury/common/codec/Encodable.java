package io.mercury.common.codec;

import java.util.function.Supplier;

/**
 * 
 * 定义可编码的对象
 * 
 * @author yellow013
 *
 * @param <R>
 */
@FunctionalInterface
public interface Encodable<R> extends Supplier<R> {

	/**
	 * 
	 * @return
	 */
	R encode();

	@Override
	default R get() {
		return encode();
	}

}

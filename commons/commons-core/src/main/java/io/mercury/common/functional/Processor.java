package io.mercury.common.functional;

import javax.annotation.Nonnull;

/**
 * 
 * @author yellow013
 *
 * @param <T>
 */
@FunctionalInterface
public interface Processor<T> {

	/**
	 * 
	 * @param t
	 * @throws Exception
	 */
	void process(@Nonnull T t) throws Exception;

}

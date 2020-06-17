package io.mercury.common.functional;

@FunctionalInterface
public interface Processor<T> {

	void process(T t) throws Exception;

}

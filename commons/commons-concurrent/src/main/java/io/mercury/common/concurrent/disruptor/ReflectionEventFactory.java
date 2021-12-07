package io.mercury.common.concurrent.disruptor;

import static io.mercury.common.util.JreReflection.invokeConstructor;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import com.lmax.disruptor.EventFactory;

import io.mercury.common.concurrent.disruptor.example.LongEvent;
import io.mercury.common.lang.Assertor;
import io.mercury.common.util.JreReflection.RuntimeReflectionException;

/**
 * 
 * @author yellow013 <br>
 *         <br>
 * @implNote 通过反射实现EventFactory
 *
 * @param <T>
 */
public final class ReflectionEventFactory<T> implements EventFactory<T> {

	private final Class<T> type;

	private ReflectionEventFactory(Class<T> type) {
		this.type = type;
	}

	/**
	 * 
	 * @param <T>
	 * @param type
	 * @return
	 */
	public static <T> ReflectionEventFactory<T> with(@Nonnull Class<T> type) {
		return with(type, null);
	}

	/**
	 * 
	 * @param <T>
	 * @param type
	 * @param log
	 * @return
	 * @throws RuntimeReflectionException
	 */
	public static <T> ReflectionEventFactory<T> with(@Nonnull Class<T> type, Logger log)
			throws RuntimeReflectionException {
		Assertor.nonNull(type, "type");
		var factory = new ReflectionEventFactory<>(type);
		try {
			factory.newInstance();
		} catch (RuntimeReflectionException e) {
			if (log != null)
				log.error("Class -> {} : {}", type, e.getMessage(), e);
			else
				System.err.println("Class -> " + type + " : " + e.getMessage());
			throw e;
		}
		return factory;
	}

	@Override
	public T newInstance() {
		try {
			return invokeConstructor(type);
		} catch (RuntimeReflectionException e) {
			throw e;
		}
	}

	public static void main(String[] args) {

		ReflectionEventFactory<LongEvent> reflectionEventFactory = new ReflectionEventFactory<>(LongEvent.class);

		LongEvent newInstance = reflectionEventFactory.newInstance();
		newInstance.set(100);

		System.out.println(newInstance.get());
		System.out.println(newInstance.getClass());

	}

}

package io.mercury.common.concurrent.ring.base;

import com.lmax.disruptor.EventFactory;
import io.mercury.common.lang.Asserter;
import io.mercury.common.util.JreReflection;
import io.mercury.common.util.JreReflection.RuntimeReflectionException;
import org.slf4j.Logger;

import javax.annotation.Nonnull;

/**
 * @param <T>
 * @author yellow013 <br>
 * <br>
 * @implNote 通过反射实现EventFactory
 */
public final class ReflectionEventFactory<T> implements EventFactory<T> {

    private final Class<T> type;

    private ReflectionEventFactory(Class<T> type) {
        this.type = type;
    }

    /**
     * @param type Event class type
     * @return ReflectionEventFactory<T>
     * @throws RuntimeReflectionException re
     */
    public static <T> ReflectionEventFactory<T> newFactory(@Nonnull Class<T> type)
            throws RuntimeReflectionException {
        return newFactory(type, null);
    }

    /**
     * @param type Event class type
     * @param log  Error logger
     * @return ReflectionEventFactory<T>
     * @throws RuntimeReflectionException re
     */
    public static <T> ReflectionEventFactory<T> newFactory(@Nonnull Class<T> type, Logger log)
            throws RuntimeReflectionException {
        Asserter.nonNull(type, "type");
        ReflectionEventFactory<T> factory = new ReflectionEventFactory<>(type);
        try {
            // Try call newInstance() function
            factory.newInstance();
            return factory;
        } catch (RuntimeReflectionException e) {
            if (log != null)
                log.error("Class -> {} :: new instance exception -> {}", type, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public T newInstance() {
        return JreReflection.invokeConstructor(type);
    }

}

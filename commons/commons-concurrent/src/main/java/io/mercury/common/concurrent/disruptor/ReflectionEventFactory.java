package io.mercury.common.concurrent.disruptor;

import com.lmax.disruptor.EventFactory;
import io.mercury.common.concurrent.disruptor.example.LongEvent;
import io.mercury.common.lang.Asserter;
import io.mercury.common.util.JreReflection.RuntimeReflectionException;
import org.slf4j.Logger;

import javax.annotation.Nonnull;

import static io.mercury.common.util.JreReflection.invokeConstructor;

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
        return invokeConstructor(type);
    }

    public static void main(String[] args) {

        ReflectionEventFactory<LongEvent> eventFactory = new ReflectionEventFactory<>(LongEvent.class);

        LongEvent event = eventFactory.newInstance();
        event.set(100);

        System.out.println(event.get());
        System.out.println(event.getClass());

    }

}

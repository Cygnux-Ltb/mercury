package io.mercury.common.serialization.basic;

import javax.annotation.Nullable;
import java.util.function.Function;

@FunctionalInterface
public interface Serializer<T, R> extends Function<T, R> {

    @Nullable
    R serialization(T source);

    @Override
    default R apply(T t) {
        return serialization(t);
    }

}

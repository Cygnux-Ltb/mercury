package io.mercury.common.collections.map;

import io.mercury.common.datetime.DateTimeUtil;

import javax.annotation.concurrent.NotThreadSafe;
import java.time.LocalDateTime;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.ToLongFunction;

@NotThreadSafe
public final class LocalDateTimeMap<V> extends TemporalMap<LocalDateTime, V, LocalDateTimeMap<V>> {

    private LocalDateTimeMap(ToLongFunction<LocalDateTime> keyFunc,
                             Function<LocalDateTime, LocalDateTime> nextKeyFunc,
                             BiPredicate<LocalDateTime, LocalDateTime> hasNextKey) {
        super(keyFunc, nextKeyFunc, hasNextKey);
    }

    private static final BiPredicate<LocalDateTime, LocalDateTime> HasNextKey =
            (nextKey, endPoint) -> nextKey.isBefore(endPoint) || nextKey.equals(endPoint);

    /**
     * @return V
     */
    public static <V> LocalDateTimeMap<V> newMapWithHour() {
        return new LocalDateTimeMap<>(DateTimeUtil::datetimeOfHour,
                key -> key.plusHours(1), HasNextKey);
    }

    /**
     * @return V
     */
    public static <V> LocalDateTimeMap<V> newMapWithMinute() {
        return new LocalDateTimeMap<>(DateTimeUtil::datetimeOfMinute,
                key -> key.plusMinutes(1), HasNextKey);
    }

    /**
     * @return V
     */
    public static <V> LocalDateTimeMap<V> newMapWithSecond() {
        return new LocalDateTimeMap<>(DateTimeUtil::datetimeOfSecond,
                key -> key.plusSeconds(1), HasNextKey);
    }

    @Override
    protected LocalDateTimeMap<V> self() {
        return this;
    }

}

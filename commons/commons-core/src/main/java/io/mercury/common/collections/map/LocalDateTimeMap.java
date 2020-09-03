package io.mercury.common.collections.map;

import static io.mercury.common.datetime.DateTimeUtil.datetimeOfHour;
import static io.mercury.common.datetime.DateTimeUtil.datetimeOfMinute;
import static io.mercury.common.datetime.DateTimeUtil.datetimeOfSecond;

import java.time.LocalDateTime;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.ToLongFunction;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public final class LocalDateTimeMap<V> extends TemporalMap<LocalDateTime, V, LocalDateTimeMap<V>> {

	private LocalDateTimeMap(ToLongFunction<LocalDateTime> keyFunc, Function<LocalDateTime, LocalDateTime> nextKeyFunc,
			BiPredicate<LocalDateTime, LocalDateTime> hasNextKey) {
		super(keyFunc, nextKeyFunc, hasNextKey);
	}

	private static final BiPredicate<LocalDateTime, LocalDateTime> HasNextKey = (nextKey,
			endPoint) -> nextKey.isBefore(endPoint) || nextKey.equals(endPoint);

	/**
	 * 
	 * @param <V>
	 * @return
	 */
	public final static <V> LocalDateTimeMap<V> newMapWithHour() {
		return new LocalDateTimeMap<>(key -> datetimeOfHour(key), key -> key.plusHours(1), HasNextKey);
	}

	/**
	 * 
	 * @param <V>
	 * @return
	 */
	public final static <V> LocalDateTimeMap<V> newMapWithMinute() {
		return new LocalDateTimeMap<>(key -> datetimeOfMinute(key), key -> key.plusMinutes(1), HasNextKey);
	}

	/**
	 * 
	 * @param <V>
	 * @return
	 */
	public final static <V> LocalDateTimeMap<V> newMapWithSecond() {
		return new LocalDateTimeMap<>(key -> datetimeOfSecond(key), key -> key.plusSeconds(1), HasNextKey);
	}

	@Override
	protected LocalDateTimeMap<V> returnThis() {
		return this;
	}

}

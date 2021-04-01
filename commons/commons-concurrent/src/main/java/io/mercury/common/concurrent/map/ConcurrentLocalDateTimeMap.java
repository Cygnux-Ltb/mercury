package io.mercury.common.concurrent.map;

import java.time.LocalDateTime;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.ToLongFunction;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import io.mercury.common.datetime.DateTimeUtil;

@NotThreadSafe
public final class ConcurrentLocalDateTimeMap<V>
		extends ConcurrentTemporalMap<LocalDateTime, V, ConcurrentLocalDateTimeMap<V>> {

	/**
	 * 
	 * @param conversionFunc
	 */
	private ConcurrentLocalDateTimeMap(ToLongFunction<LocalDateTime> keyToLangFunc,
			Function<LocalDateTime, LocalDateTime> nextKeyFunc, BiPredicate<LocalDateTime, LocalDateTime> hasNextKey) {
		super(keyToLangFunc, nextKeyFunc, hasNextKey);
	}

	private static final ToLongFunction<LocalDateTime> KeyFuncWithHour = DateTimeUtil::datetimeOfHour;
	private static final Function<LocalDateTime, LocalDateTime> NextKeyFuncWithHour = key -> key.plusHours(1);

	private static final ToLongFunction<LocalDateTime> KeyFuncWithMinute = DateTimeUtil::datetimeOfMinute;
	private static final Function<LocalDateTime, LocalDateTime> NextKeyFuncWithMinute = key -> key.plusMinutes(1);

	private static final ToLongFunction<LocalDateTime> KeyFuncWithSecond = DateTimeUtil::datetimeOfSecond;
	private static final Function<LocalDateTime, LocalDateTime> NextKeyFuncWithSecond = key -> key.plusSeconds(1);

	private static final BiPredicate<LocalDateTime, LocalDateTime> HasNextKey = (nextKey,
			endPoint) -> nextKey.isBefore(endPoint) || nextKey.equals(endPoint);

	/**
	 * 
	 * @param <V>
	 * @return
	 */
	public final static <V> ConcurrentLocalDateTimeMap<V> newMapToHour() {
		return new ConcurrentLocalDateTimeMap<>(KeyFuncWithHour, NextKeyFuncWithHour, HasNextKey);
	}

	/**
	 * 
	 * @param <V>
	 * @return
	 */
	public final static <V> ConcurrentLocalDateTimeMap<V> newMapToMinute() {
		return new ConcurrentLocalDateTimeMap<>(KeyFuncWithMinute, NextKeyFuncWithMinute, HasNextKey);
	}

	/**
	 * 
	 * @param <V>
	 * @return
	 */
	public final static <V> ConcurrentLocalDateTimeMap<V> newMapToSecond() {
		return new ConcurrentLocalDateTimeMap<>(KeyFuncWithSecond, NextKeyFuncWithSecond, HasNextKey);
	}

	@Override
	public ConcurrentLocalDateTimeMap<V> put(@Nonnull LocalDateTime key, V value) {
		put(keyFunc.applyAsLong(key), value);
		return this;
	}

}

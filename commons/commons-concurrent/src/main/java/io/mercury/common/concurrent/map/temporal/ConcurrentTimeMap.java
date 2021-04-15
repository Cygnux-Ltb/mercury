package io.mercury.common.concurrent.map;

import java.time.LocalTime;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.ToLongFunction;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import io.mercury.common.datetime.DateTimeUtil;

@NotThreadSafe
public final class ConcurrentLocalTimeMap<V> extends ConcurrentTemporalMap<LocalTime, V, ConcurrentLocalTimeMap<V>> {

	private ConcurrentLocalTimeMap(ToLongFunction<LocalTime> keyToLangFunc, Function<LocalTime, LocalTime> nextKeyFunc,
			BiPredicate<LocalTime, LocalTime> hasNextKey) {
		super(keyToLangFunc, nextKeyFunc, hasNextKey);
	}

	private static final ToLongFunction<LocalTime> KeyFuncWithHour = DateTimeUtil::timeOfHour;
	private static final Function<LocalTime, LocalTime> NextKeyFuncWithHour = key -> key.plusHours(1);

	private static final ToLongFunction<LocalTime> KeyFuncWithMinute = DateTimeUtil::timeOfMinute;
	private static final Function<LocalTime, LocalTime> NextKeyFuncWithMinute = key -> key.plusMinutes(1);

	private static final ToLongFunction<LocalTime> KeyFuncWithSecond = DateTimeUtil::timeOfSecond;
	private static final Function<LocalTime, LocalTime> NextKeyFuncWithSecond = key -> key.plusSeconds(1);

	private static final BiPredicate<LocalTime, LocalTime> HasNextKey = (nextKey,
			endPoint) -> nextKey.isBefore(endPoint) || nextKey.equals(endPoint);

	/**
	 * 
	 * @param <V>
	 * @return
	 */
	public final static <V> ConcurrentLocalTimeMap<V> newMapToHour() {
		return new ConcurrentLocalTimeMap<>(KeyFuncWithHour, NextKeyFuncWithHour, HasNextKey);
	}

	/**
	 * 
	 * @param <V>
	 * @return
	 */
	public final static <V> ConcurrentLocalTimeMap<V> newMapToMinute() {
		return new ConcurrentLocalTimeMap<>(KeyFuncWithMinute, NextKeyFuncWithMinute, HasNextKey);
	}

	/**
	 * 
	 * @param <V>
	 * @return
	 */
	public final static <V> ConcurrentLocalTimeMap<V> newMapToSecond() {
		return new ConcurrentLocalTimeMap<>(KeyFuncWithSecond, NextKeyFuncWithSecond, HasNextKey);
	}

	@Override
	public ConcurrentLocalTimeMap<V> put(@Nonnull LocalTime key, V value) {
		put(keyFunc.applyAsLong(key), value);
		return this;
	}

	public static void main(String[] args) {

		System.out.println(Long.MAX_VALUE);
		System.out.println(DateTimeUtil.datetimeOfMillisecond());

	}

}

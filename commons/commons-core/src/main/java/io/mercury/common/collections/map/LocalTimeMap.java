package io.mercury.common.collections.map;

import static io.mercury.common.datetime.DateTimeUtil.timeOfHour;
import static io.mercury.common.datetime.DateTimeUtil.timeOfMinute;
import static io.mercury.common.datetime.DateTimeUtil.timeOfSecond;

import java.time.LocalTime;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.ToLongFunction;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import io.mercury.common.datetime.DateTimeUtil;

@NotThreadSafe
public final class LocalTimeMap<V> extends TemporalMap<LocalTime, V, LocalTimeMap<V>> {

	private LocalTimeMap(ToLongFunction<LocalTime> keyToLangFunc, Function<LocalTime, LocalTime> nextKeyFunc,
			BiPredicate<LocalTime, LocalTime> hasNextKey) {
		super(keyToLangFunc, nextKeyFunc, hasNextKey);
	}

	private static ToLongFunction<LocalTime> keyToLangFuncWithHour = key -> timeOfHour(key);
	private static Function<LocalTime, LocalTime> nextKeyFuncWithHour = key -> key.plusHours(1);

	private static ToLongFunction<LocalTime> keyToLangFuncWithMinute = key -> timeOfMinute(key);
	private static Function<LocalTime, LocalTime> nextKeyFuncWithMinute = key -> key.plusMinutes(1);

	private static ToLongFunction<LocalTime> keyToLangFuncWithSecond = key -> timeOfSecond(key);
	private static Function<LocalTime, LocalTime> nextKeyFuncWithSecond = key -> key.plusSeconds(1);

	private static BiPredicate<LocalTime, LocalTime> hasNextKey = (nextKey, endPoint) -> nextKey.isBefore(endPoint)
			|| nextKey.equals(endPoint);

	public final static <V> LocalTimeMap<V> newMapToHour() {
		return new LocalTimeMap<>(keyToLangFuncWithHour, nextKeyFuncWithHour, hasNextKey);
	}

	public final static <V> LocalTimeMap<V> newMapToMinute() {
		return new LocalTimeMap<>(keyToLangFuncWithMinute, nextKeyFuncWithMinute, hasNextKey);
	}

	public final static <V> LocalTimeMap<V> newMapToSecond() {
		return new LocalTimeMap<>(keyToLangFuncWithSecond, nextKeyFuncWithSecond, hasNextKey);
	}

	@Override
	public LocalTimeMap<V> put(@Nonnull LocalTime key, V value) {
		put(keyToLangFunc.applyAsLong(key), value);
		return this;
	}

	public static void main(String[] args) {

		System.out.println(Long.MAX_VALUE);
		System.out.println(DateTimeUtil.datetimeOfMillisecond());

	}

}

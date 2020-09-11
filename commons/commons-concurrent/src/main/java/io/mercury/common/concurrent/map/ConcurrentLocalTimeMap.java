package io.mercury.common.concurrent.map;

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
public final class ConcurrentLocalTimeMap<V> extends ConcurrentTemporalMap<LocalTime, V, ConcurrentLocalTimeMap<V>> {

	private ConcurrentLocalTimeMap(ToLongFunction<LocalTime> keyToLangFunc, Function<LocalTime, LocalTime> nextKeyFunc,
			BiPredicate<LocalTime, LocalTime> hasNextKey) {
		super(keyToLangFunc, nextKeyFunc, hasNextKey);
	}

	private static final ToLongFunction<LocalTime> keyToLangFuncWithHour = key -> timeOfHour(key);
	private static final Function<LocalTime, LocalTime> nextKeyFuncWithHour = key -> key.plusHours(1);

	private static final ToLongFunction<LocalTime> keyToLangFuncWithMinute = key -> timeOfMinute(key);
	private static final Function<LocalTime, LocalTime> nextKeyFuncWithMinute = key -> key.plusMinutes(1);

	private static final ToLongFunction<LocalTime> keyToLangFuncWithSecond = key -> timeOfSecond(key);
	private static final Function<LocalTime, LocalTime> nextKeyFuncWithSecond = key -> key.plusSeconds(1);

	private static final BiPredicate<LocalTime, LocalTime> hasNextKey = (nextKey,
			endPoint) -> nextKey.isBefore(endPoint) || nextKey.equals(endPoint);

	public final static <V> ConcurrentLocalTimeMap<V> newMapToHour() {
		return new ConcurrentLocalTimeMap<>(keyToLangFuncWithHour, nextKeyFuncWithHour, hasNextKey);
	}

	public final static <V> ConcurrentLocalTimeMap<V> newMapToMinute() {
		return new ConcurrentLocalTimeMap<>(keyToLangFuncWithMinute, nextKeyFuncWithMinute, hasNextKey);
	}

	public final static <V> ConcurrentLocalTimeMap<V> newMapToSecond() {
		return new ConcurrentLocalTimeMap<>(keyToLangFuncWithSecond, nextKeyFuncWithSecond, hasNextKey);
	}

	@Override
	public ConcurrentLocalTimeMap<V> put(@Nonnull LocalTime key, V value) {
		put(keyToLangFunc.applyAsLong(key), value);
		return this;
	}

	public static void main(String[] args) {

		System.out.println(Long.MAX_VALUE);
		System.out.println(DateTimeUtil.datetimeOfMillisecond());

	}

}

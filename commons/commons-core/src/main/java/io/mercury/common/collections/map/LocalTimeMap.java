package io.mercury.common.collections.map;

import static io.mercury.common.datetime.DateTimeUtil.timeOfHour;
import static io.mercury.common.datetime.DateTimeUtil.timeOfMinute;
import static io.mercury.common.datetime.DateTimeUtil.timeOfSecond;

import java.time.LocalTime;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.ToLongFunction;

import javax.annotation.concurrent.NotThreadSafe;

import io.mercury.common.datetime.DateTimeUtil;

@NotThreadSafe
public final class LocalTimeMap<V> extends TemporalMap<LocalTime, V, LocalTimeMap<V>> {

	private LocalTimeMap(ToLongFunction<LocalTime> keyFunc, Function<LocalTime, LocalTime> nextKeyFunc,
			BiPredicate<LocalTime, LocalTime> hasNextKey) {
		super(keyFunc, nextKeyFunc, hasNextKey);
	}

	private static final BiPredicate<LocalTime, LocalTime> HasNextKey = (nextKey,
			endPoint) -> nextKey.isBefore(endPoint) || nextKey.equals(endPoint);

	/**
	 * 
	 * @param <V>
	 * @return
	 */
	public static <V> LocalTimeMap<V> newMapWithHour() {
		return new LocalTimeMap<>(DateTimeUtil::timeOfHour, key -> key.plusHours(1), HasNextKey);
	}

	/**
	 * 
	 * @param <V>
	 * @return
	 */
	public static <V> LocalTimeMap<V> newMapWithMinute() {
		return new LocalTimeMap<>(DateTimeUtil::timeOfMinute, key -> key.plusMinutes(1), HasNextKey);
	}

	/**
	 * 
	 * @param <V>
	 * @return
	 */
	public static <V> LocalTimeMap<V> newMapWithSecond() {
		return new LocalTimeMap<>(DateTimeUtil::timeOfSecond, key -> key.plusSeconds(1), HasNextKey);
	}

	@Override
	protected LocalTimeMap<V> self() {
		return this;
	}

	public static void main(String[] args) {

		System.out.println(Long.MAX_VALUE);
		System.out.println(DateTimeUtil.datetimeOfMillisecond());

	}

}

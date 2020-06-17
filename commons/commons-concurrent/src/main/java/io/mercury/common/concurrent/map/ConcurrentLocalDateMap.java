package io.mercury.common.concurrent.map;

import static io.mercury.common.datetime.DateTimeUtil.date;

import java.time.LocalDate;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.ToLongFunction;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.eclipse.collections.api.list.MutableList;

@NotThreadSafe
public final class ConcurrentLocalDateMap<V> extends ConcurrentTemporalMap<LocalDate, V, ConcurrentLocalDateMap<V>> {

	private ConcurrentLocalDateMap(ToLongFunction<LocalDate> keyToLangFunc, Function<LocalDate, LocalDate> nextKeyFunc,
			BiPredicate<LocalDate, LocalDate> hasNextKey) {
		super(keyToLangFunc, nextKeyFunc, hasNextKey);
	}

	private static ToLongFunction<LocalDate> keyToLangFunc = key -> date(key);
	private static Function<LocalDate, LocalDate> nextKeyFunc = key -> key.plusDays(1);

	private static BiPredicate<LocalDate, LocalDate> hasNextKey = (nextKey, endPoint) -> nextKey.isBefore(endPoint)
			|| nextKey.equals(endPoint);

	public final static <V> ConcurrentLocalDateMap<V> newMap() {
		return new ConcurrentLocalDateMap<>(keyToLangFunc, nextKeyFunc, hasNextKey);
	}

	@Override
	public ConcurrentLocalDateMap<V> put(@Nonnull LocalDate key, V value) {
		put(keyToLangFunc.applyAsLong(key), value);
		return this;
	}

	public static void main(String[] args) {

		ConcurrentLocalDateMap<String> newMap = newMap();

		LocalDate date = LocalDate.of(2019, 5, 30);
		for (int i = 0; i < 5000; i++) {
			date = date.plusDays(1);
			newMap.put(date, date.toString());
		}

		for (int i = 0; i < 5000; i++) {
			long nanoTime0 = System.nanoTime();
			MutableList<String> scan = newMap.scan(LocalDate.now(), LocalDate.now().plusYears(1));
			long nanoTime1 = System.nanoTime();
			System.out.println((nanoTime1 - nanoTime0) / 1000);
			System.out.println(scan.size());
		}

		newMap.scan(LocalDate.now(), LocalDate.now().plusYears(1)).each(str -> System.out.println(str));

	}

}

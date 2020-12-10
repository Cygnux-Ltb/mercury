package io.mercury.common.sequence;

import static io.mercury.common.thread.Threads.sleep;
import static io.mercury.common.util.BitOperator.intBinaryFormat;
import static io.mercury.common.util.BitOperator.longBinaryFormat;
import static io.mercury.common.util.BitOperator.shortBinaryFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.annotation.concurrent.ThreadSafe;

import org.eclipse.collections.api.set.primitive.MutableLongSet;

import io.mercury.common.collections.Capacity;
import io.mercury.common.collections.MutableSets;
import io.mercury.common.datetime.EpochTime;

/**
 * 
 * Use Snow flake ID
 * 
 * @author yellow013
 *
 */
@ThreadSafe
public final class SeqAllocator {

	public static final long allocate() {
		long seq = allocate0();
		while (seq <= 0) {
			sleep(1);
			seq = allocate0();
		}
		return seq;
	}

	private static volatile long lastEpochMillis;

	private static volatile int incr;

	/**
	 * Unsigned Short max value
	 */
	private static final int incrLimit = 0xffff;

	public synchronized static final long allocate0() {
		long epochMillis = EpochTime.millis();
		if (lastEpochMillis != epochMillis) {
			lastEpochMillis = epochMillis;
			incr = 0;
		}
		if (++incr > incrLimit)
			return -1L;
		return (lastEpochMillis << 15) | incr;
	}

	public static void main(String[] args) {

		long t0 = System.nanoTime();

		MutableLongSet set = MutableSets.newLongHashSet(Capacity.L21_SIZE_2097152);

		for (int i = 0; i < 1000000; i++) {
			set.add(SeqAllocator.allocate());
		}
		System.out.println(set.size());
		long t1 = System.nanoTime();

		System.out.println((t1 - t0) / 1000000);

		long epochMillis = EpochTime.millis();

		long lmEpochMillis = epochMillis << 16;

		System.out.println(longBinaryFormat(epochMillis));
		System.out.println(longBinaryFormat(lmEpochMillis));
		System.out.println(longBinaryFormat(100L));
		System.out.println(longBinaryFormat(lmEpochMillis | 100L));

		System.out.println(intBinaryFormat(10));
		System.out.println(intBinaryFormat(1));
		System.out.println(intBinaryFormat(10 | 1));
		System.out.println(intBinaryFormat(0x7fff));
		System.out.println(shortBinaryFormat(Short.MAX_VALUE));
		System.out.println(10 & 1);

		ZonedDateTime dateTime = ZonedDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.MIN, ZoneOffset.UTC);
		long baseEpochMilli = dateTime.toInstant().toEpochMilli();
		System.out.println(baseEpochMilli);
		System.out.println(longBinaryFormat(baseEpochMilli));
		long diff = epochMillis - baseEpochMilli;
		System.out.println(diff);
		System.out.println(longBinaryFormat(diff));

		System.out.println((1L << 39) / (1000L * 60 * 60 * 24 * 365));

	}

}

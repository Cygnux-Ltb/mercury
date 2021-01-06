package io.mercury.common.sequence;

import static io.mercury.common.thread.Threads.sleep;
import static io.mercury.common.util.BitFormatter.intBinaryFormat;
import static io.mercury.common.util.BitFormatter.longBinaryFormat;
import static java.lang.System.currentTimeMillis;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.annotation.concurrent.ThreadSafe;

import org.eclipse.collections.api.set.primitive.MutableLongSet;

import io.mercury.common.collections.Capacity;
import io.mercury.common.collections.MutableSets;
import io.mercury.common.datetime.EpochTime;
import io.mercury.common.util.BitFormatter;

/**
 * 
 * Use Epoch Time ID
 * 
 * @author yellow013
 *
 */
@ThreadSafe
public final class EpochSeqAllocator {

	public static final long allocate() {
		long seq = allocate0();
		while (seq <= 0) {
			sleep(1);
			seq = allocate0();
		}
		return seq;
	}

	// 最后使用的Epoch毫秒
	private static volatile long lastEpochMillis;

	// 自增位
	private static volatile long incr;

	// 自增位最大限制
	private static final long incrLimit = 0xffff;

	/**
	 * 
	 * @return
	 */
	private synchronized static final long allocate0() {
		long currentEpochMillis = currentTimeMillis();
		if (lastEpochMillis != currentEpochMillis) {
			lastEpochMillis = currentEpochMillis;
			incr = 0;
		}
		if (++incr > incrLimit) {
			return -1L;
		}
		return (lastEpochMillis << Short.SIZE) | incr;
	}

	public static final long parseEpoch(long seq) {
		return seq >>> Short.SIZE;
	}

	public static void main(String[] args) {

		MutableLongSet longSet = MutableSets.newLongHashSet(Capacity.L21_SIZE_2097152);
		long t0 = System.nanoTime();
		for (int i = 0; i < 1000000; i++) {
			longSet.add(EpochSeqAllocator.allocate());
		}
		long t1 = System.nanoTime();
		long tx = (t1 - t0) / 1000000;
		System.out.println(longSet.size() + " count time ms -> " + tx);

		// set.each(System.out::println);

		long epochMillis = EpochTime.millis();
		System.out.println("epoch millis binary: ");
		System.out.println(longBinaryFormat(epochMillis));
		long lmEpochMillis = epochMillis << 16;
		System.out.println("epoch millis << 16 binary: ");
		System.out.println(longBinaryFormat(lmEpochMillis));

		System.out.println(longBinaryFormat(100L));
		System.out.println(longBinaryFormat(lmEpochMillis | 0x7fff));

		System.out.println(intBinaryFormat(10));
		System.out.println(intBinaryFormat(1));
		System.out.println(intBinaryFormat(10 | 1));
		System.out.println(intBinaryFormat(0x7fff));
		System.out.println(10 & 1);

		ZonedDateTime dateTime = ZonedDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.MIN, ZoneOffset.UTC);
		long baseEpochMilli = dateTime.toInstant().toEpochMilli();
		System.out.println(baseEpochMilli);
		System.out.println(longBinaryFormat(baseEpochMilli));
		long diff = epochMillis - baseEpochMilli;
		System.out.println(diff);
		System.out.println(longBinaryFormat(diff));

		System.out.println((1L << 39) / (1000L * 60 * 60 * 24 * 365));

		System.out.println(BitFormatter.intBinary(0xffff));
		System.out.println(BitFormatter.intBinary(Short.MAX_VALUE));

		long allocate = EpochSeqAllocator.allocate();
		System.out.println(allocate);
		System.out.println(BitFormatter.longBinaryFormat(allocate));
		System.out.println(BitFormatter.longBinaryFormat(EpochSeqAllocator.parseEpoch(allocate)));

	}

}

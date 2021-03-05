package io.mercury.common.sequence;

import static java.lang.System.currentTimeMillis;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import io.mercury.common.datetime.TimeZone;
import io.mercury.common.util.Assertor;
import io.mercury.common.util.BitFormatter;

/**
 * 
 * 通过将63位正整数long类型拆分为三部分实现唯一序列 <br>
 * 时间戳 | 所有者(可以是某个业务或者分布式系统上的机器) | 自增序列<br>
 * 
 * <pre>
 * 0b|---------------EPOCH TIMESTAMP----------------|--OWNER---|-INCREMENT-|
 * 0b01111111 11111111 11111111 11111111 11111111 11111111 11111111 11111111
 * 
 * <pre>
 *
 * @author yellow013
 */
public final class SnowflakeAlgorithm {

	/**
	 * 最小基线时间, UTC 2000-01-01 00:00:00.000
	 */
	public static final ZonedDateTime MinimumBaseline = ZonedDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.MIN,
			TimeZone.UTC);

	// 开始时间截 (使用自己业务系统指定的时间)
	private final long baselineMillis;

	// 是否直接使用Epoch时间
	private final boolean isUseEpoch;

	// 自增位最多占用16位
	private final int increment_bit_limit = Byte.SIZE * 2;

	// 机器ID所占的位数
	private final long workerIdBits = 10L;

	// 支持的最大机器ID, 结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
	@SuppressWarnings("unused")
	private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

	// 序列在ID中占的位数
	private final long sequenceBits = 12L;

	// 机器ID向左移12位
	private final long ownerIdShift = sequenceBits;

	// 时间截向左移22位(10+12)
	private final long timestampLeftShift = sequenceBits + workerIdBits;

	// 生成序列的掩码, 这里为4095 (0xfff == 4095 == 0b111111111111)
	private final long sequenceMask = -1L ^ (-1L << sequenceBits);

	// 工作机器ID(0~1024)
	private long ownerId;

	// 毫秒内序列(0~4095)
	private long sequence = 0L;

	// 上次生成ID的时间截
	private volatile long lastTimestamp = -1L;

	/**
	 * 
	 * @param baseline
	 */
	private SnowflakeAlgorithm(ZonedDateTime baseline) {
		if(baseline == null )
		//ZonedDateTime epoch = ZonedDateTime.ofInstant(Instant.EPOCH, TimeZone.UTC);
		
		this.baselineMillis = MinimumBaseline.isBefore(baseline) ? MinimumBaseline.toInstant().toEpochMilli()
				: baseline.toInstant().toEpochMilli();
	}

	/**
	 * 
	 * @param baseline
	 * @return
	 */
	public static SnowflakeAlgorithm newInstance(@Nonnull LocalDate baseline) {
		Assertor.nonNull(baseline, "baseline");
		return newInstance(baseline, ZoneOffset.UTC);
	}

	/**
	 * 
	 * @param baseline
	 * @param zoneId
	 * @return
	 */
	public static SnowflakeAlgorithm newInstance(@Nonnull LocalDate baseline, @Nonnull ZoneId zoneId) {
		Assertor.nonNull(baseline, "baseline");
		Assertor.nonNull(zoneId, "zoneId");
		return new SnowflakeAlgorithm(ZonedDateTime.of(baseline, LocalTime.MIN, zoneId));
	}

	/**
	 * 获得下一个ID (该方法是线程安全的)
	 * 
	 * @return SnowflakeId
	 */
	public synchronized long next() throws ClockBackwardException {
		long currentMillis = currentTimeMillis();

		// 如果当前时间小于上一次ID生成的时间戳, 说明系统时钟回退过这个时候应当抛出异常
		if (currentMillis < lastTimestamp) {
			throw new ClockBackwardException(lastTimestamp - currentMillis);
		}

		// 如果是同一时间生成的, 则进行毫秒内序列
		if (lastTimestamp == currentMillis) {
			sequence = (sequence + 1) & sequenceMask;
			// 毫秒内序列溢出
			if (sequence == 0) {
				// 阻塞到下一个毫秒, 获得新的时间戳
				currentMillis = tilNextMillis(lastTimestamp);
			}
		}
		// 时间戳改变, 毫秒内序列重置
		else {
			sequence = 0L;
		}

		// 上次生成ID的时间截
		lastTimestamp = currentMillis;

		// 移位并通过或运算拼到一起组成64位的ID
		return ((currentMillis - baselineMillis) << timestampLeftShift) // 时间戳左移至高位
				| (ownerId << ownerIdShift) // 所有者ID左移至中间位
				| sequence; // 自增位
	}

	/**
	 * 阻塞到下一个毫秒, 直到获得新的时间戳
	 * 
	 * @param lastTimestamp 上次生成ID的时间截
	 * @return 当前时间戳
	 */
	protected long tilNextMillis(final long lastTimestamp) {
		long timestamp;
		do {
			timestamp = currentTimeMillis();
		} while (timestamp <= lastTimestamp);
		return timestamp;
	}

	public static void main(String[] args) {

		System.out.println(0b11111111_11111111);
		System.out.println(BitFormatter.shortBinaryFormat((short) 0b11111111_11111111));
		System.out.println(BitFormatter.shortBinaryFormat(Short.MAX_VALUE));
		System.out.println(0b01111111_11111111_11111111_11111111);
		System.out.println(BitFormatter.intBinaryFormat(Integer.MAX_VALUE));
		System.out.println(BitFormatter.intBinaryFormat((int) 0b01111111_11111111_11111111_11111111));

		long l0 = 0b00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L;

		ZonedDateTime dateTime = ZonedDateTime.of(LocalDate.of(2000, 1, 1), LocalTime.MIN, TimeZone.UTC);

		long l1 = dateTime.toEpochSecond() * 1000;
		System.out.println(BitFormatter.longBinaryFormat(l1));

		ZonedDateTime now = ZonedDateTime.now(TimeZone.UTC);
		System.out.println(now);
		long nowMillis = now.toInstant().toEpochMilli();
		System.out.println(MinimumBaseline);
		long minMillis = MinimumBaseline.toInstant().toEpochMilli();

		long diffMillis = nowMillis - minMillis;
		System.out.println("diffMillis -> " + BitFormatter.longBinaryFormat(diffMillis));
		long dayMillis = TimeUnit.DAYS.toMillis(1);
		System.out.println(dayMillis);
		System.out.println(BitFormatter.longBinaryFormat(dayMillis));
		System.out.println(diffMillis / dayMillis / 365);

		long maxDiff = -1L >>> (Byte.SIZE * 5);
		System.out.println(maxDiff);
		System.out.println(BitFormatter.longBinaryFormat(maxDiff));
		System.out.println(ZonedDateTime.ofInstant(Instant.EPOCH, TimeZone.UTC).plus(maxDiff * 15, ChronoUnit.SECONDS));

	}

}

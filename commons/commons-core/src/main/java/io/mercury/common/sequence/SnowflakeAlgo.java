package io.mercury.common.sequence;

import static java.lang.System.currentTimeMillis;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.annotation.Nonnull;

import io.mercury.common.datetime.EpochUtil;
import io.mercury.common.datetime.TimeZone;
import io.mercury.common.util.BitFormatter;
import io.mercury.common.util.BitOperator;
import io.mercury.common.util.HexUtil;

/**
 * 
 * 通过将63位正整数long类型拆分为三部分实现唯一序列 <br>
 * 时间戳 | 所有者(可以是某个业务或者分布式系统上的机器) | 自增序列<br>
 * 
 * <pre>
 * 0b|---------------EPOCH TIMESTAMP----------------|--OWNER----|-INCREMENT-|
 * 0b01111111 11111111 11111111 11111111 11111111 11_111111 1111_1111 11111111
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

	/*
	 * 所有者ID所占的位数
	 */
	public static final int OwnerIdBits = 10;

	/*
	 * 序列在ID中占的位数
	 */
	public static final int SequenceBits = 12;

	/*
	 * 
	 */
	public static final int TimestampBits = 41;

	/*
	 * 所有者ID向左移12位
	 */
	public static final int OwnerIdLeftShift = SequenceBits;

	/*
	 * 时间截向左移22位(10+12)
	 */
	public static final int TimestampLeftShift = OwnerIdBits + SequenceBits;

	/*
	 * 生成序列的掩码, 4095 (0xffff == 4096 == 0b1111_11111111)
	 */
	public static final long SequenceMask = BitOperator.maxValueOfBit(SequenceBits);

	/**
	 * 所有者ID的掩码
	 */
	public static final long OwnerIdMask = BitOperator.maxValueOfBit(OwnerIdBits) << OwnerIdLeftShift;

	// 开始时间截 (使用自己业务系统指定的时间)
	private final long baseline;

	// 所有者ID(0~1024)
	private final long ownerId;

	// 毫秒内序列(0~4096)
	private volatile long sequence = 0L;

	// 上次生成ID的时间截
	private volatile long lastTimestamp = -1L;

	/**
	 * 
	 * @param ownerId
	 */
	public SnowflakeAlgorithm(int ownerId) {
		this(ownerId, ZonedDateTime.ofInstant(Instant.EPOCH, TimeZone.UTC));
	}

	/**
	 * 
	 * @param ownerId
	 * @param baseline
	 */
	public SnowflakeAlgorithm(int ownerId, @Nonnull LocalDate baseline) {
		this(ownerId, baseline, ZoneOffset.UTC);
	}

	/**
	 * 
	 * @param ownerId
	 * @param baseline
	 * @param zoneId
	 */
	public SnowflakeAlgorithm(int ownerId, @Nonnull LocalDate baseline, @Nonnull ZoneId zoneId) {
		this(ownerId, baseline == null ? EpochUtil.ZeroPoint
				: ZonedDateTime.of(baseline, LocalTime.MIN, zoneId == null ? TimeZone.UTC : zoneId));
	}

	/**
	 * 
	 * @param baseline
	 */
	private SnowflakeAlgorithm(int ownerId, ZonedDateTime baseline) {
		if (ownerId < 0 || ownerId >= 1024)
			throw new IllegalArgumentException("ownerId must be [greater than 0] and [less than or equal 1024]");
		this.ownerId = ownerId;
		this.baseline = baseline.isBefore(EpochUtil.ZeroPoint) ? 0 : baseline.toInstant().toEpochMilli();
	}

	/**
	 * 获取基线时间
	 * 
	 * @return
	 */
	public long getBaseline() {
		return baseline;
	}

	/**
	 * 解析序列ID中包含的Epoch时间戳
	 * 
	 * @param seq
	 * @return
	 */
	public long parseEpochMillis(long seq) {
		return parseBaselineDiffMillis(seq) + baseline;
	}

	/**
	 * 解析序列ID中包含的基线偏移时间
	 * 
	 * @param seq
	 * @return
	 */
	public static long parseBaselineDiffMillis(long seq) {
		return (seq >> (OwnerIdBits + SequenceBits));
	}

	/**
	 * 解析序列ID中包含的所有者ID
	 * 
	 * @param seq
	 * @return
	 */
	public static long parseOwnerId(long seq) {
		return (seq & OwnerIdMask) >> OwnerIdLeftShift;
	}

	/**
	 * 获得下一个ID (该方法是线程安全的)
	 * 
	 * @return SnowflakeId
	 */
	public synchronized long next() throws ClockBackwardException {
		long currentTimestamp = currentTimeMillis();
		// 如果当前时间小于上一次ID生成的时间戳, 说明系统时钟回退过这个时候应当抛出异常
		if (currentTimestamp < lastTimestamp) {
			throw new ClockBackwardException(lastTimestamp, currentTimestamp);
		}
		// 如果是同一时间生成的, 则进行毫秒内序列
		if (currentTimestamp == lastTimestamp) {
			sequence = (sequence + 1) & SequenceMask;
			// 毫秒内序列溢出
			if (sequence == 0L) {
				// 阻塞到下一个毫秒, 获得新的时间戳
				currentTimestamp = nextMillis(lastTimestamp);
			}
		}
		// 时间戳改变, 毫秒内序列重置
		else {
			sequence = 0L;
		}
		// 更新最后一次生成ID的时间截
		lastTimestamp = currentTimestamp;
		// 移位并通过或运算拼接在一起组成64位ID
		return // 计算时间戳于基线时间的偏移量, 并将偏移量左移至高位
		((currentTimestamp - baseline) << TimestampLeftShift)
				// 所有者ID左移至中间
				| (ownerId << OwnerIdLeftShift)
				// 自增位
				| sequence;
	}

	/**
	 * 自旋阻塞到下一个毫秒, 直到获得新的时间戳
	 * 
	 * @param lastTimestamp 上次生成ID的时间截
	 * @return 当前时间戳
	 */
	private long nextMillis(final long lastTimestamp) {
		long timestamp;
		do {
			timestamp = currentTimeMillis();
		} while (timestamp <= lastTimestamp);
		return timestamp;
	}

	public static void main(String[] args) {

		System.out.println(BitFormatter.longBinaryFormat(SequenceMask));
		System.out.println(BitFormatter.longBinaryFormat(OwnerIdMask));

		SnowflakeAlgorithm algorithm = new SnowflakeAlgorithm(20);

		long next = algorithm.next();
		System.out.println(BitFormatter.longBinaryFormat(next));

		long time = algorithm.parseEpochMillis(next);
		System.out.println(System.currentTimeMillis());
		System.out.println(time);
		System.out.println(BitFormatter.longBinaryFormat(time));
		System.out.println(SnowflakeAlgorithm.parseOwnerId(next));

		System.out.println(ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), TimeZone.UTC));

		long maxSequence = BitOperator.maxValueOfBit(SequenceBits);
		System.out.println(maxSequence);
		System.out.println(BitFormatter.longBinaryFormat(maxSequence));
		System.out.println(HexUtil.toHexString(maxSequence));

		long maxOwnerId = BitOperator.maxValueOfBit(OwnerIdBits);
		System.out.println(maxOwnerId);
		System.out.println(BitFormatter.longBinaryFormat(maxOwnerId));
		System.out.println(HexUtil.toHexString(maxOwnerId));

	}

}

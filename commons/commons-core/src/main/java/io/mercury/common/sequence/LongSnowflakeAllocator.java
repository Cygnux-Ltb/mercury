package io.mercury.common.sequence;

import static io.mercury.common.util.BitFormatter.longBinaryFormat;
import static java.lang.System.currentTimeMillis;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import io.mercury.common.datetime.TimeZone;

/**
 * 
 * 
 * 
 * 通过将31位正整数int类型拆分为三部分实现唯一序列 <br>
 * 时间戳 | 所有者(可以是某个业务或者分布式系统上的机器) | 自增序列<br>
 * 0b_01111111 11111111 11111111 11111111<br>
 * 可用的部分只有31位
 *
 * @author yellow013
 */
public final class LongSnowflakeAllocator {

	/**
	 * 
	 * @author yellow013
	 *
	 */
	public static class Bulider {

		private final LocalDateTime baselineEpoch;

		private Bulider(LocalDateTime baselineEpoch) {
			this.baselineEpoch = baselineEpoch;
		}

		public LongSnowflakeAllocator bulid() {
			return new LongSnowflakeAllocator(this);
		}

	}

	public static LongSnowflakeAllocator newAllocator(LocalDateTime baselineEpoch) {
		return new Bulider(baselineEpoch).bulid();
	}

	private LongSnowflakeAllocator(Bulider bulider) {
		this.baselineEpoch = ZonedDateTime.of(bulider.baselineEpoch, ZoneOffset.UTC).toEpochSecond();
	}

	// 开始时间截 (使用自己业务系统指定的时间)
	private final long baselineEpoch;

	// 自增位最多占用16位
	private final int increment_bit_limit = Byte.SIZE * 2;

	// 开始时间截 (这个用自己业务系统上线的时间)
	private final long twepoch = 1575365018000L;

	// 机器ID所占的位数
	private final long workerIdBits = 10L;

	// 支持的最大机器ID，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
	@SuppressWarnings("unused")
	private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

	// 序列在ID中占的位数
	private final long sequenceBits = 12L;

	// 机器ID向左移12位
	private final long workerIdShift = sequenceBits;

	// 时间截向左移22位(10+12)
	private final long timestampLeftShift = sequenceBits + workerIdBits;

	// 生成序列的掩码，这里为4095 (0xfff == 4095 == 0b111111111111)
	private final long sequenceMask = -1L ^ (-1L << sequenceBits);

	// 工作机器ID(0~1024)
	private long workerId;

	// 毫秒内序列(0~4095)
	private long sequence = 0L;

	// 上次生成ID的时间截
	private volatile long lastTimestamp = -1L;

	// ==============================Function==========================================
	/**
	 * 获得下一个ID (该方法是线程安全的)
	 * 
	 * @return SnowflakeId
	 */
	public synchronized long nextSeq() throws ClockBackwardException {
		long timestamp = System.currentTimeMillis();

		// 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
		if (timestamp < lastTimestamp) {
			throw new ClockBackwardException(lastTimestamp - timestamp);
		}

		// 如果是同一时间生成的，则进行毫秒内序列
		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			// 毫秒内序列溢出
			if (sequence == 0) {
				// 阻塞到下一个毫秒,获得新的时间戳
				timestamp = tilNextMillis(lastTimestamp);
			}
		}
		// 时间戳改变，毫秒内序列重置
		else {
			sequence = 0L;
		}

		// 上次生成ID的时间截
		lastTimestamp = timestamp;

		// 移位并通过或运算拼到一起组成64位的ID
		return ((timestamp - twepoch) << timestampLeftShift) // 时间戳左移至高位
				| (workerId << workerIdShift) // 所有者ID左移至中间位
				| sequence; // 自增位
	}

	/**
	 * 阻塞到下一个毫秒，直到获得新的时间戳
	 * 
	 * @param lastTimestamp 上次生成ID的时间截
	 * @return 当前时间戳
	 */
	protected long tilNextMillis(long lastTimestamp) {
		long timestamp = currentTimeMillis();
		while (timestamp <= lastTimestamp) {
			timestamp = currentTimeMillis();
		}
		return timestamp;
	}


	public static void main(String[] args) {

		long l = 0b00000000_00000000_00000000_00000000_00000000_00000000_11111111_11111111L;
		long l0 = 0b00000000_00000000_00000000_01111111_11111111_11111111_11111111_11111111L;
		long l1 = 0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_11111111L;

		ZonedDateTime baseline = ZonedDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.MIN, TimeZone.UTC);

		long years = Duration.between(LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.MIN),
				LocalDateTime.of(LocalDate.of(2021, 1, 1), LocalTime.MIN)).toMillis();

		System.out.println(Byte.SIZE);

		System.out.println(longBinaryFormat(System.currentTimeMillis() - baseline.toInstant().toEpochMilli()));
		System.out.println(longBinaryFormat(l));
		System.out.println(l);
		System.out.println(longBinaryFormat(l0));
		System.out.println(l0);
		System.out.println(l1);
		System.out.println(years);
		System.out.println(l0 / years);

	}

}

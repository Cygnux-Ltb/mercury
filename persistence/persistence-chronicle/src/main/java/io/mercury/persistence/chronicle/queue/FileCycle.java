package io.mercury.persistence.chronicle.queue;

import static io.mercury.common.datetime.TimeConst.MILLIS_PER_SECONDS;

import java.time.Instant;

import net.openhft.chronicle.queue.RollCycle;
import net.openhft.chronicle.queue.RollCycles;

public enum FileCycle {

	/**
	 * 0x4000000 (1073741824) entries per minute, indexing every 16th entry
	 */
	MINUTELY(RollCycles.MINUTELY, "0x4000000 (1073741824) entries per minute, indexing every 16th entry"),

	/**
	 * 0x40000000 (1073741824) entries per 5 minutes, indexing every 256th entry
	 */
	FIVE_MINUTELY(RollCycles.FIVE_MINUTELY,
			"0x40000000 (1073741824) entries per 5 minutes, indexing every 256th entry"),

	/**
	 * 0x40000000 (1073741824) entries per 10 minutes, indexing every 256th entry
	 */
	TEN_MINUTELY(RollCycles.TEN_MINUTELY, "0x40000000 (1073741824) entries per 10 minutes, indexing every 256th entry"),

	/**
	 * 0x40000000 (1073741824) entries per 20 minutes, indexing every 256th entry
	 */
	TWENTY_MINUTELY(RollCycles.TWENTY_MINUTELY,
			"0x40000000 (1073741824) entries per 20 minutes, indexing every 256th entry"),

	/**
	 * 0x40000000 (1073741824) entries per half hour, indexing every 256th entry
	 */
	HALF_HOURLY(RollCycles.HALF_HOURLY, "0x40000000 (1073741824) entries per half hour, indexing every 256th entry"),

	/**
	 * 0x10000000 (268435456) entries per hour, indexing every 16th entry
	 */
	HOURLY(RollCycles.HOURLY, "0x10000000 (268435456) entries per hour, indexing every 16th entry"),

	/**
	 * 0xffffffff (4294967295) entries per hour, indexing every 256th entry
	 */
	FAST_HOURLY(RollCycles.FAST_HOURLY, "0xffffffff (4294967295) entries per hour, indexing every 256th entry"),

	/**
	 * 0xffffffff (4294967295) entries per hour, indexing every 64th entry
	 */
	LARGE_HOURLY(RollCycles.LARGE_HOURLY, "0xffffffff (4294967295) entries per hour, indexing every 64th entry"),

	/**
	 * 0xffffffff (4294967295) entries per 2 hours, indexing every 256th entry
	 */
	TWO_HOURLY(RollCycles.TWO_HOURLY, "0xffffffff (4294967295) entries per 2 hours, indexing every 256th entry"),

	/**
	 * 0xffffffff (4294967295) entries per 4 hours, indexing every 256th entry
	 */
	FOUR_HOURLY(RollCycles.FOUR_HOURLY, "0xffffffff (4294967295) entries per 4 hours, indexing every 256th entry"),

	/**
	 * 0xffffffff (4294967295) entries per 6 hours, indexing every 256th entry
	 */
	SIX_HOURLY(RollCycles.SIX_HOURLY, "0xffffffff (4294967295) entries per 6 hours, indexing every 256th entry"),

	/**
	 * 0x20000000 (536870912) entries per day, indexing every 8th entry
	 */
	SMALL_DAILY(RollCycles.SMALL_DAILY, "0x20000000 (536870912) entries per day, indexing every 8th entry"),

	/**
	 * 0xffffffff (4294967295) entries per day, indexing every 64th entry
	 */
	DAILY(RollCycles.DAILY, "0xffffffff (4294967295) entries per day, indexing every 64th entry"),

	/**
	 * 0xffffffff (4294967295) entries per day, indexing every 256th entry
	 */
	FAST_DAILY(RollCycles.FAST_DAILY, "0xffffffff (4294967295) entries per day, indexing every 256th entry"),

	/**
	 * 0x1fffffffff (137438953471) entries per day, indexing every 128th entry
	 */
	LARGE_DAILY(RollCycles.LARGE_DAILY, "0x1fffffffff (137438953471) entries per day, indexing every 128th entry"),

	;

	// 每个滚动文件包含的秒数
	private final int seconds;

	private final RollCycle rollCycle;

	private final String desc;

	private FileCycle(RollCycle rollCycle, String desc) {
		this.seconds = rollCycle.lengthInMillis() / MILLIS_PER_SECONDS;
		this.rollCycle = rollCycle;
		this.desc = desc;
	}

	/**
	 * 
	 * @return
	 */
	public int getSeconds() {
		return seconds;
	}

	/**
	 * 
	 * @return
	 */
	public RollCycle getRollCycle() {
		return rollCycle;
	}

	/**
	 * 
	 * @return
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * 输入<b> [epochSecond] </b><br>
	 * 计算文件的滚动周期<b> [cycle] </b>
	 * 
	 * @param epochSecond
	 * @return
	 */
	public long toIndex(long epochSecond) throws IllegalArgumentException {
		if (epochSecond < 0)
			throw new IllegalArgumentException("param : epochSecond is can't less than 0");
		return rollCycle.toIndex((int) (epochSecond / seconds), 0);
	}

	public static void main(String[] args) {

		System.out.println(FileCycle.MINUTELY.toIndex(System.currentTimeMillis() / 1000));
		System.out.println(Integer.MAX_VALUE);
		System.out.println(Instant.now().getEpochSecond() / 60);
		System.out.println(RollCycles.DAILY.lengthInMillis());

	}

}

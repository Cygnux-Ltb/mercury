package io.mercury.persistence.chronicle.queue;

import static io.mercury.common.datetime.TimeConst.SECONDS_PER_DAY;
import static io.mercury.common.datetime.TimeConst.SECONDS_PER_HOUR;
import static io.mercury.common.datetime.TimeConst.SECONDS_PER_MINUTE;

import java.time.Instant;

import net.openhft.chronicle.queue.RollCycle;
import net.openhft.chronicle.queue.RollCycles;

public enum FileCycle {

	MINUTELY(SECONDS_PER_MINUTE, RollCycles.MINUTELY, "64 million entries per minute"),

	HOURLY(SECONDS_PER_HOUR, RollCycles.HOURLY, "256 million entries per hour"),

	HOURLY_LARGE(SECONDS_PER_HOUR, RollCycles.LARGE_HOURLY, "2 billion entries per hour"),

	DAILY_SMALL(SECONDS_PER_DAY, RollCycles.SMALL_DAILY, "512 million entries per day"),

	DAILY(SECONDS_PER_DAY, RollCycles.DAILY, "4 billion entries per day"),

	DAILY_LARGE(SECONDS_PER_DAY, RollCycles.LARGE_DAILY, "128 billion entries per day"),

	DAILY_XLARGE(SECONDS_PER_DAY, RollCycles.XLARGE_DAILY, "4 trillion entries per day, indexing every 256th entry"),

	;

	// 每个滚动文件包含的秒数
	private int seconds;

	private RollCycle rollCycle;

	private String desc;

	private FileCycle(int seconds, RollCycle rollCycle, String desc) {
		this.seconds = seconds;
		this.rollCycle = rollCycle;
		this.desc = desc;
	}

	public int getSeconds() {
		return seconds;
	}

	public RollCycle getRollCycle() {
		return rollCycle;
	}

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

	}

}

package io.mercury.common.datetime;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class TimePoint implements Comparable<TimePoint> {

	private final int date;
	private final int time;
	private final int nano;
	private final ZoneOffset offset;

	/**
	 * 
	 * @param date
	 * @param time
	 * @param nano
	 * @param offset
	 */
	private TimePoint(int date, int time, int nano, ZoneOffset offset) {
		this.date = date;
		this.time = time;
		this.nano = nano;
		this.offset = offset;
	}

	/**
	 * 
	 * @return
	 */
	public static TimePoint now() {
		return now(LocalDateTime.now(), TimeZone.SYS_DEFAULT);
	}

	/**
	 * 
	 * @param datetime
	 * @return
	 */
	public static TimePoint now(@Nonnull LocalDateTime datetime) {
		return now(datetime, TimeZone.SYS_DEFAULT);
	}

	/**
	 * 
	 * @param datetime
	 * @param offset
	 * @return
	 */
	public static TimePoint now(@Nonnull LocalDateTime datetime, @Nonnull ZoneOffset offset) {
		return new TimePoint(DateTimeUtil.date(datetime.toLocalDate()),
				DateTimeUtil.timeOfSecond(datetime.toLocalTime()), datetime.getNano(), offset);
	}

	/**
	 * 
	 * @param date
	 * @param time
	 * @param nano
	 * @return
	 */
	public static TimePoint of(int date, int time, int nano) {
		return new TimePoint(date, time, nano, TimeZone.SYS_DEFAULT);
	}

	/**
	 * 
	 * @param date
	 * @param time
	 * @param nano
	 * @param offset
	 * @return
	 */
	public static TimePoint of(int date, int time, int nano, @Nonnull ZoneOffset offset) {
		return new TimePoint(date, time, nano, offset);
	}

	public int getDate() {
		return date;
	}

	public int getTime() {
		return time;
	}

	public int getNano() {
		return nano;
	}

	public ZoneOffset getOffset() {
		return offset;
	}

	@Override
	public int compareTo(@Nullable TimePoint o) {
		return o == null ? -1
				: date < o.date ? -1
						: date > o.date ? 1
								: time < o.time ? -1
										: time > o.time ? 1
												: nano < o.nano ? -1
														: nano > o.nano ? 1
																: offset.getTotalSeconds() < o.offset.getTotalSeconds()
																		? -1
																		: offset.getId().equals(o.offset.getId()) ? 0
																				: 1;
	}

	public static void main(String[] args) {
		TimePoint now = TimePoint.now();
		System.out.println(now.getDate());
		System.out.println(now.getTime());
		System.out.println(now.getNano());
	}

}

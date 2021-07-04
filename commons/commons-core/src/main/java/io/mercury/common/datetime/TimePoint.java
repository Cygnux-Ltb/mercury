package io.mercury.common.datetime;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.annotation.Nonnull;

public final class TimePoint implements Comparable<TimePoint> {

	private final int date;
	private final int time;
	private final int nano;
	private final ZoneId zoneId;

	/**
	 * 
	 * @param date
	 * @param time
	 * @param nano
	 * @param zoneId
	 */
	private TimePoint(int date, int time, int nano, ZoneId zoneId) {
		this.date = date;
		this.time = time;
		this.nano = nano;
		this.zoneId = zoneId;
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
	 * @param zoneId
	 * @return
	 */
	public static TimePoint now(@Nonnull LocalDateTime datetime, @Nonnull ZoneId zoneId) {
		return new TimePoint(DateTimeUtil.date(datetime.toLocalDate()),
				DateTimeUtil.timeOfSecond(datetime.toLocalTime()), datetime.getNano(), zoneId);
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
	 * @param zoneId
	 * @return
	 */
	public static TimePoint of(int date, int time, int nano, @Nonnull ZoneId zoneId) {
		return new TimePoint(date, time, nano, zoneId);
	}

	@Override
	public int compareTo(TimePoint o) {
		return date < o.date ? -1
				: date > o.date ? 1
						: time < o.time ? -1 : time > o.time ? 1 : nano < o.nano ? -1 : nano > o.nano ? 1 : 0;
	}

	public int date() {
		return date;
	}

	public int time() {
		return time;
	}

	public int nano() {
		return nano;
	}

	public ZoneId zoneId() {
		return zoneId;
	}

	public static void main(String[] args) {
		TimePoint now = TimePoint.now();
		System.out.println(now.date());
		System.out.println(now.time());
		System.out.println(now.nano());
	}

}

package io.mercury.common.datetime;

import java.time.LocalDateTime;

public final class TimePoint implements Comparable<TimePoint> {

	private int date;
	private int time;
	private int nano;

	public TimePoint(int date, int time, int nano) {
		this.date = date;
		this.time = time;
		this.nano = nano;
	}

	/**
	 * TODO 使用位运算实现
	 * 
	 * @return
	 */
	public static TimePoint now() {
		LocalDateTime now = LocalDateTime.now();
		return of(DateTimeUtil.date(now.toLocalDate()), DateTimeUtil.timeOfSecond(now.toLocalTime()), now.getNano());
	}

	/**
	 * 
	 * @param date
	 * @param time
	 * @param nano
	 * @return
	 */
	public static TimePoint of(int date, int time, int nano) {
		return new TimePoint(date, time, nano);
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

	@Override
	public int compareTo(TimePoint o) {
		return date < o.date ? -1
				: date > o.date ? 1
						: time < o.time ? -1 : time > o.time ? 1 : nano < o.nano ? -1 : nano > o.nano ? 1 : 0;
	}

	public static void main(String[] args) {

		TimePoint now = TimePoint.now();
		System.out.println(now.date());
		System.out.println(now.time());
		System.out.println(now.nano());

	}

}

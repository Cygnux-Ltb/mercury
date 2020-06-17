package io.mercury.common.datetime;

import static io.mercury.common.datetime.DateTimeUtil.date;
import static io.mercury.common.datetime.DateTimeUtil.timeOfSecond;

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

	// TODO 使用位运算实现
	public static TimePoint now() {
		LocalDateTime now = LocalDateTime.now();
		return of(date(now.toLocalDate()), timeOfSecond(now.toLocalTime()), now.getNano());
	}

	public static TimePoint of(int date, int time, int nano) {
		return new TimePoint(date, time, nano);
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

	@Override
	public int compareTo(TimePoint o) {
		return date < o.date ? -1
				: date > o.date ? 1
						: time < o.time ? -1 
								: time > o.time ? 1 
										: nano < o.nano ? -1 
												: nano > o.nano ? 1 : 0;
	}

	public static void main(String[] args) {

		TimePoint now = TimePoint.now();
		System.out.println(now.getDate());
		System.out.println(now.getTime());
		System.out.println(now.getNano());

	}

}

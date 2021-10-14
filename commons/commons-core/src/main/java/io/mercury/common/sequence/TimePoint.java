package io.mercury.common.sequence;

import java.time.ZonedDateTime;

import io.mercury.common.datetime.EpochUtil;
import io.mercury.common.util.Assertor;

/**
 * 时间点序列
 * 
 * @author yellow013
 */
public class TimePoint implements Serial<TimePoint> {

	private final ZonedDateTime timePoint;

	private final long epochSecond;

	private final int repeat;

	private final long serialId;

	/**
	 * 
	 * @param timePoint
	 * @param repeat
	 */
	protected TimePoint(ZonedDateTime timePoint, int repeat) {
		this.timePoint = timePoint;
		this.epochSecond = timePoint.toEpochSecond();
		this.repeat = repeat;
		this.serialId = (epochSecond * 1000L) + repeat;
	}

	/**
	 * 根据固定时间创建新序列
	 * 
	 * @param timePoint
	 * @return
	 */
	public static TimePoint newWith(ZonedDateTime timePoint) {
		Assertor.nonNull(timePoint, "datetime");
		return new TimePoint(timePoint, 0);
	}

	/**
	 * 根据前一个序列创建新序列
	 * 
	 * @param previous
	 * @return
	 */
	public static TimePoint newWith(TimePoint previous) {
		Assertor.nonNull(previous, "previous");
		return new TimePoint(previous.timePoint, previous.repeat + 1);
	}

	public ZonedDateTime getTimePoint() {
		return timePoint;
	}

	public long getEpochSecond() {
		return epochSecond;
	}

	public int getRepeat() {
		return repeat;
	}

	@Override
	public long getSerialId() {
		return serialId;
	}

	public static void main(String[] args) {

		ZonedDateTime now = ZonedDateTime.now();

		long epochSecond = now.toEpochSecond();
		System.out.println(epochSecond);

		TimePoint timeStarted0 = TimePoint.newWith(now);
		System.out.println(timeStarted0.getTimePoint());
		System.out.println(timeStarted0.getEpochSecond());
		System.out.println(timeStarted0.getSerialId());

		TimePoint timeStarted1 = TimePoint.newWith(timeStarted0);
		System.out.println(timeStarted1.getTimePoint());
		System.out.println(timeStarted1.getEpochSecond());
		System.out.println(timeStarted1.getSerialId());

		System.out.println(EpochUtil.getEpochMillis());
		System.out.println(EpochUtil.getEpochSeconds());

		System.out.println(Long.MAX_VALUE);

	}

}

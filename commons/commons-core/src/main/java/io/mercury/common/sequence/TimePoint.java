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

	private final ZonedDateTime datetime;

	private final long epochSecond;

	private final int repeat;

	private final long serialId;

	/**
	 * 
	 * @param datetime
	 * @param repeat
	 */
	protected TimePoint(ZonedDateTime datetime, int repeat) {
		this.datetime = datetime;
		this.epochSecond = datetime.toEpochSecond();
		this.repeat = repeat;
		this.serialId = (epochSecond * 1000L) + repeat;
	}

	/**
	 * 根据固定时间创建新序列
	 * 
	 * @param datetime
	 * @return
	 */
	public static TimePoint with(ZonedDateTime datetime) {
		Assertor.nonNull(datetime, "datetime");
		return new TimePoint(datetime, 0);
	}

	/**
	 * 根据前一个序列创建新序列
	 * 
	 * @param previous
	 * @return
	 */
	public static TimePoint with(TimePoint previous) {
		Assertor.nonNull(previous, "previous");
		return new TimePoint(previous.datetime, previous.repeat + 1);
	}

	public ZonedDateTime getDatetime() {
		return datetime;
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

		TimePoint timeStarted0 = TimePoint.with(now);
		System.out.println(timeStarted0.getDatetime());
		System.out.println(timeStarted0.getEpochSecond());
		System.out.println(timeStarted0.getSerialId());

		TimePoint timeStarted1 = TimePoint.with(timeStarted0);
		System.out.println(timeStarted1.getDatetime());
		System.out.println(timeStarted1.getEpochSecond());
		System.out.println(timeStarted1.getSerialId());

		System.out.println(EpochUtil.getEpochMillis());
		System.out.println(EpochUtil.getEpochSeconds());

		System.out.println(Long.MAX_VALUE);

	}

}
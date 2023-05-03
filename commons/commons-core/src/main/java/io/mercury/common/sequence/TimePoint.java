package io.mercury.common.sequence;

import io.mercury.common.datetime.EpochTime;
import io.mercury.common.lang.Asserter;

import java.time.ZonedDateTime;

import static io.mercury.common.datetime.pattern.DateTimePattern.YYYY_MM_DD_HH_MM_SS;

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
     * @param datetime ZonedDateTime
     * @param repeat   int
     */
    protected TimePoint(ZonedDateTime datetime, int repeat) {
        this.datetime = datetime;
        this.epochSecond = datetime.toEpochSecond();
        this.repeat = repeat;
        this.serialId = (epochSecond * 10000L) + repeat;
    }

    /**
     * 根据固定时间创建新序列
     *
     * @param datetime ZonedDateTime
     * @return TimePoint
     */
    public static TimePoint with(ZonedDateTime datetime) {
        Asserter.nonNull(datetime, "datetime");
        return new TimePoint(datetime, 0);
    }

    /**
     * 根据前一个序列创建新序列
     *
     * @param previous TimePoint
     * @return TimePoint
     */
    public static TimePoint with(TimePoint previous) {
        Asserter.nonNull(previous, "previous");
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
    public long serialId() {
        return serialId;
    }

    private transient String cache;

    @Override
    public String toString() {
        if (cache == null)
            cache = epochSecond + " -> " + YYYY_MM_DD_HH_MM_SS.format(datetime) + " / " + repeat;
        return cache;
    }

    public static void main(String[] args) {

        ZonedDateTime now = ZonedDateTime.now();

        long epochSecond = now.toEpochSecond();
        System.out.println(epochSecond);

        TimePoint timeStarted0 = TimePoint.with(now);
        System.out.println(timeStarted0);
        System.out.println(timeStarted0.getDatetime());
        System.out.println(timeStarted0.getEpochSecond());
        System.out.println(timeStarted0.serialId());

        TimePoint timeStarted1 = TimePoint.with(timeStarted0);
        System.out.println(timeStarted1);
        System.out.println(timeStarted1.getDatetime());
        System.out.println(timeStarted1.getEpochSecond());
        System.out.println(timeStarted1.serialId());

        System.out.println(EpochTime.getEpochMillis());
        System.out.println(EpochTime.getEpochSeconds());

        System.out.println(Long.MAX_VALUE);

    }

}

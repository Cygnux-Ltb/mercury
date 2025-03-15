package io.mercury.common.datetime;

import io.mercury.common.collections.MutableLists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import java.time.Duration;
import java.time.ZonedDateTime;

public record ZonedTimeRange(
        ZonedDateTime start,
        ZonedDateTime end
) {

    /**
     * @param start ZonedDateTime
     * @param end   ZonedDateTime
     * @return ZonedTimeRange
     */
    public static ZonedTimeRange of(ZonedDateTime start, ZonedDateTime end) {
        return new ZonedTimeRange(start, end);
    }

    /**
     * @param start    ZonedDateTime
     * @param end      ZonedDateTime
     * @param duration Duration
     * @return MutableList<ZonedTimeRange>
     */
    public static MutableList<ZonedTimeRange> split(ZonedDateTime start, ZonedDateTime end, Duration duration) {
        if (end.isBefore(start))
            throw new IllegalArgumentException("the [end] can not before [start]");
        var timeRanges = MutableLists.<ZonedTimeRange>newFastList();
        var nextStart = start;
        do {
            var nextEndTime = nextStart.plus(duration);
            ZonedTimeRange timeRange;
            if (nextEndTime.isAfter(end))
                timeRange = ZonedTimeRange.of(nextStart, end);
            else
                timeRange = ZonedTimeRange.of(nextStart, nextEndTime);
            timeRanges.add(timeRange);
            nextStart = timeRange.end();
        } while (nextStart.isBefore(end));
        return timeRanges;
    }

    /**
     * @param start    ZonedDateTime
     * @param end      ZonedDateTime
     * @param duration Duration
     * @return ImmutableList<ZonedTimeRange>
     */
    public static ImmutableList<ZonedTimeRange> splitWithImmutable(ZonedDateTime start, ZonedDateTime end, Duration duration) {
        return split(start, end, duration).toImmutable();
    }

}

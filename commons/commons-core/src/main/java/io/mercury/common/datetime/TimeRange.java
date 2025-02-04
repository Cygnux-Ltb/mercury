package io.mercury.common.datetime;

import io.mercury.common.collections.MutableLists;
import org.eclipse.collections.api.list.MutableList;

import java.time.Duration;
import java.time.LocalDateTime;

public record TimeRange(
        LocalDateTime start,
        LocalDateTime end
) {

    public static TimeRange of(LocalDateTime start, LocalDateTime end) {
        return new TimeRange(start, end);
    }

    public static MutableList<TimeRange> splitTime(LocalDateTime start, LocalDateTime end, Duration duration) {
        var timeRanges = MutableLists.<TimeRange>newFastList();
        var nextStart = start;
        do {
            var nextEndTime = nextStart.plus(duration);
            TimeRange timeRange;
            if (nextEndTime.isAfter(end))
                timeRange = TimeRange.of(nextStart, end);
            else
                timeRange = TimeRange.of(nextStart, nextEndTime);
            timeRanges.add(timeRange);
            nextStart = timeRange.end();
        } while (nextStart.isBefore(end));
        return timeRanges;
    }

}


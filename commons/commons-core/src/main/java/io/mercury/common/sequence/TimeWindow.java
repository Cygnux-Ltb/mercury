package io.mercury.common.sequence;

import static io.mercury.common.datetime.TimeZone.SYS_DEFAULT;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYY_MM_DD_HH_MM_SS;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

import io.mercury.common.collections.MutableLists;
import io.mercury.common.util.Assertor;

/**
 * 时间窗口序列
 * 
 * @author yellow013
 */
public class TimeWindow implements Serial<TimeWindow> {

	private final long epochSecond;

	private final LocalDateTime start;

	private final LocalDateTime end;

	// TODO UNUSED
	@Deprecated
	private final ZoneOffset offset;

	private final Duration duration;

	protected TimeWindow(@Nonnull LocalDateTime start, @Nonnull LocalDateTime end, ZoneOffset offset) {
		Assertor.nonNull(start, "start");
		Assertor.nonNull(end, "end");
		Assertor.nonNull(offset, "offset");
		this.start = start;
		this.end = end;
		this.offset = offset;
		this.duration = Duration.between(start, end);
		this.epochSecond = start.toEpochSecond(offset);
	}

	public static final TimeWindow genNext(TimeWindow window) {
		return new TimeWindow(window.start.plusSeconds(window.duration.getSeconds()),
				window.end.plusSeconds(window.duration.getSeconds()), window.offset);
	}

	public static final TimeWindow with(@Nonnull LocalDateTime start, @Nonnull LocalDateTime end, ZoneOffset offset) {
		return new TimeWindow(start, end, offset);
	}

	/**
	 * 
	 * @param start
	 * @param end
	 * @param duration
	 * @return
	 */
	public static final ImmutableList<TimeWindow> segmentationWindow(LocalTime start, LocalTime end,
			Duration duration) {
		return segmentationWindow(start, end, SYS_DEFAULT, duration);
	}

	/**
	 * 
	 * @param start
	 * @param end
	 * @param offset
	 * @param duration
	 * @return
	 */
	public static final ImmutableList<TimeWindow> segmentationWindow(LocalTime start, LocalTime end, ZoneOffset offset,
			Duration duration) {
		return segmentationWindow(LocalDate.now(offset), start, end, offset, duration);
	}

	/**
	 * 
	 * @param statrDate
	 * @param start
	 * @param end
	 * @param duration
	 * @return
	 */
	public static final ImmutableList<TimeWindow> segmentationWindow(LocalDate statrDate, LocalTime start,
			LocalTime end, Duration duration) {
		return segmentationWindow(statrDate, start, end, SYS_DEFAULT, duration);
	}

	/**
	 * 
	 * @param statrDate
	 * @param start
	 * @param end
	 * @param offset
	 * @param duration
	 * @return
	 */
	public static final ImmutableList<TimeWindow> segmentationWindow(LocalDate statrDate, LocalTime start,
			LocalTime end, ZoneOffset offset, Duration duration) {
		return segmentationWindow(LocalDateTime.of(statrDate, start),
				LocalDateTime.of(start.isAfter(end) ? statrDate.plusDays(1) : statrDate, end), offset, duration);
	}

	/**
	 * 
	 * @param start
	 * @param end
	 * @param duration
	 * @return
	 */
	public static final ImmutableList<TimeWindow> segmentationWindow(LocalDateTime start, LocalDateTime end,
			Duration duration) {
		return segmentationWindow(start, end, SYS_DEFAULT, duration);
	}

	/**
	 * 
	 * @param start
	 * @param end
	 * @param offset
	 * @param duration
	 * @return
	 */
	public static final ImmutableList<TimeWindow> segmentationWindow(LocalDateTime start, LocalDateTime end,
			ZoneOffset offset, Duration duration) {
		if (end.isBefore(start))
			throw new IllegalArgumentException("the end time can not before start time");
		Duration between = Duration.between(start, end);
		long seconds = duration.getSeconds();
		long count = between.getSeconds() / seconds;
		MutableList<TimeWindow> windows = MutableLists.newFastList();
		if (count == 0) {
			// 时间窗口的持续时间超过起止时间
			windows.add(new TimeWindow(start, end, offset));
		} else {
			// 分配第一个时间窗口
			LocalDateTime t0 = start;
			LocalDateTime t1 = start.plusSeconds(seconds);
			for (int i = 0; i < count; i++) {
				windows.add(new TimeWindow(t0, t1, offset));
				// 增加新时间窗口
				t0 = t0.plusSeconds(seconds);
				t1 = t1.plusSeconds(seconds);
				// 检查新时间窗口是否超过结束时间
				if (end.equals(t1) || end.isBefore(t1)) {
					windows.add(new TimeWindow(t0, end, offset));
					break;
				}
			}
		}
		return windows.toImmutable();
	}

	/**
	 * 检查制定时间是否在时间窗口内
	 * 
	 * @param time
	 * @return
	 */
	public boolean isPeriod(LocalDateTime time) {
		return start.equals(time) || (start.isBefore(time) && end.isAfter(time)) ? true : false;
	}

	@Override
	public long getSerialId() {
		return epochSecond;
	}

	public long getEpochSecond() {
		return epochSecond;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	@Deprecated
	public ZoneOffset getOffset() {
		return offset;
	}

	private String strCache;

	@Override
	public String toString() {
		if (strCache == null)
			strCache = epochSecond + " -> [" + offset + "][" + YYYY_MM_DD_HH_MM_SS.format(start) + " -- "
					+ YYYY_MM_DD_HH_MM_SS.format(end) + "][" + duration.getSeconds() + "]";
		return strCache;
	}

	public static void main(String[] args) {
		ImmutableList<TimeWindow> windows = TimeWindow.segmentationWindow(LocalDate.now(), LocalTime.NOON,
				LocalTime.of(9, 17), Duration.ofMinutes(5));

		windows.each(System.out::println);

		TimeWindow last = windows.getLast();
		TimeWindow last_1 = windows.get(windows.size() - 2);

		System.out.println();

		System.out.println(last_1 + " -> " + last_1.isPeriod(LocalDateTime.of(2021, 10, 15, 9, 15)));
		System.out.println(last + " -> " + last.isPeriod(LocalDateTime.of(2021, 10, 15, 9, 15)));

	}

}

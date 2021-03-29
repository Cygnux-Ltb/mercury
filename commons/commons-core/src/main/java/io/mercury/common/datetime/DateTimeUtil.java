package io.mercury.common.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Date;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import io.mercury.common.datetime.pattern.TemporalPattern;
import io.mercury.common.datetime.pattern.spec.DatePattern;
import io.mercury.common.datetime.pattern.spec.DateTimePattern;
import io.mercury.common.datetime.pattern.spec.TimePattern;
import io.mercury.common.util.StringUtil;

@ThreadSafe
public final class DateTimeUtil {

	/**
	 * 返回 primitive int 表示的 yyyyMMdd
	 * 
	 * @param date
	 * @return
	 */
	public final static int date() {
		return date(LocalDate.now());
	}

	/**
	 * 根据指定 LocalDateTime 返回 primitive int 表示的 yyyyMMdd
	 * 
	 * @param date
	 * @return
	 */
	public final static int date(@Nonnull LocalDateTime dateTime) {
		return date(dateTime.toLocalDate());
	}

	/**
	 * 根据指定 ZonedDateTime 返回 primitive int 表示的 yyyyMMdd
	 * 
	 * @param date
	 * @return
	 */
	public final static int date(@Nonnull ZonedDateTime zonedDateTime) {
		return date(zonedDateTime.toLocalDate());
	}

	/**
	 * 根据指定 LocalDate 返回 primitive int 表示的 yyyyMMdd
	 * 
	 * @param date
	 * @return
	 */
	public final static int date(@Nonnull LocalDate date) {
		return date.getYear() * 10000 + date.getMonth().getValue() * 100 + date.getDayOfMonth();
	}

	/**
	 * 返回 primitive int 表示的 yyyyMM
	 * 
	 * @param date
	 * @return
	 */
	public final static int dateOfMonth() {
		return dateOfMonth(LocalDate.now());
	}

	/**
	 * 根据指定 LocalDateTime 返回 primitive int 表示的 yyyyMM
	 * 
	 * @param date
	 * @return
	 */
	public final static int dateOfMonth(@Nonnull LocalDateTime dateTime) {
		return dateOfMonth(dateTime.toLocalDate());
	}

	/**
	 * 根据指定 ZonedDateTime 返回 primitive int 表示的 yyyyMM
	 * 
	 * @param date
	 * @return
	 */
	public final static int dateOfMonth(@Nonnull ZonedDateTime zonedDateTime) {
		return dateOfMonth(zonedDateTime.toLocalDate());
	}

	/**
	 * 根据指定 LocalDate 返回 primitive int 表示的 yyyyMM
	 * 
	 * @param date
	 * @return
	 */
	public final static int dateOfMonth(@Nonnull LocalDate date) {
		return date.getYear() * 100 + date.getMonth().getValue();
	}

	/**
	 * 返回 primitive int 表示的 yyyyXXX
	 * 
	 * @param date
	 * @return
	 */
	public final static int yearDay() {
		return yearDay(LocalDate.now());
	}

	/**
	 * 根据指定 LocalDate 返回 primitive int 表示的 yyyyXXX
	 * 
	 * @param date
	 * @return
	 */
	public final static int yearDay(@Nonnull LocalDate date) {
		return date.getYear() * 1000 + date.getDayOfYear();
	}

	/**
	 * 返回 primitive int 表示的 HH
	 * 
	 * @return
	 */
	public final static int timeOfHour() {
		return timeOfHour(LocalTime.now());
	}

	/**
	 * 根据指定 LocalTime 返回 primitive int 表示的 HH
	 * 
	 * @param time
	 * @return
	 */
	public final static int timeOfHour(@Nonnull LocalTime time) {
		return time.getHour();
	}

	/**
	 * 返回 primitive int 表示的 HHmm
	 * 
	 * @return
	 */
	public final static int timeOfMinute() {
		return timeOfMinute(LocalTime.now());
	}

	/**
	 * 根据指定 LocalTime 返回 primitive int 表示的 HHmm
	 * 
	 * @param time
	 * @return
	 */
	public final static int timeOfMinute(@Nonnull LocalTime time) {
		return time.getHour() * 100 + time.getMinute();
	}

	/**
	 * 返回 primitive int 表示的 HHmmss
	 * 
	 * @return
	 */
	public final static int timeOfSecond() {
		return timeOfSecond(LocalTime.now());
	}

	/**
	 * 根据指定 LocalTime 返回 primitive int 表示的 HHmmss
	 * 
	 * @param time
	 * @return
	 */
	public final static int timeOfSecond(@Nonnull LocalTime time) {
		return time.getHour() * 10000 + time.getMinute() * 100 + time.getSecond();
	}

	/**
	 * 返回 primitive int 表示的 HHmmssSSS
	 * 
	 * @return
	 */
	public final static int timeOfMillisecond() {
		return timeOfMillisecond(LocalTime.now());
	}

	/**
	 * 根据指定 LocalTime 返回 primitive int 表示的 HHmmssSSS
	 * 
	 * @param time
	 * @return
	 */
	public final static int timeOfMillisecond(@Nonnull LocalTime time) {
		return timeOfSecond(time) * 1000 + time.getNano() / 1000000;
	}

	/**
	 * 返回 primitive long 表示的 HHmmssSSSSSS
	 * 
	 * @return
	 */
	public final static long timeOfMicrosecond() {
		return timeOfMicrosecond(LocalTime.now());
	}

	/**
	 * 根据指定 LocalTime 返回 primitive long 表示的 HHmmssSSSSSS
	 * 
	 * @param time
	 * @return
	 */
	public final static long timeOfMicrosecond(@Nonnull LocalTime time) {
		return timeOfSecond(time) * 1000000L + time.getNano() / 1000;
	}

	/**
	 * 返回 primitive long 表示的 HHmmssSSSSSSSSS
	 * 
	 * @return
	 */
	public final static long timeOfNanosecond() {
		return timeOfNanosecond(LocalTime.now());
	}

	/**
	 * 根据指定 LocalTime 返回 primitive long 表示的 HHmmssSSSSSSSSS
	 * 
	 * @param time
	 * @return
	 */
	public final static long timeOfNanosecond(@Nonnull LocalTime time) {
		return timeOfSecond(time) * 1000000000L + time.getNano();
	}

	/**
	 * 返回 primitive long 表示的 yyyyMMddHH
	 * 
	 * @return
	 */
	public final static long datetimeOfHour() {
		return datetimeOfHour(LocalDateTime.now());
	}

	/**
	 * 根据指定 LocalDateTime 返回 primitive long 表示的 yyyyMMddHH
	 * 
	 * @param dateTime
	 * @return
	 */
	public final static long datetimeOfHour(@Nonnull LocalDateTime dateTime) {
		return date(dateTime.toLocalDate()) * 100L + timeOfHour(dateTime.toLocalTime());
	}

	/**
	 * 返回 primitive long 表示的 yyyyMMddHHmm
	 * 
	 * @return
	 */
	public final static long datetimeOfMinute() {
		return datetimeOfMinute(LocalDateTime.now());
	}

	/**
	 * 根据指定 LocalDateTime 返回 primitive long 表示的 yyyyMMddHHmm
	 * 
	 * @param dateTime
	 * @return
	 */
	public final static long datetimeOfMinute(@Nonnull LocalDateTime dateTime) {
		return date(dateTime.toLocalDate()) * 10000L + timeOfMinute(dateTime.toLocalTime());
	}

	/**
	 * 返回 primitive long 表示的 yyyyMMddHHmmss
	 * 
	 * @return
	 */
	public final static long datetimeOfSecond() {
		return datetimeOfSecond(LocalDateTime.now());
	}

	/**
	 * 根据指定 LocalDateTime 返回 primitive long 表示的 yyyyMMddHHmmss
	 * 
	 * @param dateTime
	 * @return
	 */
	public final static long datetimeOfSecond(@Nonnull LocalDateTime dateTime) {
		return date(dateTime.toLocalDate()) * 1000000L + timeOfSecond(dateTime.toLocalTime());
	}

	/**
	 * 返回 primitive long 表示的 yyyyMMddHHmmssSSS
	 * 
	 * @return
	 */
	public final static long datetimeOfMillisecond() {
		return datetimeOfMillisecond(LocalDateTime.now());
	}

	/**
	 * 根据指定 LocalDateTime 返回 primitive long 表示的 yyyyMMddHHmmssSSS<br>
	 * year 不可超过 922337
	 * 
	 * @param dateTime
	 * @return
	 */
	public final static long datetimeOfMillisecond(@Nonnull LocalDateTime dateTime) {
		return datetimeOfSecond(dateTime) * 1000L + dateTime.toLocalTime().getNano() / 1000000;
	}

	/**
	 * primitive int yyyyMMdd 转换为 LocalDate
	 * 
	 * @param date
	 * @return
	 */
	public final static LocalDate toLocalDate(int date) {
		return LocalDate.of(date / 10000, (date % 10000) / 100, date % 100);
	}

	/**
	 * primitive int HHmmss 转换为 LocalTime
	 * 
	 * @param date
	 * @return
	 */
	public final static LocalTime toLocalTime(int time) {
		return LocalTime.of(time / 10000000, (time % 10000000) / 100000, (time % 100000) / 1000,
				(time % 1000) * 1000000);
	}

	/**
	 * primitive long yyyyMMddHHmmss 转换为 LocalDateTime
	 * 
	 * @param date
	 * @return
	 */
	public final static LocalDateTime toLocalDateTime(long datetime) {
		return LocalDateTime.of(toLocalDate((int) (datetime / 1000000000)), toLocalTime((int) (datetime % 1000000000)));
	}

	/**
	 * 
	 * @param pattern
	 * @param str
	 * @return
	 * @throws IllegalArgumentException
	 * @throws DateTimeParseException
	 */
	public final static LocalDate toLocalDate(@Nonnull DatePattern pattern, @Nonnull String str)
			throws IllegalArgumentException, DateTimeParseException {
		checkFormatParam(pattern, str);
		return LocalDate.parse(str, pattern.getFormatter());
	}

	/**
	 * 
	 * @param pattern
	 * @param str
	 * @return
	 * @throws IllegalArgumentException
	 * @throws DateTimeParseException
	 */
	public final static LocalTime toLocalTime(@Nonnull TimePattern pattern, @Nonnull String str)
			throws IllegalArgumentException, DateTimeParseException {
		checkFormatParam(pattern, str);
		return LocalTime.parse(str, pattern.getFormatter());
	}

	/**
	 * 
	 * @param pattern
	 * @param str
	 * @return
	 * @throws IllegalArgumentException
	 * @throws DateTimeParseException
	 */
	public final static LocalDateTime toLocalDateTime(@Nonnull DateTimePattern pattern, @Nonnull String str)
			throws IllegalArgumentException, DateTimeParseException {
		checkFormatParam(pattern, str);
		return LocalDateTime.parse(str, pattern.getFormatter());
	}

	private static void checkFormatParam(TemporalPattern pattern, String str) {
		if (pattern == null)
			throw new IllegalArgumentException("pattern cannot null");
		if (StringUtil.isNullOrEmpty(str))
			throw new IllegalArgumentException("str cannot null or empty.");
		if (str.length() != pattern.getPattern().length())
			throw new IllegalArgumentException("str and pattern length no match.");
	}

	/**
	 * 
	 * @param startDate
	 * @return
	 */
	public final static long durationByDay(@Nonnull LocalDate startDate) {
		return durationByDay(startDate, LocalDate.now());
	}

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public final static long durationByDay(@Nonnull LocalDate startDate, @Nonnull LocalDate endDate) {
		return endDate.toEpochDay() - startDate.toEpochDay();
	}

	/**
	 * 
	 * @param startTime
	 * @return
	 */
	public final static long durationBySecond(@Nonnull LocalTime startTime) {
		return durationBySecond(startTime, LocalTime.now());
	}

	/**
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public final static long durationBySecond(@Nonnull LocalTime startTime, @Nonnull LocalTime endTime) {
		return endTime.toSecondOfDay() - startTime.toSecondOfDay();
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public final static LocalDateTime toLocalDateTime(@Nonnull Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), TimeZone.SYS_DEFAULT);
	}

	/**
	 * 
	 * @param date
	 * @param zoneId
	 * @return
	 */
	public final static LocalDateTime toLocalDateTime(@Nonnull Date date, @Nonnull ZoneId zoneId) {
		return LocalDateTime.ofInstant(date.toInstant(), zoneId);
	}

	/**
	 * 
	 * @param pattern
	 * @return
	 */
	public final static String fmtDate(@Nonnull DatePattern pattern) {
		return pattern.getFormatter().format(LocalDate.now());
	}

	/**
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public final static String fmtDate(@Nonnull DatePattern pattern, @Nonnull LocalDate date) {
		return pattern.getFormatter().format(date);
	}

	/**
	 * 
	 * @param pattern
	 * @return
	 */
	public final static String fmtTime(@Nonnull TimePattern pattern) {
		return pattern.getFormatter().format(LocalTime.now());
	}

	/**
	 * 
	 * @param time
	 * @param pattern
	 * @return
	 */
	public final static String fmtTime(@Nonnull TimePattern pattern, @Nonnull LocalTime time) {
		return pattern.getFormatter().format(time);
	}

	/**
	 * 
	 * @param datetime
	 * @param pattern
	 * @return
	 */
	public final static String fmtDateTime(@Nonnull DateTimePattern pattern, @Nonnull LocalDateTime datetime) {
		return pattern.getFormatter().format(datetime);
	}

	/**
	 * 
	 * @param pattern
	 * @return
	 */
	public final static String fmtDateTime(@Nonnull DateTimePattern pattern) {
		return pattern.getFormatter().format(LocalDateTime.now());
	}

	/**
	 * 
	 * @return
	 */
	public static LocalDate currentDate() {
		return LocalDate.now();
	}

	/**
	 * 
	 * @return
	 */
	public static LocalDate previousDate() {
		return previousDate(LocalDate.now());
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static LocalDate previousDate(LocalDate date) {
		return date.minusDays(1);
	}

	/**
	 * 
	 * @return
	 */
	public static LocalDate nextDate() {
		return nextDate(LocalDate.now());
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static LocalDate nextDate(LocalDate date) {
		return date.plusDays(1);
	}

	public static void main(String[] args) {

		LocalDateTime dateTime = LocalDateTime.now();
		System.out.println(Integer.MAX_VALUE);
		System.out.println(Long.MAX_VALUE);
		System.out.println(date(dateTime.toLocalDate()));
		System.out.println(timeOfHour(dateTime.toLocalTime()));
		System.out.println(timeOfMinute(dateTime.toLocalTime()));
		System.out.println(timeOfSecond(dateTime.toLocalTime()));
		System.out.println(timeOfMillisecond(dateTime.toLocalTime()));
		System.out.println(timeOfMicrosecond(dateTime.toLocalTime()));
		System.out.println(timeOfNanosecond(dateTime.toLocalTime()));
		System.out.println(datetimeOfHour(dateTime));
		System.out.println(datetimeOfMinute(dateTime));
		System.out.println(datetimeOfSecond(dateTime));
		System.out.println(datetimeOfMillisecond(dateTime));
		System.out.println(toLocalDate(20161223));
		System.out.println(toLocalTime(234554987));
		System.out.println(toLocalDateTime(20161223234554987L));
		System.out.println(yearDay());
		System.out.println(LocalDate.now());
		System.out.println(LocalDate.ofEpochDay(System.currentTimeMillis() / (24 * 60 * 60 * 1000)));
		System.out.println(System.currentTimeMillis() / (24 * 60 * 60 * 1000));

	}

}

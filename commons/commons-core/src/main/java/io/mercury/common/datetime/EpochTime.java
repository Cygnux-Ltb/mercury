package io.mercury.common.datetime;

import static io.mercury.common.datetime.TimeConst.MICROS_PER_DAY;
import static io.mercury.common.datetime.TimeConst.MICROS_PER_MILLIS;
import static io.mercury.common.datetime.TimeConst.MICROS_PER_SECONDS;
import static io.mercury.common.datetime.TimeConst.MILLIS_PER_DAY;
import static io.mercury.common.datetime.TimeConst.MILLIS_PER_HOUR;
import static io.mercury.common.datetime.TimeConst.MILLIS_PER_MINUTE;
import static io.mercury.common.datetime.TimeConst.MILLIS_PER_SECONDS;
import static io.mercury.common.datetime.TimeConst.NANOS_PER_MICROS;
import static io.mercury.common.datetime.TimeConst.NANOS_PER_MILLIS;
import static io.mercury.common.datetime.TimeConst.SECONDS_PER_MINUTE;
import static io.mercury.common.datetime.TimeZone.SYS_DEFAULT;
import static io.mercury.common.datetime.TimeZone.SYS_DEFAULT_OFFSET;
import static java.lang.System.currentTimeMillis;

import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.annotation.Nonnull;;

public final class EpochTime {

	/**
	 * 
	 * @return
	 */
	public static final long day() {
		return currentTimeMillis() / MILLIS_PER_DAY;
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static final long day(@Nonnull LocalDate date) {
		return date.toEpochDay();
	}

	/**
	 * 
	 * @param dateTime
	 * @return
	 */
	public static final long day(@Nonnull LocalDateTime dateTime) {
		return day(dateTime.toLocalDate());
	}

	/**
	 * 
	 * @param dateTime
	 * @return
	 */
	public static final long day(@Nonnull ZonedDateTime dateTime) {
		return day(dateTime.toLocalDate());
	}

	/**
	 * 
	 * @return
	 */
	public static final long hour() {
		return currentTimeMillis() / MILLIS_PER_HOUR;
	}

	/**
	 * 
	 * @param datetime
	 * @return
	 */
	public static final long hour(@Nonnull LocalDateTime datetime) {
		return datetime.toEpochSecond(SYS_DEFAULT_OFFSET) / TimeConst.SECONDS_PER_HOUR;
	}

	/**
	 * 
	 * @param datetime
	 * @param offset
	 * @return
	 */
	public static final long hour(@Nonnull LocalDateTime datetime, @Nonnull ZoneOffset offset) {
		return datetime.toEpochSecond(offset) / TimeConst.SECONDS_PER_HOUR;
	}

	/**
	 * 
	 * @param dateTime
	 * @return
	 */
	public static final long hour(@Nonnull ZonedDateTime dateTime) {
		return dateTime.toEpochSecond() / TimeConst.SECONDS_PER_HOUR;
	}

	/**
	 * 
	 * @return
	 */
	public static final long minute() {
		return currentTimeMillis() / MILLIS_PER_MINUTE;
	}

	/**
	 * 
	 * @param datetime
	 * @return
	 */
	public static final long minute(@Nonnull LocalDateTime datetime) {
		return minute(datetime, SYS_DEFAULT_OFFSET);
	}

	/**
	 * 
	 * @param datetime
	 * @param offset
	 * @return
	 */
	public static final long minute(@Nonnull LocalDateTime datetime, @Nonnull ZoneOffset offset) {
		return datetime.toEpochSecond(offset);
	}

	/**
	 * 
	 * @param dateTime
	 * @return
	 */
	public static final long minute(@Nonnull ZonedDateTime dateTime) {
		return dateTime.toEpochSecond() / SECONDS_PER_MINUTE;
	}

	/**
	 * 
	 * @return
	 */
	public static final long seconds() {
		return currentTimeMillis() / MILLIS_PER_SECONDS;
	}

	/**
	 * 
	 * @param datetime
	 * @return
	 */
	public static final long seconds(@Nonnull LocalDateTime datetime) {
		return datetime.toEpochSecond(SYS_DEFAULT_OFFSET);
	}

	/**
	 * 
	 * @param datetime
	 * @param offset
	 * @return
	 */
	public static final long seconds(@Nonnull LocalDateTime datetime, @Nonnull ZoneOffset offset) {
		return datetime.toEpochSecond(offset);
	}

	/**
	 * 
	 * @param dateTime
	 * @return
	 */
	public static final long seconds(@Nonnull ZonedDateTime dateTime) {
		return dateTime.toEpochSecond();
	}

	/**
	 * 
	 * @return
	 */
	public static final long millis() {
		return currentTimeMillis();
	}

	/**
	 * 
	 * @param datetime
	 * @return
	 */
	public static final long millis(@Nonnull ZonedDateTime datetime) {
		return datetime.toEpochSecond() * MILLIS_PER_SECONDS + datetime.getNano() / NANOS_PER_MILLIS;
	}

	/**
	 * 
	 * @param datetime
	 * @return
	 */
	public static final long millis(@Nonnull LocalDateTime datetime) {
		return datetime.toLocalDate().toEpochDay() * MILLIS_PER_DAY
				+ datetime.toLocalTime().toSecondOfDay() * MILLIS_PER_SECONDS + datetime.getNano() / NANOS_PER_MILLIS
				- SYS_DEFAULT_OFFSET.getTotalSeconds() * MILLIS_PER_SECONDS;
	}

	/**
	 * 
	 * @param datetime
	 * @param zoneOffset
	 * @return
	 */
	public static final long millis(@Nonnull LocalDateTime datetime, @Nonnull ZoneOffset zoneOffset) {
		return datetime.toLocalDate().toEpochDay() * MILLIS_PER_DAY
				+ datetime.toLocalTime().toSecondOfDay() * MILLIS_PER_SECONDS + datetime.getNano() / NANOS_PER_MILLIS
				- zoneOffset.getTotalSeconds() * MILLIS_PER_SECONDS;
	}

	/**
	 * 
	 * @return
	 */
	public static final long micros() {
		return currentTimeMillis() * MICROS_PER_MILLIS;
	}

	/**
	 * 
	 * @param datetime
	 * @return
	 */
	public static final long micros(@Nonnull ZonedDateTime datetime) {
		return datetime.toEpochSecond() * MICROS_PER_SECONDS + datetime.getNano() / NANOS_PER_MICROS;
	}

	/**
	 * 
	 * @param datetime
	 * @return
	 */
	public static final long micros(@Nonnull LocalDateTime datetime) {
		return datetime.toLocalDate().toEpochDay() * MICROS_PER_DAY
				+ datetime.toLocalTime().toSecondOfDay() * MICROS_PER_SECONDS + datetime.getNano() / NANOS_PER_MICROS
				- SYS_DEFAULT_OFFSET.getTotalSeconds() * MICROS_PER_SECONDS;
	}

	/**
	 * 
	 * @param datetime
	 * @param zoneOffset
	 * @return
	 */
	public static final long micros(@Nonnull LocalDateTime datetime, @Nonnull ZoneOffset zoneOffset) {
		return datetime.toLocalDate().toEpochDay() * MICROS_PER_DAY
				+ datetime.toLocalTime().toSecondOfDay() * MICROS_PER_SECONDS + datetime.getNano() / NANOS_PER_MICROS
				- zoneOffset.getTotalSeconds() * MICROS_PER_SECONDS;
	}

	/**
	 * 
	 * @param seconds
	 * @return
	 */
	public static final ZonedDateTime ofEpochSeconds(long seconds) {
		return ofEpochSeconds(seconds, SYS_DEFAULT);
	}

	/**
	 * 
	 * @param seconds
	 * @param zoneId
	 * @return
	 */
	public static final ZonedDateTime ofEpochSeconds(long seconds, ZoneId zoneId) {
		return ZonedDateTime.ofInstant(Instant.ofEpochSecond(seconds), zoneId);
	}

	/**
	 * 
	 * @param millis
	 * @return
	 */
	public static final ZonedDateTime ofEpochMillis(long millis) {
		return ofEpochMillis(millis, SYS_DEFAULT);
	}

	/**
	 * 
	 * @param millis
	 * @param zoneId
	 * @return
	 */
	public static final ZonedDateTime ofEpochMillis(long millis, ZoneId zoneId) {
		return ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), zoneId);
	}

	public static void main(String[] args) {

		long epoch = currentTimeMillis();
		LocalDateTime now = LocalDateTime.now();
		ZoneOffset offset = ZonedDateTime.now().getOffset();

		System.out.println(offset.getId() + "-" + offset.getTotalSeconds());
		System.out.println(epoch);
		System.out.println();
		System.out.println(seconds(now));
		System.out.println(now.toEpochSecond(offset));
		System.out.println();
		System.out.println(millis());
		System.out.println(millis(now));
		System.out.println(millis(now, offset));
		System.out.println();
		System.out.println(micros());
		System.out.println(micros(now));
		System.out.println(micros(now, offset));
		System.out.println(hour());

		Charset.availableCharsets().entrySet().stream().forEach(entity -> System.out.println(entity));

	}

}

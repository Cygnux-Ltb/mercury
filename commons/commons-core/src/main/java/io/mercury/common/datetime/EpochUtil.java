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
import static io.mercury.common.datetime.TimeConst.SECONDS_PER_HOUR;
import static io.mercury.common.datetime.TimeConst.SECONDS_PER_MINUTE;
import static io.mercury.common.datetime.TimeZone.SYS_DEFAULT;
import static java.lang.System.currentTimeMillis;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.annotation.Nonnull;;

public final class EpochUtil {

	/**
	 * EpochTime Zero Point : UTC 1970-01-01 00:00:00.0000
	 */
	public static final ZonedDateTime ZeroPoint = ZonedDateTime.ofInstant(Instant.EPOCH, TimeZone.UTC);

	private static final long NANOS_EPOCH_OFFSET;

	static {
		long millisEpoch = System.currentTimeMillis();
		long nanosPoint = System.nanoTime();
		long nanosEpoch = millisEpoch * NANOS_PER_MILLIS;
		NANOS_EPOCH_OFFSET = nanosEpoch - nanosPoint;
	}

	/**
	 * 
	 * @return
	 */
	public static final long getEpochDays() {
		return currentTimeMillis() / MILLIS_PER_DAY;
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static final long getEpochDays(@Nonnull LocalDate date) {
		return date.toEpochDay();
	}

	/**
	 * 
	 * @param dateTime
	 * @return
	 */
	public static final long getEpochDays(@Nonnull LocalDateTime dateTime) {
		return dateTime.toLocalDate().toEpochDay();
	}

	/**
	 * 
	 * @param dateTime
	 * @return
	 */
	public static final long getEpochDays(@Nonnull ZonedDateTime dateTime) {
		return dateTime.toLocalDate().toEpochDay();
	}

	/**
	 * 
	 * @return
	 */
	public static final long getEpochHours() {
		return currentTimeMillis() / MILLIS_PER_HOUR;
	}

	/**
	 * 
	 * @param datetime
	 * @return
	 */
	public static final long getEpochHours(@Nonnull LocalDateTime datetime) {
		return datetime.toEpochSecond(SYS_DEFAULT) / SECONDS_PER_HOUR;
	}

	/**
	 * 
	 * @param datetime
	 * @param offset
	 * @return
	 */
	public static final long getEpochHours(@Nonnull LocalDateTime datetime, @Nonnull ZoneOffset offset) {
		return datetime.toEpochSecond(offset) / SECONDS_PER_HOUR;
	}

	/**
	 * 
	 * @param dateTime
	 * @return
	 */
	public static final long getEpochHours(@Nonnull ZonedDateTime dateTime) {
		return dateTime.toEpochSecond() / SECONDS_PER_HOUR;
	}

	/**
	 * 
	 * @return
	 */
	public static final long getEpochMinutes() {
		return currentTimeMillis() / MILLIS_PER_MINUTE;
	}

	/**
	 * 
	 * @param datetime
	 * @return
	 */
	public static final long getEpochMinutes(@Nonnull LocalDateTime datetime) {
		return getEpochMinutes(datetime, SYS_DEFAULT);
	}

	/**
	 * 
	 * @param datetime
	 * @param offset
	 * @return
	 */
	public static final long getEpochMinutes(@Nonnull LocalDateTime datetime, @Nonnull ZoneOffset offset) {
		return datetime.toEpochSecond(offset) / SECONDS_PER_MINUTE;
	}

	/**
	 * 
	 * @param dateTime
	 * @return
	 */
	public static final long getEpochMinutes(@Nonnull ZonedDateTime dateTime) {
		return dateTime.toEpochSecond() / SECONDS_PER_MINUTE;
	}

	/**
	 * 
	 * @return
	 */
	public static final long getEpochSeconds() {
		return currentTimeMillis() / MILLIS_PER_SECONDS;
	}

	/**
	 * 
	 * @param datetime
	 * @return
	 */
	public static final long getEpochSeconds(@Nonnull LocalDateTime datetime) {
		return datetime.toEpochSecond(SYS_DEFAULT);
	}

	/**
	 * 
	 * @param datetime
	 * @param offset
	 * @return
	 */
	public static final long getEpochSeconds(@Nonnull LocalDateTime datetime, @Nonnull ZoneOffset offset) {
		return datetime.toEpochSecond(offset);
	}

	/**
	 * 
	 * @param dateTime
	 * @return
	 */
	public static final long getEpochSeconds(@Nonnull ZonedDateTime dateTime) {
		return dateTime.toEpochSecond();
	}

	/**
	 * 
	 * @return
	 */
	public static final long getEpochMillis() {
		return currentTimeMillis();
	}

	/**
	 * 
	 * @param datetime
	 * @return
	 */
	public static final long getEpochMillis(@Nonnull ZonedDateTime datetime) {
		return datetime.toEpochSecond() * MILLIS_PER_SECONDS + datetime.getNano() / NANOS_PER_MILLIS;
	}

	/**
	 * 
	 * @param datetime
	 * @return
	 */
	public static final long getEpochMillis(@Nonnull LocalDateTime datetime) {
		return datetime.toLocalDate().toEpochDay() * MILLIS_PER_DAY
				+ datetime.toLocalTime().toSecondOfDay() * MILLIS_PER_SECONDS + datetime.getNano() / NANOS_PER_MILLIS
				- SYS_DEFAULT.getTotalSeconds() * MILLIS_PER_SECONDS;
	}

	/**
	 * 
	 * @param datetime
	 * @param offset
	 * @return
	 */
	public static final long getEpochMillis(@Nonnull LocalDateTime datetime, @Nonnull ZoneOffset offset) {
		return datetime.toLocalDate().toEpochDay() * MILLIS_PER_DAY
				+ datetime.toLocalTime().toSecondOfDay() * MILLIS_PER_SECONDS + datetime.getNano() / NANOS_PER_MILLIS
				- offset.getTotalSeconds() * MILLIS_PER_SECONDS;
	}

	/**
	 * 
	 * @return
	 */
	public static final long getEpochMicros() {
		return currentTimeMillis() * MICROS_PER_MILLIS;
	}

	/**
	 * 
	 * @param datetime
	 * @return
	 */
	public static final long getEpochMicros(@Nonnull ZonedDateTime datetime) {
		return datetime.toEpochSecond() * MICROS_PER_SECONDS + datetime.getNano() / NANOS_PER_MICROS;
	}

	/**
	 * 
	 * @param datetime
	 * @return
	 */
	public static final long getEpochMicros(@Nonnull LocalDateTime datetime) {
		return datetime.toLocalDate().toEpochDay() * MICROS_PER_DAY
				+ datetime.toLocalTime().toSecondOfDay() * MICROS_PER_SECONDS + datetime.getNano() / NANOS_PER_MICROS
				- SYS_DEFAULT.getTotalSeconds() * MICROS_PER_SECONDS;
	}

	/**
	 * 
	 * @param datetime
	 * @param offset
	 * @return
	 */
	public static final long getEpochMicros(@Nonnull LocalDateTime datetime, @Nonnull ZoneOffset offset) {
		return datetime.toLocalDate().toEpochDay() * MICROS_PER_DAY
				+ datetime.toLocalTime().toSecondOfDay() * MICROS_PER_SECONDS + datetime.getNano() / NANOS_PER_MICROS
				- offset.getTotalSeconds() * MICROS_PER_SECONDS;
	}

	/**
	 * @return
	 */
	public static final long getEpochNanos() {
		return System.nanoTime() + NANOS_EPOCH_OFFSET;
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

//		long epoch = currentTimeMillis();
//		LocalDateTime now = LocalDateTime.now();
//		ZoneOffset offset = ZonedDateTime.now().getOffset();
//
//		System.out.println(offset.getId() + "-" + offset.getTotalSeconds());
//		System.out.println(epoch);
//		System.out.println();
//		System.out.println(getEpochSeconds(now));
//		System.out.println(now.toEpochSecond(offset));
//		System.out.println();
//		System.out.println(getEpochMillis());
//		System.out.println(getEpochMillis(now));
//		System.out.println(getEpochMillis(now, offset));
//		System.out.println();
//		System.out.println(getEpochMicros());
//		System.out.println(getEpochMicros(now));
//		System.out.println(getEpochMicros(now, offset));
//		System.out.println(getEpochHours());

		long[] nss = new long[50];
		long ms = System.currentTimeMillis();
		for (int i = 0; i < nss.length; i++)
			nss[i] = getEpochNanos();

		System.out.println(ms);
		for (int i = 0; i < nss.length; i++)
			System.out.println(nss[i]);

	}

}

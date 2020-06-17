package io.mercury.common.datetime;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.annotation.Nonnull;

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
import static java.lang.System.currentTimeMillis;;

public final class EpochTime {

	public static final long day() {
		return currentTimeMillis() / MILLIS_PER_DAY;
	}

	public static final long day(@Nonnull LocalDate date) {
		return date.toEpochDay();
	}

	public static final long day(@Nonnull LocalDateTime dateTime) {
		return day(dateTime.toLocalDate());
	}

	public static final long day(@Nonnull ZonedDateTime zdateTime) {
		return day(zdateTime.toLocalDate());
	}

	public static final long hour() {
		return currentTimeMillis() / MILLIS_PER_HOUR;
	}

	public static final long hour(@Nonnull LocalDateTime datetime) {
		return datetime.toEpochSecond(SYS_DEFAULT_OFFSET) / TimeConst.SECONDS_PER_HOUR;
	}

	public static final long hour(@Nonnull LocalDateTime datetime, @Nonnull ZoneOffset offset) {
		return datetime.toEpochSecond(offset) / TimeConst.SECONDS_PER_HOUR;
	}

	public static final long hour(@Nonnull ZonedDateTime zdateTime) {
		return zdateTime.toEpochSecond() / TimeConst.SECONDS_PER_HOUR;
	}

	public static final long minute() {
		return currentTimeMillis() / MILLIS_PER_MINUTE;
	}

	public static final long minute(@Nonnull LocalDateTime datetime) {
		return minute(datetime, SYS_DEFAULT_OFFSET);
	}

	public static final long minute(@Nonnull LocalDateTime datetime, @Nonnull ZoneOffset offset) {
		return datetime.toEpochSecond(offset);
	}

	public static final long minute(@Nonnull ZonedDateTime zdateTime) {
		return zdateTime.toEpochSecond() / SECONDS_PER_MINUTE;
	}

	public static final long seconds() {
		return currentTimeMillis() / MILLIS_PER_SECONDS;
	}

	public static final long seconds(@Nonnull LocalDateTime datetime) {
		return datetime.toEpochSecond(SYS_DEFAULT_OFFSET);
	}

	public static final long seconds(@Nonnull LocalDateTime datetime, @Nonnull ZoneOffset offset) {
		return datetime.toEpochSecond(offset);
	}

	public static final long seconds(@Nonnull ZonedDateTime zdateTime) {
		return zdateTime.toEpochSecond();
	}

	public static final long millis() {
		return currentTimeMillis();
	}

	public static final long millis(@Nonnull ZonedDateTime datetime) {
		return datetime.toEpochSecond() * MILLIS_PER_SECONDS + datetime.getNano() / NANOS_PER_MILLIS;
	}

	public static final long millis(@Nonnull LocalDateTime datetime) {
		return datetime.toLocalDate().toEpochDay() * MILLIS_PER_DAY
				+ datetime.toLocalTime().toSecondOfDay() * MILLIS_PER_SECONDS + datetime.getNano() / NANOS_PER_MILLIS
				- SYS_DEFAULT_OFFSET.getTotalSeconds() * MILLIS_PER_SECONDS;
	}

	public static final long millis(@Nonnull LocalDateTime datetime, @Nonnull ZoneOffset zoneOffset) {
		return datetime.toLocalDate().toEpochDay() * MILLIS_PER_DAY
				+ datetime.toLocalTime().toSecondOfDay() * MILLIS_PER_SECONDS + datetime.getNano() / NANOS_PER_MILLIS
				- zoneOffset.getTotalSeconds() * MILLIS_PER_SECONDS;
	}

	public static final long micros() {
		return currentTimeMillis() * MICROS_PER_MILLIS;
	}

	public static final long micros(@Nonnull ZonedDateTime datetime) {
		return datetime.toEpochSecond() * MICROS_PER_SECONDS + datetime.getNano() / NANOS_PER_MICROS;
	}

	public static final long micros(@Nonnull LocalDateTime datetime) {
		return datetime.toLocalDate().toEpochDay() * MICROS_PER_DAY
				+ datetime.toLocalTime().toSecondOfDay() * MICROS_PER_SECONDS + datetime.getNano() / NANOS_PER_MICROS
				- SYS_DEFAULT_OFFSET.getTotalSeconds() * MICROS_PER_SECONDS;
	}

	public static final long micros(@Nonnull LocalDateTime datetime, @Nonnull ZoneOffset zoneOffset) {
		return datetime.toLocalDate().toEpochDay() * MICROS_PER_DAY
				+ datetime.toLocalTime().toSecondOfDay() * MICROS_PER_SECONDS + datetime.getNano() / NANOS_PER_MICROS
				- zoneOffset.getTotalSeconds() * MICROS_PER_SECONDS;
	}

	public static final ZonedDateTime ofEpochSeconds(long seconds) {
		return ofEpochSeconds(seconds, SYS_DEFAULT);
	}

	public static final ZonedDateTime ofEpochSeconds(long seconds, ZoneId zoneId) {
		return ZonedDateTime.ofInstant(Instant.ofEpochSecond(seconds), zoneId);
	}

	public static final ZonedDateTime ofEpochMillis(long millis) {
		return ofEpochMillis(millis, SYS_DEFAULT);
	}

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
		// Charset.availableCharsets().entrySet().stream().forEach(entity ->
		// System.out.println(entity));

	}

}

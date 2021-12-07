package io.mercury.common.datetime;

import static io.mercury.common.datetime.TimeConst.MICROS_PER_MILLIS;
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
import static java.lang.System.nanoTime;

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
	public static final ZonedDateTime EPOCH_ZERO = ZonedDateTime.ofInstant(Instant.EPOCH, TimeZone.UTC);

	private static final long NANOS_EPOCH_OFFSET;

	private static final long MICROS_EPOCH_OFFSET;

	static {
		// 当前Epoch毫秒数
		long millisEpoch = currentTimeMillis();
		// 当前系统纳秒数
		long baseline = nanoTime();
		// 当前Epoch纳秒数
		long nanosEpoch = millisEpoch * NANOS_PER_MILLIS;
		// 计算系统纳秒函数与Epoch函数的纳秒偏移量
		NANOS_EPOCH_OFFSET = nanosEpoch - baseline;
		// 当前Epoch微秒数
		long microsEpoch = millisEpoch * MICROS_PER_MILLIS;
		// 计算系统纳秒函数与Epoch函数的微秒偏移量
		MICROS_EPOCH_OFFSET = microsEpoch - (baseline / NANOS_PER_MICROS);
	}

	/**
	 * 获取 Epoch<b> 纳秒 </b>数
	 * 
	 * @return
	 */
	public static final long getEpochNanos() {
		return nanoTime() + NANOS_EPOCH_OFFSET;
	}

	/**
	 * 获取 Epoch<b> 微秒 </b>数
	 * 
	 * @return
	 */
	public static final long getEpochMicros() {
		return nanoTime() / NANOS_PER_MICROS + MICROS_EPOCH_OFFSET;
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
	 * @param datetime
	 * @return
	 */
	public static final long getEpochSeconds(@Nonnull ZonedDateTime datetime) {
		return datetime.toEpochSecond();
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
	 * @param datetime
	 * @return
	 */
	public static final long getEpochMinutes(@Nonnull ZonedDateTime datetime) {
		return datetime.toEpochSecond() / SECONDS_PER_MINUTE;
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
	 * @param datetime
	 * @return
	 */
	public static final long getEpochHours(@Nonnull ZonedDateTime datetime) {
		return datetime.toEpochSecond() / SECONDS_PER_HOUR;
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
	 * @param datetime
	 * @return
	 */
	public static final long getEpochDays(@Nonnull ZonedDateTime datetime) {
		return datetime.toLocalDate().toEpochDay();
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

		long[] mss = new long[50];
		long[] nss = new long[50];
		long ms = System.currentTimeMillis();
		for (int i = 0; i < nss.length; i++) {
			mss[i] = getEpochMicros();
			nss[i] = getEpochNanos();
		}

		System.out.println(ms);
		for (int i = 0; i < nss.length; i++) {
			System.out.println(mss[i]);
			System.out.println(nss[i]);
		}
	}

}

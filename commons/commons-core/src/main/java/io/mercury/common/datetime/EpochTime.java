package io.mercury.common.datetime;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

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
import static io.mercury.common.datetime.TimeZone.UTC;
import static io.mercury.common.lang.Asserter.nonNull;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.nanoTime;
import static java.time.Instant.EPOCH;

/**
 * <b>Epoch </b>获取工具
 *
 * @author yellow013
 */
public final class EpochTime {

    /**
     * EpochTime Zero Point : UTC 1970-01-01 00:00:00.0000
     */
    public static final ZonedDateTime EPOCH_ZERO = ZonedDateTime.ofInstant(EPOCH, UTC);

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
     * @param unit EpochUnit
     * @return long
     */
    public static long getEpochs(@Nonnull EpochUnit unit) {
        nonNull(unit, "unit");
        return switch (unit) {
            case SECOND -> getEpochSeconds();
            case MILLIS -> getEpochMillis();
            case MICROS -> getEpochMicros();
            case NANOS -> getEpochNanos();
        };
    }

    /**
     * 获取 Epoch<b> 纳秒 </b>数
     *
     * @return long
     */
    public static long getEpochNanos() {
        return nanoTime() + NANOS_EPOCH_OFFSET;
    }

    /**
     * 获取 Epoch<b> 微秒 </b>数
     *
     * @return long
     */
    public static long getEpochMicros() {
        return nanoTime() / NANOS_PER_MICROS + MICROS_EPOCH_OFFSET;
    }

    /**
     * 获取 Epoch<b> 毫秒 </b>数
     *
     * @return long
     */
    public static long getEpochMillis() {
        return currentTimeMillis();
    }

    /**
     * @param datetime ZonedDateTime
     * @return long
     */
    public static long getEpochMillis(@Nonnull ZonedDateTime datetime) {
        return datetime.toEpochSecond() * MILLIS_PER_SECONDS + datetime.getNano() / NANOS_PER_MILLIS;
    }

    /**
     * @param datetime LocalDateTime
     * @return long
     */
    public static long getEpochMillis(@Nonnull LocalDateTime datetime) {
        return datetime.toLocalDate().toEpochDay() * MILLIS_PER_DAY
                + (long) datetime.toLocalTime().toSecondOfDay() * MILLIS_PER_SECONDS
                + datetime.getNano() / NANOS_PER_MILLIS
                - (long) SYS_DEFAULT.getTotalSeconds() * MILLIS_PER_SECONDS;
    }

    /**
     * @param datetime LocalDateTime
     * @param offset   ZoneOffset
     * @return long
     */
    public static long getEpochMillis(@Nonnull LocalDateTime datetime,
                                      @Nonnull ZoneOffset offset) {
        return datetime.toLocalDate().toEpochDay() * MILLIS_PER_DAY
                + (long) datetime.toLocalTime().toSecondOfDay() * MILLIS_PER_SECONDS
                + datetime.getNano() / NANOS_PER_MILLIS
                - (long) offset.getTotalSeconds() * MILLIS_PER_SECONDS;
    }

    /**
     * @return long
     */
    public static long getEpochSeconds() {
        return currentTimeMillis() / MILLIS_PER_SECONDS;
    }

    /**
     * @param datetime LocalDateTime
     * @return long
     */
    public static long getEpochSeconds(@Nonnull LocalDateTime datetime) {
        return datetime.toEpochSecond(SYS_DEFAULT);
    }

    /**
     * @param datetime LocalDateTime
     * @param offset   ZoneOffset
     * @return long
     */
    public static long getEpochSeconds(@Nonnull LocalDateTime datetime,
                                       @Nonnull ZoneOffset offset) {
        return datetime.toEpochSecond(offset);
    }

    /**
     * @param datetime ZonedDateTime
     * @return long
     */
    public static long getEpochSeconds(@Nonnull ZonedDateTime datetime) {
        return datetime.toEpochSecond();
    }

    /**
     * @return long
     */
    public static long getEpochMinutes() {
        return currentTimeMillis() / MILLIS_PER_MINUTE;
    }

    /**
     * @param datetime LocalDateTime
     * @return long
     */
    public static long getEpochMinutes(@Nonnull LocalDateTime datetime) {
        return getEpochMinutes(datetime, SYS_DEFAULT);
    }

    /**
     * @param datetime LocalDateTime
     * @param offset   ZoneOffset
     * @return long
     */
    public static long getEpochMinutes(@Nonnull LocalDateTime datetime,
                                       @Nonnull ZoneOffset offset) {
        return datetime.toEpochSecond(offset) / SECONDS_PER_MINUTE;
    }

    /**
     * @param datetime ZonedDateTime
     * @return long
     */
    public static long getEpochMinutes(@Nonnull ZonedDateTime datetime) {
        return datetime.toEpochSecond() / SECONDS_PER_MINUTE;
    }

    /**
     * @return long
     */
    public static long getEpochHours() {
        return currentTimeMillis() / MILLIS_PER_HOUR;
    }

    /**
     * @param datetime LocalDateTime
     * @return long
     */
    public static long getEpochHours(@Nonnull LocalDateTime datetime) {
        return datetime.toEpochSecond(SYS_DEFAULT) / SECONDS_PER_HOUR;
    }

    /**
     * @param datetime LocalDateTime
     * @param offset   ZoneOffset
     * @return long
     */
    public static long getEpochHours(@Nonnull LocalDateTime datetime,
                                     @Nonnull ZoneOffset offset) {
        return datetime.toEpochSecond(offset) / SECONDS_PER_HOUR;
    }

    /**
     * @param datetime ZonedDateTime
     * @return long
     */
    public static long getEpochHours(@Nonnull ZonedDateTime datetime) {
        return datetime.toEpochSecond() / SECONDS_PER_HOUR;
    }

    /**
     * @return long
     */
    public static long getEpochDays() {
        return currentTimeMillis() / MILLIS_PER_DAY;
    }

    /**
     * @param date LocalDate
     * @return long
     */
    public static long getEpochDays(@Nonnull LocalDate date) {
        return date.toEpochDay();
    }

    /**
     * @param dateTime LocalDateTime
     * @return long
     */
    public static long getEpochDays(@Nonnull LocalDateTime dateTime) {
        return dateTime.toLocalDate().toEpochDay();
    }

    /**
     * @param datetime ZonedDateTime
     * @return long
     */
    public static long getEpochDays(@Nonnull ZonedDateTime datetime) {
        return datetime.toLocalDate().toEpochDay();
    }

    /**
     * @param millis long
     * @return long
     */
    public static ZonedDateTime ofEpochMillis(long millis) {
        return ofEpochMillis(millis, SYS_DEFAULT);
    }

    /**
     * @param millis long
     * @param zoneId ZoneId
     * @return long
     */
    public static ZonedDateTime ofEpochMillis(long millis, @Nonnull ZoneId zoneId) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), zoneId);
    }

    /**
     * @param seconds long
     * @return long
     */
    public static ZonedDateTime ofEpochSeconds(long seconds) {
        return ofEpochSeconds(seconds, SYS_DEFAULT);
    }

    /**
     * @param seconds long
     * @param zoneId  ZoneId
     * @return long
     */
    public static ZonedDateTime ofEpochSeconds(long seconds,
                                               @Nonnull ZoneId zoneId) {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(seconds), zoneId);
    }


    public static void main(String[] args) {
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

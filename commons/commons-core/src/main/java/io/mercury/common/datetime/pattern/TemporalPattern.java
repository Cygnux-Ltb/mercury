package io.mercury.common.datetime.pattern;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static io.mercury.common.datetime.TimeZone.UTC;

public interface TemporalPattern {

    /**
     * @return the pattern string
     */
    String getPattern();

    /**
     * @return the DateTimeFormatter instance
     */
    DateTimeFormatter getFormatter();

    /**
     * @return the new <b> [java.time.format.DateTimeFormatter] </b> instance
     */
    default DateTimeFormatter newFormatter() {
        return DateTimeFormatter.ofPattern(getPattern());
    }

    /**
     * @return the new <b> [java.text.DateFormat] </b> instance
     */
    default DateFormat newDateFormat() {
        return new SimpleDateFormat(getPattern());
    }

    /**
     * @return String
     */
    default String now() {
        return fmt(LocalDateTime.now());
    }

    /**
     * @return String
     */
    default String utc() {
        return fmt(ZonedDateTime.now(UTC));
    }

    /**
     * @param datetime ZonedDateTime
     * @return String
     */
    default String fmt(ZonedDateTime datetime) {
        return fmt(datetime.toLocalDateTime());
    }

    /**
     * @param datetime LocalDateTime
     * @return String
     */
    default String fmt(LocalDateTime datetime) {
        return getFormatter().format(datetime);
    }

    /**
     * @param date LocalDate
     * @return String
     */
    default String fmt(LocalDate date) {
        return getFormatter().format(date);
    }

    /**
     * @param time LocalTime
     * @return String
     */
    default String fmt(LocalTime time) {
        return getFormatter().format(time);
    }

}

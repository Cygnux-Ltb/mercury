package io.mercury.common.datetime.pattern;

import static io.mercury.common.datetime.TimeZone.UTC;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

public interface TemporalPattern {

	/**
	 * 
	 * @return the pattern string
	 */
	String getPattern();

	/**
	 * 
	 * @return the DateTimeFormatter instance
	 */
	DateTimeFormatter getFormatter();

	/**
	 * 
	 * @return the new <b> [java.time.format.DateTimeFormatter] </b> instance
	 */
	default DateTimeFormatter newDateTimeFormatter() {
		return DateTimeFormatter.ofPattern(getPattern());
	}

	/**
	 * 
	 * @return the new <b> [java.text.DateFormat] </b> instance
	 */
	default DateFormat newDateFormat() {
		return new SimpleDateFormat(getPattern());
	}

	/**
	 * 
	 * @param temporal
	 * @return
	 */
	default String format(Temporal temporal) {
		return getFormatter().format(temporal);
	}

	/**
	 * 
	 * @return
	 */
	default String now() {
		return getFormatter().format(LocalDateTime.now());
	}

	/**
	 * 
	 * @return
	 */
	default String nowWithUTC() {
		return getFormatter().format(ZonedDateTime.now(UTC).toLocalDateTime());
	}

}

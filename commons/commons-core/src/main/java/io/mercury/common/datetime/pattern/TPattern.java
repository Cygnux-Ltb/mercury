package io.mercury.common.datetime.pattern;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

public interface TPattern {

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
	 * @return the new <b> [java.time.format.DateTimeFormatter] </b> object
	 */
	DateTimeFormatter newDateTimeFormatter();

	/**
	 * 
	 * @return the new <b> [java.text.SimpleDateFormat.SimpleDateFormat] </b> object
	 */
	SimpleDateFormat newSimpleDateFormat();

	/**
	 * 
	 * @param temporal
	 * @return
	 */
	String format(Temporal temporal);

}

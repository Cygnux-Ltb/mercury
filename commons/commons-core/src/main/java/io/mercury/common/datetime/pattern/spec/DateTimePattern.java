package io.mercury.common.datetime.pattern;

/**
 * 日期时间格式列表
 * 
 * @author yellow013
 *
 */
public final class DateTimePattern extends AbstractPattern {

	private static final String LINE = "-";
	private static final String BLANK = " ";
	private static final String TIME = "T";

	/**
	 * Example: 2018031413
	 */
	public final static DateTimePattern YYYYMMDDHH = new DateTimePattern(
			DatePattern.YYYYMMDD.getPattern() + TimePattern.HH.getPattern());

	/**
	 * Example: 201803141314
	 */
	public final static DateTimePattern YYYYMMDDHHMM = new DateTimePattern(
			DatePattern.YYYYMMDD.getPattern() + TimePattern.HHMM.getPattern());

	/**
	 * Example: 20180314131423
	 */
	public final static DateTimePattern YYYYMMDDHHMMSS = new DateTimePattern(
			DatePattern.YYYYMMDD.getPattern() + TimePattern.HHMMSS.getPattern());

	/**
	 * Example: 20180314131423678
	 */
	public final static DateTimePattern YYYYMMDDHHMMSSSSS = new DateTimePattern(
			DatePattern.YYYYMMDD.getPattern() + TimePattern.HHMMSSSSS.getPattern());

	/**
	 * =============================================================================================
	 */

	/**
	 * Example: 20180314 131423
	 */
	public final static DateTimePattern YYYYMMDD_HHMMSS = new DateTimePattern(
			DatePattern.YYYYMMDD.getPattern() + BLANK + TimePattern.HHMMSS.getPattern());

	/**
	 * Example: 20180314 131423678
	 */
	public final static DateTimePattern YYYYMMDD_HHMMSSSSS = new DateTimePattern(
			DatePattern.YYYYMMDD.getPattern() + BLANK + TimePattern.HHMMSSSSS.getPattern());

	/**
	 * Example: 20180314 131423678789
	 */
	public final static DateTimePattern YYYYMMDD_HHMMSSSSSSSS = new DateTimePattern(
			DatePattern.YYYYMMDD.getPattern() + BLANK + TimePattern.HHMMSSSSSSSS.getPattern());

	/**
	 * Example: 20180314-131423
	 */
	public final static DateTimePattern YYYYMMDD_L_HHMMSS = new DateTimePattern(
			DatePattern.YYYYMMDD.getPattern() + LINE + TimePattern.HHMMSS.getPattern());

	/**
	 * Example: 20180314-131423678
	 */
	public final static DateTimePattern YYYYMMDD_L_HHMMSSSSS = new DateTimePattern(
			DatePattern.YYYYMMDD.getPattern() + LINE + TimePattern.HHMMSSSSS.getPattern());

	/**
	 * Example: 20180314-131423678789
	 */
	public final static DateTimePattern YYYYMMDD_L_HHMMSSSSSSSS = new DateTimePattern(
			DatePattern.YYYYMMDD.getPattern() + LINE + TimePattern.HHMMSSSSSSSS.getPattern());

	/**
	 * Example: 20180314T131423
	 */
	public final static DateTimePattern YYYYMMDD_T_HHMMSS = new DateTimePattern(
			DatePattern.YYYYMMDD.getPattern() + TIME + TimePattern.HHMMSS.getPattern());

	/**
	 * Example: 20180314T131423678
	 */
	public final static DateTimePattern YYYYMMDD_T_HHMMSSSSS = new DateTimePattern(
			DatePattern.YYYYMMDD.getPattern() + TIME + TimePattern.HHMMSSSSS.getPattern());

	/**
	 * Example: 20180314T131423678789
	 */
	public final static DateTimePattern YYYYMMDD_T_HHMMSSSSSSSS = new DateTimePattern(
			DatePattern.YYYYMMDD.getPattern() + TIME + TimePattern.HHMMSSSSSSSS.getPattern());

	/**
	 * =============================================================================================
	 */

	/**
	 * Example: 20180314 13:14:23
	 */
	public final static DateTimePattern YYYYMMDD_HH_MM_SS = new DateTimePattern(
			DatePattern.YYYYMMDD.getPattern() + BLANK + TimePattern.HH_MM_SS.getPattern());

	/**
	 * Example: 20180314 13:14:23.678
	 */
	public final static DateTimePattern YYYYMMDD_HH_MM_SS_SSS = new DateTimePattern(
			DatePattern.YYYYMMDD.getPattern() + BLANK + TimePattern.HH_MM_SS_SSS.getPattern());

	/**
	 * Example: 20180314T13:14:23.678789
	 */
	public final static DateTimePattern YYYYMMDD_HH_MM_SS_SSSSSS = new DateTimePattern(
			DatePattern.YYYYMMDD.getPattern() + BLANK + TimePattern.HH_MM_SS_SSSSSS.getPattern());

	/**
	 * Example: 20180314-13:14:23
	 */
	public final static DateTimePattern YYYYMMDD_L_HH_MM_SS = new DateTimePattern(
			DatePattern.YYYYMMDD.getPattern() + LINE + TimePattern.HH_MM_SS.getPattern());

	/**
	 * Example: 20180314-13:14:23.678
	 */
	public final static DateTimePattern YYYYMMDD_L_HH_MM_SS_SSS = new DateTimePattern(
			DatePattern.YYYYMMDD.getPattern() + LINE + TimePattern.HH_MM_SS_SSS.getPattern());

	/**
	 * Example: 20180314-13:14:23.678789
	 */
	public final static DateTimePattern YYYYMMDD_L_HH_MM_SS_SSSSSS = new DateTimePattern(
			DatePattern.YYYYMMDD.getPattern() + LINE + TimePattern.HH_MM_SS_SSSSSS.getPattern());

	/**
	 * Example: 20180314T13:14:23
	 */
	public final static DateTimePattern YYYYMMDD_T_HH_MM_SS = new DateTimePattern(
			DatePattern.YYYYMMDD.getPattern() + TIME + TimePattern.HH_MM_SS.getPattern());

	/**
	 * Example: 20180314T13:14:23.678
	 */
	public final static DateTimePattern YYYYMMDD_T_HH_MM_SS_SSS = new DateTimePattern(
			DatePattern.YYYYMMDD.getPattern() + TIME + TimePattern.HH_MM_SS_SSS.getPattern());

	/**
	 * Example: 20180314T13:14:23.567
	 */
	public final static DateTimePattern YYYYMMDD_T_HH_MM_SS_SSSSSS = new DateTimePattern(
			DatePattern.YYYYMMDD.getPattern() + TIME + TimePattern.HH_MM_SS_SSSSSS.getPattern());

	/**
	 * =============================================================================================
	 */

	/**
	 * Example: 2018-03-14 131423
	 */
	public final static DateTimePattern YYYY_MM_DD_HHMMSS = new DateTimePattern(
			DatePattern.YYYY_MM_DD.getPattern() + BLANK + TimePattern.HHMMSS.getPattern());

	/**
	 * Example: 2018-03-14 131423678
	 */
	public final static DateTimePattern YYYY_MM_DD_HHMMSSSSS = new DateTimePattern(
			DatePattern.YYYY_MM_DD.getPattern() + BLANK + TimePattern.HHMMSSSSS.getPattern());

	/**
	 * Example: 2018-03-14 131423678789
	 */
	public final static DateTimePattern YYYY_MM_DD_HHMMSSSSSSSS = new DateTimePattern(
			DatePattern.YYYY_MM_DD.getPattern() + BLANK + TimePattern.HHMMSSSSSSSS.getPattern());

	/**
	 * Example: 2018-03-14T131423
	 */
	public final static DateTimePattern YYYY_MM_DD_T_HHMMSS = new DateTimePattern(
			DatePattern.YYYY_MM_DD.getPattern() + TIME + TimePattern.HHMMSS.getPattern());

	/**
	 * Example: 2018-03-14T131423678
	 */
	public final static DateTimePattern YYYY_MM_DD_T_HHMMSSSSS = new DateTimePattern(
			DatePattern.YYYY_MM_DD.getPattern() + TIME + TimePattern.HHMMSSSSS.getPattern());

	/**
	 * Example: 2018-03-14T131423678789
	 */
	public final static DateTimePattern YYYY_MM_DD_T_HHMMSSSSSSSS = new DateTimePattern(
			DatePattern.YYYY_MM_DD.getPattern() + TIME + TimePattern.HHMMSSSSSSSS.getPattern());

	/**
	 * =============================================================================================
	 */

	/**
	 * Example: 2018-03-14 13:14:23
	 */
	public final static DateTimePattern YYYY_MM_DD_HH_MM_SS = new DateTimePattern(
			DatePattern.YYYY_MM_DD.getPattern() + BLANK + TimePattern.HH_MM_SS.getPattern());

	/**
	 * Example: 2018-03-14 13:14:23.678
	 */
	public final static DateTimePattern YYYY_MM_DD_HH_MM_SS_SSS = new DateTimePattern(
			DatePattern.YYYY_MM_DD.getPattern() + BLANK + TimePattern.HH_MM_SS_SSS.getPattern());

	/**
	 * Example: 2018-03-14 13:14:23.678789
	 */
	public final static DateTimePattern YYYY_MM_DD_HH_MM_SS_SSSSSS = new DateTimePattern(
			DatePattern.YYYY_MM_DD.getPattern() + BLANK + TimePattern.HH_MM_SS_SSSSSS.getPattern());

	/**
	 * Example: 2018-03-14T13:14:23
	 */
	public final static DateTimePattern YYYY_MM_DD_T_HH_MM_SS = new DateTimePattern(
			DatePattern.YYYY_MM_DD.getPattern() + TIME + TimePattern.HH_MM_SS.getPattern());

	/**
	 * Example: 2018-03-14T13:14:23.678
	 */
	public final static DateTimePattern YYYY_MM_DD_T_HH_MM_SS_SSS = new DateTimePattern(
			DatePattern.YYYY_MM_DD.getPattern() + TIME + TimePattern.HH_MM_SS_SSS.getPattern());

	/**
	 * Example: 2018-03-14T13:14:23.678789
	 */
	public final static DateTimePattern YYYY_MM_DD_T_HH_MM_SS_SSSSSS = new DateTimePattern(
			DatePattern.YYYY_MM_DD.getPattern() + TIME + TimePattern.HH_MM_SS_SSSSSS.getPattern());

	/**
	 * 
	 * @param pattern
	 */
	private DateTimePattern(String pattern) {
		super(pattern);
	}

}

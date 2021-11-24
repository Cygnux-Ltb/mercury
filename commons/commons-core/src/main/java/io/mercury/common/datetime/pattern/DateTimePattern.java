package io.mercury.common.datetime.pattern.spec;

import static io.mercury.common.datetime.pattern.DatetimeSeparator.BLANK;
import static io.mercury.common.datetime.pattern.DatetimeSeparator.LINE;
import static io.mercury.common.datetime.pattern.spec.DatePattern.YYYYMMDD;
import static io.mercury.common.datetime.pattern.spec.DatePattern.YYYY_MM_DD;
import static io.mercury.common.datetime.pattern.spec.TimePattern.HH;
import static io.mercury.common.datetime.pattern.spec.TimePattern.HHMM;
import static io.mercury.common.datetime.pattern.spec.TimePattern.HHMMSS;
import static io.mercury.common.datetime.pattern.spec.TimePattern.HHMMSSSSS;
import static io.mercury.common.datetime.pattern.spec.TimePattern.HHMMSSSSSSSS;
import static io.mercury.common.datetime.pattern.spec.TimePattern.HH_MM_SS;
import static io.mercury.common.datetime.pattern.spec.TimePattern.HH_MM_SS_SSS;
import static io.mercury.common.datetime.pattern.spec.TimePattern.HH_MM_SS_SSSSSS;

import io.mercury.common.datetime.pattern.AbstractPattern;;

/**
 * 常用日期时间格式列表
 * 
 * @author yellow013
 *
 */
public final class DateTimePattern extends AbstractPattern {

	/**
	 * Example: 2018031413
	 */
	public final static DateTimePattern YYYYMMDDHH = new DateTimePattern(
			YYYYMMDD.getPattern() + HH.getPattern());

	/**
	 * Example: 201803141314
	 */
	public final static DateTimePattern YYYYMMDDHHMM = new DateTimePattern(
			YYYYMMDD.getPattern() + HHMM.getPattern());

	/**
	 * Example: 20180314131423
	 */
	public final static DateTimePattern YYYYMMDDHHMMSS = new DateTimePattern(
			YYYYMMDD.getPattern() + HHMMSS.getPattern());

	/**
	 * Example: 20180314131423678
	 */
	public final static DateTimePattern YYYYMMDDHHMMSSSSS = new DateTimePattern(
			YYYYMMDD.getPattern() + HHMMSSSSS.getPattern());

	/**
	 * =============================================================================================
	 */

	/**
	 * Example: 20180314 131423
	 */
	public final static DateTimePattern YYYYMMDD_HHMMSS = new DateTimePattern(
			YYYYMMDD.getPattern() + BLANK + HHMMSS.getPattern());

	/**
	 * Example: 20180314 131423678
	 */
	public final static DateTimePattern YYYYMMDD_HHMMSSSSS = new DateTimePattern(
			YYYYMMDD.getPattern() + BLANK + HHMMSSSSS.getPattern());

	/**
	 * Example: 20180314 131423678789
	 */
	public final static DateTimePattern YYYYMMDD_HHMMSSSSSSSS = new DateTimePattern(
			YYYYMMDD.getPattern() + BLANK + HHMMSSSSSSSS.getPattern());

	/**
	 * Example: 20180314-131423
	 */
	public final static DateTimePattern YYYYMMDD_L_HHMMSS = new DateTimePattern(
			YYYYMMDD.getPattern() + LINE + HHMMSS.getPattern());

	/**
	 * Example: 20180314-131423678
	 */
	public final static DateTimePattern YYYYMMDD_L_HHMMSSSSS = new DateTimePattern(
			YYYYMMDD.getPattern() + LINE + HHMMSSSSS.getPattern());

	/**
	 * Example: 20180314-131423678789
	 */
	public final static DateTimePattern YYYYMMDD_L_HHMMSSSSSSSS = new DateTimePattern(
			YYYYMMDD.getPattern() + LINE + HHMMSSSSSSSS.getPattern());

	/**
	 * =============================================================================================
	 */

	/**
	 * Example: 20180314 13:14:23
	 */
	public final static DateTimePattern YYYYMMDD_HH_MM_SS = new DateTimePattern(
			YYYYMMDD.getPattern() + BLANK + HH_MM_SS.getPattern());

	/**
	 * Example: 20180314 13:14:23.678
	 */
	public final static DateTimePattern YYYYMMDD_HH_MM_SS_SSS = new DateTimePattern(
			YYYYMMDD.getPattern() + BLANK + HH_MM_SS_SSS.getPattern());

	/**
	 * Example: 20180314T13:14:23.678789
	 */
	public final static DateTimePattern YYYYMMDD_HH_MM_SS_SSSSSS = new DateTimePattern(
			YYYYMMDD.getPattern() + BLANK + HH_MM_SS_SSSSSS.getPattern());

	/**
	 * Example: 20180314-13:14:23
	 */
	public final static DateTimePattern YYYYMMDD_L_HH_MM_SS = new DateTimePattern(
			YYYYMMDD.getPattern() + LINE + HH_MM_SS.getPattern());

	/**
	 * Example: 20180314-13:14:23.678
	 */
	public final static DateTimePattern YYYYMMDD_L_HH_MM_SS_SSS = new DateTimePattern(
			YYYYMMDD.getPattern() + LINE + HH_MM_SS_SSS.getPattern());

	/**
	 * Example: 20180314-13:14:23.678789
	 */
	public final static DateTimePattern YYYYMMDD_L_HH_MM_SS_SSSSSS = new DateTimePattern(
			YYYYMMDD.getPattern() + LINE + HH_MM_SS_SSSSSS.getPattern());

	/**
	 * =============================================================================================
	 */

	/**
	 * Example: 2018-03-14 131423
	 */
	public final static DateTimePattern YYYY_MM_DD_HHMMSS = new DateTimePattern(
			YYYY_MM_DD.getPattern() + BLANK + HHMMSS.getPattern());

	/**
	 * Example: 2018-03-14 131423678
	 */
	public final static DateTimePattern YYYY_MM_DD_HHMMSSSSS = new DateTimePattern(
			YYYY_MM_DD.getPattern() + BLANK + HHMMSSSSS.getPattern());

	/**
	 * Example: 2018-03-14 131423678789
	 */
	public final static DateTimePattern YYYY_MM_DD_HHMMSSSSSSSS = new DateTimePattern(
			YYYY_MM_DD.getPattern() + BLANK + HHMMSSSSSSSS.getPattern());

	/**
	 * =============================================================================================
	 */

	/**
	 * Example: 2018-03-14 13:14:23
	 */
	public final static DateTimePattern YYYY_MM_DD_HH_MM_SS = new DateTimePattern(
			YYYY_MM_DD.getPattern() + BLANK + HH_MM_SS.getPattern());

	/**
	 * Example: 2018-03-14 13:14:23.678
	 */
	public final static DateTimePattern YYYY_MM_DD_HH_MM_SS_SSS = new DateTimePattern(
			YYYY_MM_DD.getPattern() + BLANK + HH_MM_SS_SSS.getPattern());

	/**
	 * Example: 2018-03-14 13:14:23.678789
	 */
	public final static DateTimePattern YYYY_MM_DD_HH_MM_SS_SSSSSS = new DateTimePattern(
			YYYY_MM_DD.getPattern() + BLANK + HH_MM_SS_SSSSSS.getPattern());

	/**
	 * 
	 * @param pattern
	 */
	private DateTimePattern(String pattern) {
		super(pattern);
	}

}

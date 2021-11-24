package io.mercury.common.datetime.pattern.spec;

import io.mercury.common.datetime.pattern.AbstractPattern;

/**
 * 常用时间格式列表
 * 
 * @author yellow013
 *
 */
public final class TimePattern extends AbstractPattern {

	/**
	 * Example: 13
	 */
	public final static TimePattern HH = new TimePattern("HH");

	/**
	 * Example: 1314
	 */
	public final static TimePattern HHMM = new TimePattern("HHmm");

	/**
	 * Example: 131423
	 */
	public final static TimePattern HHMMSS = new TimePattern("HHmmss");

	/**
	 * Example: 131423678
	 */
	public final static TimePattern HHMMSSSSS = new TimePattern("HHmmssSSS");

	/**
	 * Example: 131423678789
	 */
	public final static TimePattern HHMMSSSSSSSS = new TimePattern("HHmmssSSSSSS");

	/**
	 * Example: 13:14
	 */
	public final static TimePattern HH_MM = new TimePattern("HH:mm");

	/**
	 * Example: 13:14:23
	 */
	public final static TimePattern HH_MM_SS = new TimePattern("HH:mm:ss");

	/**
	 * Example: 13:14:23.678
	 */
	public final static TimePattern HH_MM_SS_SSS = new TimePattern("HH:mm:ss.SSS");

	/**
	 * Example: 13:14:23.678789
	 */
	public final static TimePattern HH_MM_SS_SSSSSS = new TimePattern("HH:mm:ss.SSSSSS");

	/**
	 * 
	 * @param pattern
	 */
	private TimePattern(String pattern) {
		super(pattern);
	}

}

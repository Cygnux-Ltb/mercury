package io.mercury.common.datetime.pattern;

/**
 * 常用时间格式列表
 * 
 * @author yellow013
 *
 */
public final class TimePattern extends AbstractPattern {

	/**
	 * example: 13
	 */
	public final static TimePattern HH = new TimePattern("HH");

	/**
	 * example: 1314
	 */
	public final static TimePattern HHMM = new TimePattern("HHmm");

	/**
	 * example: 131423
	 */
	public final static TimePattern HHMMSS = new TimePattern("HHmmss");

	/**
	 * example: 131423678
	 */
	public final static TimePattern HHMMSSSSS = new TimePattern("HHmmssSSS");

	/**
	 * example: 131423678789
	 */
	public final static TimePattern HHMMSSSSSSSS = new TimePattern("HHmmssSSSSSS");

	/**
	 * example: 13:14
	 */
	public final static TimePattern HH_MM = new TimePattern("HH:mm");

	/**
	 * example: 13:14:23
	 */
	public final static TimePattern HH_MM_SS = new TimePattern("HH:mm:ss");

	/**
	 * example: 13:14:23.678
	 */
	public final static TimePattern HH_MM_SS_SSS = new TimePattern("HH:mm:ss.SSS");

	/**
	 * example: 13:14:23.678789
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

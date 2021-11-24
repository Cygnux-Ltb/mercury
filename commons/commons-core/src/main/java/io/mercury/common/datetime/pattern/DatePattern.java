package io.mercury.common.datetime.pattern.spec;

import io.mercury.common.datetime.pattern.AbstractPattern;

/**
 * 日期格式列表
 * 
 * @author yellow013
 *
 */
public final class DatePattern extends AbstractPattern {

	/**
	 * Example: 201803
	 */
	public final static DatePattern YYYYMM = new DatePattern("yyyyMM");

	/**
	 * Example: 20180314
	 */
	public final static DatePattern YYYYMMDD = new DatePattern("yyyyMMdd");

	/**
	 * Example: 2018-03
	 */
	public final static DatePattern YYYY_MM = new DatePattern("yyyy-MM");

	/**
	 * Example: 2018-03-14
	 */
	public final static DatePattern YYYY_MM_DD = new DatePattern("yyyy-MM-dd");

	/**
	 * 
	 * @param pattern
	 */
	private DatePattern(String pattern) {
		super(pattern);
	}

}

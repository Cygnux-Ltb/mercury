package io.mercury.common.datetime.pattern;

/**
 * 指定的日期时间模式
 * 
 * @author yellow013
 *
 */
public final class SpecifiedPattern extends AbstractPattern {

	private SpecifiedPattern(String pattern) {
		super(pattern);
	}

	public static SpecifiedPattern ofPattern(String pattern) {
		return new SpecifiedPattern(pattern);
	}

}
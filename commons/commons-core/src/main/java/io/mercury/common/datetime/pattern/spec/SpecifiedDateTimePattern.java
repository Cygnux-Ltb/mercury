package io.mercury.common.datetime.pattern.impl;

import io.mercury.common.datetime.pattern.SpecifiedPattern;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 指定日期时间格式列表
 *
 * @author yellow013
 */
public final class SpecifiedDateTimePattern extends SpecifiedPattern<LocalDateTime> {

    /**
     * @param pattern String
     * @return SpecifiedDateTimePattern
     */
    public static SpecifiedDateTimePattern ofPattern(String pattern) {
        return new SpecifiedDateTimePattern(pattern);
    }

    /**
     * @param pattern String
     */
    private SpecifiedDateTimePattern(String pattern) {
        super(pattern);
    }

    @Override
    protected LocalDateTime provideNow() {
        return LocalDateTime.now();
    }

    @Override
    protected LocalDateTime provideNow(ZoneId zoneId) {
        return LocalDateTime.now(zoneId);
    }

    /**
     * @param text String
     * @return LocalDateTime
     */
    @Override
    public LocalDateTime parse(String text) {
        return LocalDateTime.parse(text, getFormatter());
    }

}

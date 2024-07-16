package io.mercury.common.datetime.pattern.impl;

import io.mercury.common.datetime.pattern.SpecifiedPattern;

import java.time.LocalTime;
import java.time.ZoneId;

/**
 * 常用时间格式列表
 *
 * @author yellow013
 */
public final class SpecifiedTimePattern extends SpecifiedPattern<LocalTime> {

    /**
     * @param pattern String
     * @return SpecifiedTimePattern
     */
    public static SpecifiedTimePattern ofPattern(String pattern) {
        return new SpecifiedTimePattern(pattern);
    }

    /**
     * @param pattern String
     */
    private SpecifiedTimePattern(String pattern) {
        super(pattern);
    }

    @Override
    protected LocalTime provideNow() {
        return LocalTime.now();
    }

    @Override
    protected LocalTime provideNow(ZoneId zoneId) {
        return LocalTime.now(zoneId);
    }

    /**
     * @param text String
     * @return LocalTime
     */
    @Override
    public LocalTime parse(String text) {
        return LocalTime.parse(text, getFormatter());
    }

}

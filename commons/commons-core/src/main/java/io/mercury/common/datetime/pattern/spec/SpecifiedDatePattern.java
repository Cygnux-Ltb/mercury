package io.mercury.common.datetime.pattern.impl;

import io.mercury.common.datetime.pattern.SpecifiedPattern;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * 日期格式列表
 *
 * @author yellow013
 */
public final class SpecifiedDatePattern extends SpecifiedPattern<LocalDate> {

    /**
     * @param pattern String
     * @return SpecifiedDatePattern
     */
    public static SpecifiedDatePattern ofPattern(String pattern) {
        return new SpecifiedDatePattern(pattern);
    }

    /**
     * @param pattern String
     */
    private SpecifiedDatePattern(String pattern) {
        super(pattern);
    }

    @Override
    protected LocalDate provideNow() {
        return LocalDate.now();
    }

    @Override
    protected LocalDate provideNow(ZoneId zoneId) {
        return LocalDate.now(zoneId);
    }

    /**
     * @param text String
     * @return LocalDate
     */
    @Override
    public LocalDate parse(String text) {
        return LocalDate.parse(text, getFormatter());
    }

}

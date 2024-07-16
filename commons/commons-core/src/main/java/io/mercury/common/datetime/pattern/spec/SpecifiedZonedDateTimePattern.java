package io.mercury.common.datetime.pattern.impl;

import io.mercury.common.datetime.pattern.SpecifiedPattern;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * 常用日期时间格式(带时区)
 */
public final class SpecifiedZonedDateTimePattern extends SpecifiedPattern<ZonedDateTime> {

    /**
     * @param pattern String
     * @return SpecifiedZonedDateTimePattern
     */
    public static SpecifiedZonedDateTimePattern ofPattern(String pattern) {
        return new SpecifiedZonedDateTimePattern(pattern);
    }

    /**
     * @param pattern String
     */
    private SpecifiedZonedDateTimePattern(String pattern) {
        super(pattern);
    }

    @Override
    protected ZonedDateTime provideNow() {
        return ZonedDateTime.now();
    }

    @Override
    protected ZonedDateTime provideNow(ZoneId zoneId) {
        return ZonedDateTime.now(zoneId);
    }

    /**
     * @param text String
     * @return T extends Temporal
     */
    @Override
    public ZonedDateTime parse(String text) {
        return ZonedDateTime.parse(text, getFormatter());
    }

}

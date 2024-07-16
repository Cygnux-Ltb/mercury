package io.mercury.common.datetime.pattern;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static io.mercury.common.datetime.pattern.DateTimePattern.YYMMDDHH;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYMMDDHHMM;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYMMDDHHMMSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYMMDDHHMMSSSSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYMMDD_HHMMSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYMMDD_HHMMSSSSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYMMDD_HHMMSSSSSSSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYMMDD_HH_MM_SS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYMMDD_HH_MM_SS_SSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYMMDD_HH_MM_SS_SSSSSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYMMDD_L_HHMMSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYMMDD_L_HHMMSSSSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYMMDD_L_HHMMSSSSSSSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYMMDD_L_HH_MM_SS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYMMDD_L_HH_MM_SS_SSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYMMDD_L_HH_MM_SS_SSSSSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDDHH;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDDHHMM;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDDHHMMSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDDHHMMSSSSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDD_HHMMSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDD_HHMMSSSSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDD_HHMMSSSSSSSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDD_HH_MM_SS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDD_HH_MM_SS_SSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDD_HH_MM_SS_SSSSSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDD_L_HHMMSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDD_L_HHMMSSSSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDD_L_HHMMSSSSSSSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDD_L_HH_MM_SS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDD_L_HH_MM_SS_SSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYYMMDD_L_HH_MM_SS_SSSSSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYY_MM_DD_HHMMSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYY_MM_DD_HHMMSSSSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYY_MM_DD_HHMMSSSSSSSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYY_MM_DD_HH_MM_SS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYY_MM_DD_HH_MM_SS_SSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YYYY_MM_DD_HH_MM_SS_SSSSSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YY_MM_DD_HHMMSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YY_MM_DD_HHMMSSSSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YY_MM_DD_HHMMSSSSSSSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YY_MM_DD_HH_MM_SS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YY_MM_DD_HH_MM_SS_SSS;
import static io.mercury.common.datetime.pattern.DateTimePattern.YY_MM_DD_HH_MM_SS_SSSSSS;

/**
 * 常用日期时间格式(带时区)
 */
public final class ZonedDateTimePattern extends SpecifiedPattern<ZonedDateTime> {

    /**
     * example: 18031413+0900
     */
    public final static ZonedDateTimePattern YYMMDDHH_Z =
            new ZonedDateTimePattern(YYMMDDHH.getPattern() + "Z");

    /**
     * example: 1803141314+0900
     */
    public final static ZonedDateTimePattern YYMMDDHHMM_Z =
            new ZonedDateTimePattern(YYMMDDHHMM.getPattern() + "Z");

    /**
     * example: 180314131423+0900
     */
    public final static ZonedDateTimePattern YYMMDDHHMMSS_Z =
            new ZonedDateTimePattern(YYMMDDHHMMSS.getPattern() + "Z");

    /**
     * example: 180314131423678+0900
     */
    public final static ZonedDateTimePattern YYMMDDHHMMSSSSS_Z =
            new ZonedDateTimePattern(YYMMDDHHMMSSSSS.getPattern() + "Z");

    /**
     * example: 2018031413+0900
     */
    public final static ZonedDateTimePattern YYYYMMDDHH_Z =
            new ZonedDateTimePattern(YYYYMMDDHH.getPattern() + "Z");

    /**
     * example: 201803141314+0900
     */
    public final static ZonedDateTimePattern YYYYMMDDHHMM_Z =
            new ZonedDateTimePattern(YYYYMMDDHHMM.getPattern() + "Z");

    /**
     * example: 20180314131423+0900
     */
    public final static ZonedDateTimePattern YYYYMMDDHHMMSS_Z =
            new ZonedDateTimePattern(YYYYMMDDHHMMSS.getPattern() + "Z");

    /**
     * example: 20180314131423678+0900
     */
    public final static ZonedDateTimePattern YYYYMMDDHHMMSSSSS_Z =
            new ZonedDateTimePattern(YYYYMMDDHHMMSSSSS.getPattern() + "Z");

    /*
     * =============================================================================================
     */

    /**
     * example: 180314 131423+0900
     */
    public final static ZonedDateTimePattern YYMMDD_HHMMSS_Z =
            new ZonedDateTimePattern(YYMMDD_HHMMSS.getPattern() + "Z");

    /**
     * example: 180314 131423678+0900
     */
    public final static ZonedDateTimePattern YYMMDD_HHMMSSSSS_Z =
            new ZonedDateTimePattern(YYMMDD_HHMMSSSSS.getPattern() + "Z");

    /**
     * example: 180314 131423678789+0900
     */
    public final static ZonedDateTimePattern YYMMDD_HHMMSSSSSSSS_Z =
            new ZonedDateTimePattern(YYMMDD_HHMMSSSSSSSS.getPattern() + "Z");

    /**
     * example: 180314-131423+0900
     */
    public final static ZonedDateTimePattern YYMMDD_L_HHMMSS_Z =
            new ZonedDateTimePattern(YYMMDD_L_HHMMSS.getPattern() + "Z");

    /**
     * example: 180314-131423678+0900
     */
    public final static ZonedDateTimePattern YYMMDD_L_HHMMSSSSS_Z =
            new ZonedDateTimePattern(YYMMDD_L_HHMMSSSSS.getPattern() + "Z");

    /**
     * example: 180314-131423678789+0900
     */
    public final static ZonedDateTimePattern YYMMDD_L_HHMMSSSSSSSS_Z =
            new ZonedDateTimePattern(YYMMDD_L_HHMMSSSSSSSS.getPattern() + "Z");

    /**
     * example: 20180314 131423+0900
     */
    public final static ZonedDateTimePattern YYYYMMDD_HHMMSS_Z =
            new ZonedDateTimePattern(YYYYMMDD_HHMMSS.getPattern() + "Z");

    /**
     * example: 20180314 131423678+0900
     */
    public final static ZonedDateTimePattern YYYYMMDD_HHMMSSSSS_Z =
            new ZonedDateTimePattern(YYYYMMDD_HHMMSSSSS.getPattern() + "Z");

    /**
     * example: 20180314 131423678789+0900
     */
    public final static ZonedDateTimePattern YYYYMMDD_HHMMSSSSSSSS_Z =
            new ZonedDateTimePattern(YYYYMMDD_HHMMSSSSSSSS.getPattern() + "Z");

    /**
     * example: 20180314-131423+0900
     */
    public final static ZonedDateTimePattern YYYYMMDD_L_HHMMSS_Z =
            new ZonedDateTimePattern(YYYYMMDD_L_HHMMSS.getPattern() + "Z");

    /**
     * example: 20180314-131423678+0900
     */
    public final static ZonedDateTimePattern YYYYMMDD_L_HHMMSSSSS_Z =
            new ZonedDateTimePattern(YYYYMMDD_L_HHMMSSSSS.getPattern() + "Z");

    /**
     * example: 20180314-131423678789+0900
     */
    public final static ZonedDateTimePattern YYYYMMDD_L_HHMMSSSSSSSS_Z =
            new ZonedDateTimePattern(YYYYMMDD_L_HHMMSSSSSSSS.getPattern() + "Z");

    /*
     * =============================================================================================
     */

    /**
     * example: 180314 13:14:23+0900
     */
    public final static ZonedDateTimePattern YYMMDD_HH_MM_SS_Z =
            new ZonedDateTimePattern(YYMMDD_HH_MM_SS.getPattern() + "Z");

    /**
     * example: 180314 13:14:23.678+0900
     */
    public final static ZonedDateTimePattern YYMMDD_HH_MM_SS_SSS_Z =
            new ZonedDateTimePattern(YYMMDD_HH_MM_SS_SSS.getPattern() + "Z");

    /**
     * example: 180314T13:14:23.678789+0900
     */
    public final static ZonedDateTimePattern YYMMDD_HH_MM_SS_SSSSSS_Z =
            new ZonedDateTimePattern(YYMMDD_HH_MM_SS_SSSSSS.getPattern() + "Z");

    /**
     * example: 180314-13:14:23+0900
     */
    public final static ZonedDateTimePattern YYMMDD_L_HH_MM_SS_Z =
            new ZonedDateTimePattern(YYMMDD_L_HH_MM_SS.getPattern() + "Z");

    /**
     * example: 180314-13:14:23.678+0900
     */
    public final static ZonedDateTimePattern YYMMDD_L_HH_MM_SS_SSS_Z =
            new ZonedDateTimePattern(YYMMDD_L_HH_MM_SS_SSS.getPattern() + "Z");

    /**
     * example: 180314-13:14:23.678789+0900
     */
    public final static ZonedDateTimePattern YYMMDD_L_HH_MM_SS_SSSSSS_Z =
            new ZonedDateTimePattern(YYMMDD_L_HH_MM_SS_SSSSSS.getPattern() + "Z");

    /**
     * example: 20180314 13:14:23+0900
     */
    public final static ZonedDateTimePattern YYYYMMDD_HH_MM_SS_Z =
            new ZonedDateTimePattern(YYYYMMDD_HH_MM_SS.getPattern() + "Z");

    /**
     * example: 20180314 13:14:23.678+0900
     */
    public final static ZonedDateTimePattern YYYYMMDD_HH_MM_SS_SSS_Z =
            new ZonedDateTimePattern(YYYYMMDD_HH_MM_SS_SSS.getPattern() + "Z");

    /**
     * example: 20180314 13:14:23.678789+0900
     */
    public final static ZonedDateTimePattern YYYYMMDD_HH_MM_SS_SSSSSS_Z =
            new ZonedDateTimePattern(YYYYMMDD_HH_MM_SS_SSSSSS.getPattern() + "Z");

    /**
     * example: 20180314-13:14:23+0900
     */
    public final static ZonedDateTimePattern YYYYMMDD_L_HH_MM_SS_Z =
            new ZonedDateTimePattern(YYYYMMDD_L_HH_MM_SS.getPattern() + "Z");

    /**
     * example: 20180314-13:14:23.678+0900
     */
    public final static ZonedDateTimePattern YYYYMMDD_L_HH_MM_SS_SSS_Z =
            new ZonedDateTimePattern(YYYYMMDD_L_HH_MM_SS_SSS.getPattern() + "Z");

    /**
     * example: 20180314-13:14:23.678789+0900
     */
    public final static ZonedDateTimePattern YYYYMMDD_L_HH_MM_SS_SSSSSS_Z =
            new ZonedDateTimePattern(YYYYMMDD_L_HH_MM_SS_SSSSSS.getPattern() + "Z");

    /*
     * =============================================================================================
     */

    /**
     * example: 18-03-14 131423+0900
     */
    public final static ZonedDateTimePattern YY_MM_DD_HHMMSS_Z =
            new ZonedDateTimePattern(YY_MM_DD_HHMMSS.getPattern() + "Z");

    /**
     * example: 18-03-14 131423678+0900
     */
    public final static ZonedDateTimePattern YY_MM_DD_HHMMSSSSS_Z =
            new ZonedDateTimePattern(YY_MM_DD_HHMMSSSSS.getPattern() + "Z");

    /**
     * example: 18-03-14 131423678789+0900
     */
    public final static ZonedDateTimePattern YY_MM_DD_HHMMSSSSSSSS_Z =
            new ZonedDateTimePattern(YY_MM_DD_HHMMSSSSSSSS.getPattern() + "Z");

    /**
     * example: 2018-03-14 131423+0900
     */
    public final static ZonedDateTimePattern YYYY_MM_DD_HHMMSS_Z =
            new ZonedDateTimePattern(YYYY_MM_DD_HHMMSS.getPattern() + "Z");

    /**
     * example: 2018-03-14 131423678+0900
     */
    public final static ZonedDateTimePattern YYYY_MM_DD_HHMMSSSSS_Z =
            new ZonedDateTimePattern(YYYY_MM_DD_HHMMSSSSS.getPattern() + "Z");

    /**
     * example: 2018-03-14 131423678789+0900
     */
    public final static ZonedDateTimePattern YYYY_MM_DD_HHMMSSSSSSSS_Z =
            new ZonedDateTimePattern(YYYY_MM_DD_HHMMSSSSSSSS.getPattern() + "Z");

    /*
     * =============================================================================================
     */

    /**
     * example: 18-03-14 13:14:23+0900
     */
    public final static ZonedDateTimePattern YY_MM_DD_HH_MM_SS_Z =
            new ZonedDateTimePattern(YY_MM_DD_HH_MM_SS.getPattern() + "Z");

    /**
     * example: 18-03-14 13:14:23.678+0900
     */
    public final static ZonedDateTimePattern YY_MM_DD_HH_MM_SS_SSS_Z =
            new ZonedDateTimePattern(YY_MM_DD_HH_MM_SS_SSS.getPattern() + "Z");

    /**
     * example: 18-03-14 13:14:23.678789+0900
     */
    public final static ZonedDateTimePattern YY_MM_DD_HH_MM_SS_SSSSSS_Z =
            new ZonedDateTimePattern(YY_MM_DD_HH_MM_SS_SSSSSS.getPattern() + "Z");

    /**
     * example: 2018-03-14 13:14:23+0900
     */
    public final static ZonedDateTimePattern YYYY_MM_DD_HH_MM_SS_Z =
            new ZonedDateTimePattern(YYYY_MM_DD_HH_MM_SS.getPattern() + "Z");

    /**
     * example: 2018-03-14 13:14:23.678+0900
     */
    public final static ZonedDateTimePattern YYYY_MM_DD_HH_MM_SS_SSS_Z =
            new ZonedDateTimePattern(YYYY_MM_DD_HH_MM_SS_SSS.getPattern() + "Z");

    /**
     * example: 2018-03-14 13:14:23.678789+0900
     */
    public final static ZonedDateTimePattern YYYY_MM_DD_HH_MM_SS_SSSSSS_Z =
            new ZonedDateTimePattern(YYYY_MM_DD_HH_MM_SS_SSSSSS.getPattern() + "Z");


    /**
     * @param pattern String
     */
    private ZonedDateTimePattern(String pattern) {
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

    public static void main(String[] args) {
        System.out.println(YYYY_MM_DD_HH_MM_SS_SSSSSS_Z.now());
    }

}

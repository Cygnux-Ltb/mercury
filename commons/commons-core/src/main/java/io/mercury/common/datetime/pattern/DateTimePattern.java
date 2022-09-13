package io.mercury.common.datetime.pattern;

import static io.mercury.common.character.Separator.BLANK;
import static io.mercury.common.character.Separator.LINE;
import static io.mercury.common.datetime.pattern.DatePattern.YYMMDD;
import static io.mercury.common.datetime.pattern.DatePattern.YYYYMMDD;
import static io.mercury.common.datetime.pattern.DatePattern.YYYY_MM_DD;
import static io.mercury.common.datetime.pattern.DatePattern.YY_MM_DD;
import static io.mercury.common.datetime.pattern.TimePattern.HH;
import static io.mercury.common.datetime.pattern.TimePattern.HHMM;
import static io.mercury.common.datetime.pattern.TimePattern.HHMMSS;
import static io.mercury.common.datetime.pattern.TimePattern.HHMMSSSSS;
import static io.mercury.common.datetime.pattern.TimePattern.HHMMSSSSSSSS;
import static io.mercury.common.datetime.pattern.TimePattern.HH_MM_SS;
import static io.mercury.common.datetime.pattern.TimePattern.HH_MM_SS_SSS;
import static io.mercury.common.datetime.pattern.TimePattern.HH_MM_SS_SSSSSS;

/**
 * 常用日期时间格式列表
 *
 * @author yellow013
 */
public final class DateTimePattern extends AbstractPattern {

    /**
     * example: 18031413
     */
    public final static DateTimePattern YYMMDDHH = new DateTimePattern(YYMMDD.getPattern() + HH.getPattern());

    /**
     * example: 1803141314
     */
    public final static DateTimePattern YYMMDDHHMM = new DateTimePattern(YYMMDD.getPattern() + HHMM.getPattern());

    /**
     * example: 180314131423
     */
    public final static DateTimePattern YYMMDDHHMMSS = new DateTimePattern(YYMMDD.getPattern() + HHMMSS.getPattern());

    /**
     * example: 180314131423678
     */
    public final static DateTimePattern YYMMDDHHMMSSSSS = new DateTimePattern(
            YYMMDD.getPattern() + HHMMSSSSS.getPattern());

    /**
     * example: 2018031413
     */
    public final static DateTimePattern YYYYMMDDHH = new DateTimePattern(YYYYMMDD.getPattern() + HH.getPattern());

    /**
     * example: 201803141314
     */
    public final static DateTimePattern YYYYMMDDHHMM = new DateTimePattern(YYYYMMDD.getPattern() + HHMM.getPattern());

    /**
     * example: 20180314131423
     */
    public final static DateTimePattern YYYYMMDDHHMMSS = new DateTimePattern(
            YYYYMMDD.getPattern() + HHMMSS.getPattern());

    /**
     * example: 20180314131423678
     */
    public final static DateTimePattern YYYYMMDDHHMMSSSSS = new DateTimePattern(
            YYYYMMDD.getPattern() + HHMMSSSSS.getPattern());

    /*
     * =============================================================================================
     */

    /**
     * example: 180314 131423
     */
    public final static DateTimePattern YYMMDD_HHMMSS = new DateTimePattern(
            YYMMDD.getPattern() + BLANK + HHMMSS.getPattern());

    /**
     * example: 180314 131423678
     */
    public final static DateTimePattern YYMMDD_HHMMSSSSS = new DateTimePattern(
            YYMMDD.getPattern() + BLANK + HHMMSSSSS.getPattern());

    /**
     * example: 180314 131423678789
     */
    public final static DateTimePattern YYMMDD_HHMMSSSSSSSS = new DateTimePattern(
            YYMMDD.getPattern() + BLANK + HHMMSSSSSSSS.getPattern());

    /**
     * example: 180314-131423
     */
    public final static DateTimePattern YYMMDD_L_HHMMSS = new DateTimePattern(
            YYMMDD.getPattern() + LINE + HHMMSS.getPattern());

    /**
     * example: 180314-131423678
     */
    public final static DateTimePattern YYMMDD_L_HHMMSSSSS = new DateTimePattern(
            YYMMDD.getPattern() + LINE + HHMMSSSSS.getPattern());

    /**
     * example: 180314-131423678789
     */
    public final static DateTimePattern YYMMDD_L_HHMMSSSSSSSS = new DateTimePattern(
            YYMMDD.getPattern() + LINE + HHMMSSSSSSSS.getPattern());

    /**
     * example: 20180314 131423
     */
    public final static DateTimePattern YYYYMMDD_HHMMSS = new DateTimePattern(
            YYYYMMDD.getPattern() + BLANK + HHMMSS.getPattern());

    /**
     * example: 20180314 131423678
     */
    public final static DateTimePattern YYYYMMDD_HHMMSSSSS = new DateTimePattern(
            YYYYMMDD.getPattern() + BLANK + HHMMSSSSS.getPattern());

    /**
     * example: 20180314 131423678789
     */
    public final static DateTimePattern YYYYMMDD_HHMMSSSSSSSS = new DateTimePattern(
            YYYYMMDD.getPattern() + BLANK + HHMMSSSSSSSS.getPattern());

    /**
     * example: 20180314-131423
     */
    public final static DateTimePattern YYYYMMDD_L_HHMMSS = new DateTimePattern(
            YYYYMMDD.getPattern() + LINE + HHMMSS.getPattern());

    /**
     * example: 20180314-131423678
     */
    public final static DateTimePattern YYYYMMDD_L_HHMMSSSSS = new DateTimePattern(
            YYYYMMDD.getPattern() + LINE + HHMMSSSSS.getPattern());

    /**
     * example: 20180314-131423678789
     */
    public final static DateTimePattern YYYYMMDD_L_HHMMSSSSSSSS = new DateTimePattern(
            YYYYMMDD.getPattern() + LINE + HHMMSSSSSSSS.getPattern());

    /*
     * =============================================================================================
     */

    /**
     * example: 180314 13:14:23
     */
    public final static DateTimePattern YYMMDD_HH_MM_SS = new DateTimePattern(
            YYMMDD.getPattern() + BLANK + HH_MM_SS.getPattern());

    /**
     * example: 180314 13:14:23.678
     */
    public final static DateTimePattern YYMMDD_HH_MM_SS_SSS = new DateTimePattern(
            YYMMDD.getPattern() + BLANK + HH_MM_SS_SSS.getPattern());

    /**
     * example: 180314T13:14:23.678789
     */
    public final static DateTimePattern YYMMDD_HH_MM_SS_SSSSSS = new DateTimePattern(
            YYMMDD.getPattern() + BLANK + HH_MM_SS_SSSSSS.getPattern());

    /**
     * example: 180314-13:14:23
     */
    public final static DateTimePattern YYMMDD_L_HH_MM_SS = new DateTimePattern(
            YYMMDD.getPattern() + LINE + HH_MM_SS.getPattern());

    /**
     * example: 180314-13:14:23.678
     */
    public final static DateTimePattern YYMMDD_L_HH_MM_SS_SSS = new DateTimePattern(
            YYMMDD.getPattern() + LINE + HH_MM_SS_SSS.getPattern());

    /**
     * example: 180314-13:14:23.678789
     */
    public final static DateTimePattern YYMMDD_L_HH_MM_SS_SSSSSS = new DateTimePattern(
            YYMMDD.getPattern() + LINE + HH_MM_SS_SSSSSS.getPattern());

    /**
     * example: 20180314 13:14:23
     */
    public final static DateTimePattern YYYYMMDD_HH_MM_SS = new DateTimePattern(
            YYYYMMDD.getPattern() + BLANK + HH_MM_SS.getPattern());

    /**
     * example: 20180314 13:14:23.678
     */
    public final static DateTimePattern YYYYMMDD_HH_MM_SS_SSS = new DateTimePattern(
            YYYYMMDD.getPattern() + BLANK + HH_MM_SS_SSS.getPattern());

    /**
     * example: 20180314 13:14:23.678789
     */
    public final static DateTimePattern YYYYMMDD_HH_MM_SS_SSSSSS = new DateTimePattern(
            YYYYMMDD.getPattern() + BLANK + HH_MM_SS_SSSSSS.getPattern());

    /**
     * example: 20180314-13:14:23
     */
    public final static DateTimePattern YYYYMMDD_L_HH_MM_SS = new DateTimePattern(
            YYYYMMDD.getPattern() + LINE + HH_MM_SS.getPattern());

    /**
     * example: 20180314-13:14:23.678
     */
    public final static DateTimePattern YYYYMMDD_L_HH_MM_SS_SSS = new DateTimePattern(
            YYYYMMDD.getPattern() + LINE + HH_MM_SS_SSS.getPattern());

    /**
     * example: 20180314-13:14:23.678789
     */
    public final static DateTimePattern YYYYMMDD_L_HH_MM_SS_SSSSSS = new DateTimePattern(
            YYYYMMDD.getPattern() + LINE + HH_MM_SS_SSSSSS.getPattern());

    /*
     * =============================================================================================
     */

    /**
     * example: 18-03-14 131423
     */
    public final static DateTimePattern YY_MM_DD_HHMMSS = new DateTimePattern(
            YY_MM_DD.getPattern() + BLANK + HHMMSS.getPattern());

    /**
     * example: 18-03-14 131423678
     */
    public final static DateTimePattern YY_MM_DD_HHMMSSSSS = new DateTimePattern(
            YY_MM_DD.getPattern() + BLANK + HHMMSSSSS.getPattern());

    /**
     * example: 18-03-14 131423678789
     */
    public final static DateTimePattern YY_MM_DD_HHMMSSSSSSSS = new DateTimePattern(
            YY_MM_DD.getPattern() + BLANK + HHMMSSSSSSSS.getPattern());

    /**
     * example: 2018-03-14 131423
     */
    public final static DateTimePattern YYYY_MM_DD_HHMMSS = new DateTimePattern(
            YYYY_MM_DD.getPattern() + BLANK + HHMMSS.getPattern());

    /**
     * example: 2018-03-14 131423678
     */
    public final static DateTimePattern YYYY_MM_DD_HHMMSSSSS = new DateTimePattern(
            YYYY_MM_DD.getPattern() + BLANK + HHMMSSSSS.getPattern());

    /**
     * example: 2018-03-14 131423678789
     */
    public final static DateTimePattern YYYY_MM_DD_HHMMSSSSSSSS = new DateTimePattern(
            YYYY_MM_DD.getPattern() + BLANK + HHMMSSSSSSSS.getPattern());

    /*
     * =============================================================================================
     */

    /**
     * example: 18-03-14 13:14:23
     */
    public final static DateTimePattern YY_MM_DD_HH_MM_SS = new DateTimePattern(
            YY_MM_DD.getPattern() + BLANK + HH_MM_SS.getPattern());

    /**
     * example: 18-03-14 13:14:23.678
     */
    public final static DateTimePattern YY_MM_DD_HH_MM_SS_SSS = new DateTimePattern(
            YY_MM_DD.getPattern() + BLANK + HH_MM_SS_SSS.getPattern());

    /**
     * example: 18-03-14 13:14:23.678789
     */
    public final static DateTimePattern YY_MM_DD_HH_MM_SS_SSSSSS = new DateTimePattern(
            YY_MM_DD.getPattern() + BLANK + HH_MM_SS_SSSSSS.getPattern());

    /**
     * example: 2018-03-14 13:14:23
     */
    public final static DateTimePattern YYYY_MM_DD_HH_MM_SS = new DateTimePattern(
            YYYY_MM_DD.getPattern() + BLANK + HH_MM_SS.getPattern());

    /**
     * example: 2018-03-14 13:14:23.678
     */
    public final static DateTimePattern YYYY_MM_DD_HH_MM_SS_SSS = new DateTimePattern(
            YYYY_MM_DD.getPattern() + BLANK + HH_MM_SS_SSS.getPattern());

    /**
     * example: 2018-03-14 13:14:23.678789
     */
    public final static DateTimePattern YYYY_MM_DD_HH_MM_SS_SSSSSS = new DateTimePattern(
            YYYY_MM_DD.getPattern() + BLANK + HH_MM_SS_SSSSSS.getPattern());

    /**
     * @param pattern String
     */
    private DateTimePattern(String pattern) {
        super(pattern);
    }

}

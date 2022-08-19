package io.mercury.common.datetime.pattern;

/**
 * 日期格式列表
 *
 * @author yellow013
 */
public final class DatePattern extends AbstractPattern {

    /**
     * example: 1803
     */
    public final static DatePattern YYMM = new DatePattern("yyMM");

    /**
     * example: 201803
     */
    public final static DatePattern YYYYMM = new DatePattern("yyyyMM");

    /**
     * example: 180314
     */
    public final static DatePattern YYMMDD = new DatePattern("yyyyMMdd");

    /**
     * example: 20180314
     */
    public final static DatePattern YYYYMMDD = new DatePattern("yyyyMMdd");

    /**
     * example: 18-03
     */
    public final static DatePattern YY_MM = new DatePattern("yy-MM");

    /**
     * example: 2018-03
     */
    public final static DatePattern YYYY_MM = new DatePattern("yyyy-MM");

    /**
     * example: 18-03-14
     */
    public final static DatePattern YY_MM_DD = new DatePattern("yyyy-MM-dd");

    /**
     * example: 2018-03-14
     */
    public final static DatePattern YYYY_MM_DD = new DatePattern("yyyy-MM-dd");

    /**
     * @param pattern String
     */
    private DatePattern(String pattern) {
        super(pattern);
    }

}

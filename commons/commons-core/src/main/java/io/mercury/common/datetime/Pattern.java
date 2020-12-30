package io.mercury.common.datetime;

import static io.mercury.common.datetime.Pattern.DatePattern.YYYYMMDD;
import static io.mercury.common.datetime.Pattern.DatePattern.YYYY_MM_DD;
import static io.mercury.common.datetime.Pattern.PatternSeparator.BLANK;
import static io.mercury.common.datetime.Pattern.PatternSeparator.LINE;
import static io.mercury.common.datetime.Pattern.PatternSeparator.TIME;
import static io.mercury.common.datetime.Pattern.TimePattern.HH;
import static io.mercury.common.datetime.Pattern.TimePattern.HHMM;
import static io.mercury.common.datetime.Pattern.TimePattern.HHMMSS;
import static io.mercury.common.datetime.Pattern.TimePattern.HHMMSSSSS;
import static io.mercury.common.datetime.Pattern.TimePattern.HHMMSSSSSSSS;
import static io.mercury.common.datetime.Pattern.TimePattern.HH_MM_SS;
import static io.mercury.common.datetime.Pattern.TimePattern.HH_MM_SS_SSS;
import static io.mercury.common.datetime.Pattern.TimePattern.HH_MM_SS_SSSSSS;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

/**
 * 
 * All letters 'A' to 'Z' and 'a' to 'z' are reserved as pattern letters. <br>
 * The following pattern letters are defined:
 * 
 * <pre>
Symbol  Meaning                     Presentation      Examples
------  -------                     ------------      -------
G       era                         text              AD; Anno Domini; A
u       year                        year              2004; 04
y       year-of-era                 year              2004; 04
D       day-of-year                 number            189
M/L     month-of-year               number/text       7; 07; Jul; July; J
d       day-of-month                number            10

Q/q     quarter-of-year             number/text       3; 03; Q3; 3rd quarter
Y       week-based-year             year              1996; 96
w       week-of-week-based-year     number            27
W       week-of-month               number            4
E       day-of-week                 text              Tue; Tuesday; T
e/c     localized day-of-week       number/text       2; 02; Tue; Tuesday; T
F       week-of-month               number            3

a       am-pm-of-day                text              PM
h       clock-hour-of-am-pm (1-12)  number            12
K       hour-of-am-pm (0-11)        number            0
k       clock-hour-of-am-pm (1-24)  number            0

H       hour-of-day (0-23)          number            0
m       minute-of-hour              number            30
s       second-of-minute            number            55
S       fraction-of-second          fraction          978
A       milli-of-day                number            1234
n       nano-of-second              number            987654321
N       nano-of-day                 number            1234000000

V       time-zone ID                zone-id           America/Los_Angeles; Z; -08:30
z       time-zone name              zone-name         Pacific Standard Time; PST
O       localized zone-offset       offset-O          GMT+8; GMT+08:00; UTC-08:00;
X       zone-offset 'Z' for zero    offset-X          Z; -08; -0830; -08:30; -083015; -08:30:15;
x       zone-offset                 offset-x          +0000; -08; -0830; -08:30; -083015; -08:30:15;
Z       zone-offset                 offset-Z          +0000; -0800; -08:00;

p       pad next                    pad modifier      1

'       escape for text             delimiter
''      single quote                literal           '
[       optional section start
]       optional section end
#       reserved for future use
{       reserved for future use
}       reserved for future use
 * </pre>
 * 
 */
public abstract class Pattern {

	private final String pattern;
	private final DateTimeFormatter formatter;

	protected Pattern(String pattern) {
		this.pattern = pattern;
		this.formatter = DateTimeFormatter.ofPattern(pattern);
	}

	/**
	 * 
	 * @return the string pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * 
	 * @return the DateTimeFormatter instance
	 */
	public DateTimeFormatter getFormatter() {
		return formatter;
	}

	/**
	 * 
	 * @return the new java.time.format.DateTimeFormatter
	 */
	public DateTimeFormatter newDateTimeFormatter() {
		return DateTimeFormatter.ofPattern(pattern);
	}

	/**
	 * 
	 * @return the new java.text.SimpleDateFormat.SimpleDateFormat
	 */
	public DateFormat newSimpleDateFormat() {
		return new SimpleDateFormat(pattern);
	}

	/**
	 * 指定的日期时间模式
	 * 
	 * @author yellow013
	 *
	 */
	public static final class SpecifiedPattern extends Pattern {

		private SpecifiedPattern(String pattern) {
			super(pattern);
		}

		public static SpecifiedPattern ofPattern(String pattern) {
			return new SpecifiedPattern(pattern);
		}

	}

	/**
	 * 日期格式列表
	 * 
	 * @author yellow013
	 *
	 */
	public static final class DatePattern extends Pattern {

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

	/**
	 * 时间格式列表
	 * 
	 * @author yellow013
	 *
	 */
	public static final class TimePattern extends Pattern {

		/**
		 * Example: 13
		 */
		public final static TimePattern HH = new TimePattern("HH");

		/**
		 * Example: 1314
		 */
		public final static TimePattern HHMM = new TimePattern("HHmm");

		/**
		 * Example: 131423
		 */
		public final static TimePattern HHMMSS = new TimePattern("HHmmss");

		/**
		 * Example: 131423678
		 */
		public final static TimePattern HHMMSSSSS = new TimePattern("HHmmssSSS");

		/**
		 * Example: 131423678789
		 */
		public final static TimePattern HHMMSSSSSSSS = new TimePattern("HHmmssSSSSSS");

		/**
		 * Example: 13:14
		 */
		public final static TimePattern HH_MM = new TimePattern("HH:mm");

		/**
		 * Example: 13:14:23
		 */
		public final static TimePattern HH_MM_SS = new TimePattern("HH:mm:ss");

		/**
		 * Example: 13:14:23.678
		 */
		public final static TimePattern HH_MM_SS_SSS = new TimePattern("HH:mm:ss.SSS");

		/**
		 * Example: 13:14:23.678789
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

	/**
	 * 日期时间格式列表
	 * 
	 * @author yellow013
	 *
	 */
	public static final class DateTimePattern extends Pattern {

		/**
		 * Example: 2018031413
		 */
		public final static DateTimePattern YYYYMMDDHH = new DateTimePattern(YYYYMMDD.getPattern() + HH.getPattern());

		/**
		 * Example: 201803141314
		 */
		public final static DateTimePattern YYYYMMDDHHMM = new DateTimePattern(
				YYYYMMDD.getPattern() + HHMM.getPattern());

		/**
		 * Example: 20180314131423
		 */
		public final static DateTimePattern YYYYMMDDHHMMSS = new DateTimePattern(
				YYYYMMDD.getPattern() + HHMMSS.getPattern());

		/**
		 * Example: 20180314131423678
		 */
		public final static DateTimePattern YYYYMMDDHHMMSSSSS = new DateTimePattern(
				YYYYMMDD.getPattern() + HHMMSSSSS.getPattern());

		/**
		 * =============================================================================================
		 */

		/**
		 * Example: 20180314 131423
		 */
		public final static DateTimePattern YYYYMMDD_B_HHMMSS = new DateTimePattern(
				YYYYMMDD.getPattern() + BLANK + HHMMSS.getPattern());

		/**
		 * Example: 20180314 131423678
		 */
		public final static DateTimePattern YYYYMMDD_B_HHMMSSSSS = new DateTimePattern(
				YYYYMMDD.getPattern() + BLANK + HHMMSSSSS.getPattern());

		/**
		 * Example: 20180314 131423678789
		 */
		public final static DateTimePattern YYYYMMDD_B_HHMMSSSSSSSS = new DateTimePattern(
				YYYYMMDD.getPattern() + BLANK + HHMMSSSSSSSS.getPattern());

		/**
		 * Example: 20180314-131423
		 */
		public final static DateTimePattern YYYYMMDD_L_HHMMSS = new DateTimePattern(
				YYYYMMDD.getPattern() + LINE + HHMMSS.getPattern());

		/**
		 * Example: 20180314-131423678
		 */
		public final static DateTimePattern YYYYMMDD_L_HHMMSSSSS = new DateTimePattern(
				YYYYMMDD.getPattern() + LINE + HHMMSSSSS.getPattern());

		/**
		 * Example: 20180314-131423678789
		 */
		public final static DateTimePattern YYYYMMDD_L_HHMMSSSSSSSS = new DateTimePattern(
				YYYYMMDD.getPattern() + LINE + HHMMSSSSSSSS.getPattern());

		/**
		 * Example: 20180314T131423
		 */
		public final static DateTimePattern YYYYMMDD_T_HHMMSS = new DateTimePattern(
				YYYYMMDD.getPattern() + TIME + HHMMSS.getPattern());

		/**
		 * Example: 20180314T131423678
		 */
		public final static DateTimePattern YYYYMMDD_T_HHMMSSSSS = new DateTimePattern(
				YYYYMMDD.getPattern() + TIME + HHMMSSSSS.getPattern());

		/**
		 * Example: 20180314T131423678789
		 */
		public final static DateTimePattern YYYYMMDD_T_HHMMSSSSSSSS = new DateTimePattern(
				YYYYMMDD.getPattern() + TIME + HHMMSSSSSSSS.getPattern());

		/**
		 * =============================================================================================
		 */

		/**
		 * Example: 20180314 13:14:23
		 */
		public final static DateTimePattern YYYYMMDD_B_HH_MM_SS = new DateTimePattern(
				YYYYMMDD.getPattern() + BLANK + HH_MM_SS.getPattern());

		/**
		 * Example: 20180314 13:14:23.678
		 */
		public final static DateTimePattern YYYYMMDD_B_HH_MM_SS_SSS = new DateTimePattern(
				YYYYMMDD.getPattern() + BLANK + HH_MM_SS_SSS.getPattern());

		/**
		 * Example: 20180314T13:14:23.678789
		 */
		public final static DateTimePattern YYYYMMDD_B_HH_MM_SS_SSSSSS = new DateTimePattern(
				YYYYMMDD.getPattern() + BLANK + HH_MM_SS_SSSSSS.getPattern());

		/**
		 * Example: 20180314-13:14:23
		 */
		public final static DateTimePattern YYYYMMDD_L_HH_MM_SS = new DateTimePattern(
				YYYYMMDD.getPattern() + LINE + HH_MM_SS.getPattern());

		/**
		 * Example: 20180314-13:14:23.678
		 */
		public final static DateTimePattern YYYYMMDD_L_HH_MM_SS_SSS = new DateTimePattern(
				YYYYMMDD.getPattern() + LINE + HH_MM_SS_SSS.getPattern());

		/**
		 * Example: 20180314-13:14:23.678789
		 */
		public final static DateTimePattern YYYYMMDD_L_HH_MM_SS_SSSSSS = new DateTimePattern(
				YYYYMMDD.getPattern() + LINE + HH_MM_SS_SSSSSS.getPattern());

		/**
		 * Example: 20180314T13:14:23
		 */
		public final static DateTimePattern YYYYMMDD_T_HH_MM_SS = new DateTimePattern(
				YYYYMMDD.getPattern() + TIME + HH_MM_SS.getPattern());

		/**
		 * Example: 20180314T13:14:23.678
		 */
		public final static DateTimePattern YYYYMMDD_T_HH_MM_SS_SSS = new DateTimePattern(
				YYYYMMDD.getPattern() + TIME + HH_MM_SS_SSS.getPattern());

		/**
		 * Example: 20180314T13:14:23.567
		 */
		public final static DateTimePattern YYYYMMDD_T_HH_MM_SS_SSSSSS = new DateTimePattern(
				YYYYMMDD.getPattern() + TIME + HH_MM_SS_SSSSSS.getPattern());

		/**
		 * =============================================================================================
		 */

		/**
		 * Example: 2018-03-14 131423
		 */
		public final static DateTimePattern YYYY_MM_DD_B_HHMMSS = new DateTimePattern(
				YYYY_MM_DD.getPattern() + BLANK + HHMMSS.getPattern());

		/**
		 * Example: 2018-03-14 131423678
		 */
		public final static DateTimePattern YYYY_MM_DD_B_HHMMSSSSS = new DateTimePattern(
				YYYY_MM_DD.getPattern() + BLANK + HHMMSSSSS.getPattern());

		/**
		 * Example: 2018-03-14 131423678789
		 */
		public final static DateTimePattern YYYY_MM_DD_B_HHMMSSSSSSSS = new DateTimePattern(
				YYYY_MM_DD.getPattern() + BLANK + HHMMSSSSSSSS.getPattern());

		/**
		 * Example: 2018-03-14T131423
		 */
		public final static DateTimePattern YYYY_MM_DD_T_HHMMSS = new DateTimePattern(
				YYYY_MM_DD.getPattern() + TIME + HHMMSS.getPattern());

		/**
		 * Example: 2018-03-14T131423678
		 */
		public final static DateTimePattern YYYY_MM_DD_T_HHMMSSSSS = new DateTimePattern(
				YYYY_MM_DD.getPattern() + TIME + HHMMSSSSS.getPattern());

		/**
		 * Example: 2018-03-14T131423678789
		 */
		public final static DateTimePattern YYYY_MM_DD_T_HHMMSSSSSSSS = new DateTimePattern(
				YYYY_MM_DD.getPattern() + TIME + HHMMSSSSSSSS.getPattern());

		/**
		 * =============================================================================================
		 */

		/**
		 * Example: 2018-03-14 13:14:23
		 */
		public final static DateTimePattern YYYY_MM_DD_B_HH_MM_SS = new DateTimePattern(
				YYYY_MM_DD.getPattern() + BLANK + HH_MM_SS.getPattern());

		/**
		 * Example: 2018-03-14 13:14:23.678
		 */
		public final static DateTimePattern YYYY_MM_DD_B_HH_MM_SS_SSS = new DateTimePattern(
				YYYY_MM_DD.getPattern() + BLANK + HH_MM_SS_SSS.getPattern());

		/**
		 * Example: 2018-03-14 13:14:23.678789
		 */
		public final static DateTimePattern YYYY_MM_DD_B_HH_MM_SS_SSSSSS = new DateTimePattern(
				YYYY_MM_DD.getPattern() + BLANK + HH_MM_SS_SSSSSS.getPattern());

		/**
		 * Example: 2018-03-14T13:14:23
		 */
		public final static DateTimePattern YYYY_MM_DD_T_HH_MM_SS = new DateTimePattern(
				YYYY_MM_DD.getPattern() + TIME + HH_MM_SS.getPattern());

		/**
		 * Example: 2018-03-14T13:14:23.678
		 */
		public final static DateTimePattern YYYY_MM_DD_T_HH_MM_SS_SSS = new DateTimePattern(
				YYYY_MM_DD.getPattern() + TIME + HH_MM_SS_SSS.getPattern());

		/**
		 * Example: 2018-03-14T13:14:23.678789
		 */
		public final static DateTimePattern YYYY_MM_DD_T_HH_MM_SS_SSSSSS = new DateTimePattern(
				YYYY_MM_DD.getPattern() + TIME + HH_MM_SS_SSSSSS.getPattern());

		/**
		 * 
		 * @param pattern
		 */
		private DateTimePattern(String pattern) {
			super(pattern);
		}

	}

	public interface PatternSeparator {

		String LINE = "-";
		String BLANK = " ";
		String TIME = "T";

	}

}

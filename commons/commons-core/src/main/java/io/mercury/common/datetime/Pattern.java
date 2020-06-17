package io.mercury.common.datetime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

/**
 * 
 * All letters 'A' to 'Z' and 'a' to 'z' are reserved as pattern letters. The
 * following pattern letters are defined:
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

	Pattern(String pattern) {
		this.pattern = pattern;
		this.formatter = DateTimeFormatter.ofPattern(pattern);
	}

	public String getPattern() {
		return pattern;
	}

	public DateTimeFormatter getFormatter() {
		return formatter;
	}

	public DateFormat newSimpleDateFormat() {
		return new SimpleDateFormat(pattern);
	}

	public DateTimeFormatter newFormatter() {
		return DateTimeFormatter.ofPattern(pattern);
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
		public final static DateTimePattern YYYYMMDDHH = new DateTimePattern(
				DatePattern.YYYYMMDD.getPattern() + TimePattern.HH.getPattern());

		/**
		 * Example: 201803141314
		 */
		public final static DateTimePattern YYYYMMDDHHMM = new DateTimePattern(
				DatePattern.YYYYMMDD.getPattern() + TimePattern.HHMM.getPattern());

		/**
		 * Example: 20180314131423
		 */

		public final static DateTimePattern YYYYMMDDHHMMSS = new DateTimePattern(
				DatePattern.YYYYMMDD.getPattern() + TimePattern.HHMMSS.getPattern());

		/**
		 * Example: 20180314131423678
		 */
		public final static DateTimePattern YYYYMMDDHHMMSSSSS = new DateTimePattern(
				DatePattern.YYYYMMDD.getPattern() + TimePattern.HHMMSSSSS.getPattern());

		/**
		 * =============================================================================================
		 */

		/**
		 * Example: 20180314 131423
		 */
		public final static DateTimePattern YYYYMMDD_B_HHMMSS = new DateTimePattern(
				DatePattern.YYYYMMDD.getPattern() + PatternSymbol.BLANK + TimePattern.HHMMSS.getPattern());

		/**
		 * Example: 20180314 131423678
		 */
		public final static DateTimePattern YYYYMMDD_B_HHMMSSSSS = new DateTimePattern(
				DatePattern.YYYYMMDD.getPattern() + PatternSymbol.BLANK + TimePattern.HHMMSSSSS.getPattern());

		/**
		 * Example: 20180314 131423678789
		 */
		public final static DateTimePattern YYYYMMDD_B_HHMMSSSSSSSS = new DateTimePattern(
				DatePattern.YYYYMMDD.getPattern() + PatternSymbol.BLANK + TimePattern.HHMMSSSSSSSS.getPattern());

		/**
		 * Example: 20180314-131423
		 */
		public final static DateTimePattern YYYYMMDD_L_HHMMSS = new DateTimePattern(
				DatePattern.YYYYMMDD.getPattern() + PatternSymbol.LINE + TimePattern.HHMMSS.getPattern());

		/**
		 * Example: 20180314-131423678
		 */
		public final static DateTimePattern YYYYMMDD_L_HHMMSSSSS = new DateTimePattern(
				DatePattern.YYYYMMDD.getPattern() + PatternSymbol.LINE + TimePattern.HHMMSSSSS.getPattern());

		/**
		 * Example: 20180314-131423678789
		 */
		public final static DateTimePattern YYYYMMDD_L_HHMMSSSSSSSS = new DateTimePattern(
				DatePattern.YYYYMMDD.getPattern() + PatternSymbol.LINE + TimePattern.HHMMSSSSSSSS.getPattern());

		/**
		 * Example: 20180314T131423
		 */
		public final static DateTimePattern YYYYMMDD_T_HHMMSS = new DateTimePattern(
				DatePattern.YYYYMMDD.getPattern() + PatternSymbol.TIME + TimePattern.HHMMSS.getPattern());

		/**
		 * Example: 20180314T131423678
		 */
		public final static DateTimePattern YYYYMMDD_T_HHMMSSSSS = new DateTimePattern(
				DatePattern.YYYYMMDD.getPattern() + PatternSymbol.TIME + TimePattern.HHMMSSSSS.getPattern());

		/**
		 * Example: 20180314T131423678789
		 */
		public final static DateTimePattern YYYYMMDD_T_HHMMSSSSSSSS = new DateTimePattern(
				DatePattern.YYYYMMDD.getPattern() + PatternSymbol.TIME + TimePattern.HHMMSSSSSSSS.getPattern());

		/**
		 * =============================================================================================
		 */

		/**
		 * Example: 20180314 13:14:23
		 */
		public final static DateTimePattern YYYYMMDD_B_HH_MM_SS = new DateTimePattern(
				DatePattern.YYYYMMDD.getPattern() + PatternSymbol.BLANK + TimePattern.HH_MM_SS.getPattern());

		/**
		 * Example: 20180314 13:14:23.678
		 */
		public final static DateTimePattern YYYYMMDD_B_HH_MM_SS_SSS = new DateTimePattern(
				DatePattern.YYYYMMDD.getPattern() + PatternSymbol.BLANK + TimePattern.HH_MM_SS_SSS.getPattern());

		/**
		 * Example: 20180314T13:14:23.678789
		 */
		public final static DateTimePattern YYYYMMDD_B_HH_MM_SS_SSSSSS = new DateTimePattern(
				DatePattern.YYYYMMDD.getPattern() + PatternSymbol.BLANK + TimePattern.HH_MM_SS_SSSSSS.getPattern());

		/**
		 * Example: 20180314-13:14:23
		 */
		public final static DateTimePattern YYYYMMDD_L_HH_MM_SS = new DateTimePattern(
				DatePattern.YYYYMMDD.getPattern() + PatternSymbol.LINE + TimePattern.HH_MM_SS.getPattern());

		/**
		 * Example: 20180314-13:14:23.678
		 */
		public final static DateTimePattern YYYYMMDD_L_HH_MM_SS_SSS = new DateTimePattern(
				DatePattern.YYYYMMDD.getPattern() + PatternSymbol.LINE + TimePattern.HH_MM_SS_SSS.getPattern());

		/**
		 * Example: 20180314-13:14:23.678789
		 */
		public final static DateTimePattern YYYYMMDD_L_HH_MM_SS_SSSSSS = new DateTimePattern(
				DatePattern.YYYYMMDD.getPattern() + PatternSymbol.LINE + TimePattern.HH_MM_SS_SSSSSS.getPattern());

		/**
		 * Example: 20180314T13:14:23
		 */
		public final static DateTimePattern YYYYMMDD_T_HH_MM_SS = new DateTimePattern(
				DatePattern.YYYYMMDD.getPattern() + PatternSymbol.TIME + TimePattern.HH_MM_SS.getPattern());

		/**
		 * Example: 20180314T13:14:23.678
		 */
		public final static DateTimePattern YYYYMMDD_T_HH_MM_SS_SSS = new DateTimePattern(
				DatePattern.YYYYMMDD.getPattern() + PatternSymbol.TIME + TimePattern.HH_MM_SS_SSS.getPattern());

		/**
		 * Example: 20180314T13:14:23.567
		 */
		public final static DateTimePattern YYYYMMDD_T_HH_MM_SS_SSSSSS = new DateTimePattern(
				DatePattern.YYYYMMDD.getPattern() + PatternSymbol.TIME + TimePattern.HH_MM_SS_SSSSSS.getPattern());

		/**
		 * =============================================================================================
		 */

		/**
		 * Example: 2018-03-14 131423
		 */
		public final static DateTimePattern YYYY_MM_DD_B_HHMMSS = new DateTimePattern(
				DatePattern.YYYY_MM_DD.getPattern() + PatternSymbol.BLANK + TimePattern.HHMMSS.getPattern());

		/**
		 * Example: 2018-03-14 131423678
		 */
		public final static DateTimePattern YYYY_MM_DD_B_HHMMSSSSS = new DateTimePattern(
				DatePattern.YYYY_MM_DD.getPattern() + PatternSymbol.BLANK + TimePattern.HHMMSSSSS.getPattern());

		/**
		 * Example: 2018-03-14 131423678789
		 */
		public final static DateTimePattern YYYY_MM_DD_B_HHMMSSSSSSSS = new DateTimePattern(
				DatePattern.YYYY_MM_DD.getPattern() + PatternSymbol.BLANK + TimePattern.HHMMSSSSSSSS.getPattern());

		/**
		 * Example: 2018-03-14T131423
		 */
		public final static DateTimePattern YYYY_MM_DD_T_HHMMSS = new DateTimePattern(
				DatePattern.YYYY_MM_DD.getPattern() + PatternSymbol.TIME + TimePattern.HHMMSS.getPattern());

		/**
		 * Example: 2018-03-14T131423678
		 */
		public final static DateTimePattern YYYY_MM_DD_T_HHMMSSSSS = new DateTimePattern(
				DatePattern.YYYY_MM_DD.getPattern() + PatternSymbol.TIME + TimePattern.HHMMSSSSS.getPattern());

		/**
		 * Example: 2018-03-14T131423678789
		 */
		public final static DateTimePattern YYYY_MM_DD_T_HHMMSSSSSSSS = new DateTimePattern(
				DatePattern.YYYY_MM_DD.getPattern() + PatternSymbol.TIME + TimePattern.HHMMSSSSSSSS.getPattern());

		/**
		 * =============================================================================================
		 */

		/**
		 * Example: 2018-03-14 13:14:23
		 */
		public final static DateTimePattern YYYY_MM_DD_B_HH_MM_SS = new DateTimePattern(
				DatePattern.YYYY_MM_DD.getPattern() + PatternSymbol.BLANK + TimePattern.HH_MM_SS.getPattern());

		/**
		 * Example: 2018-03-14 13:14:23.678
		 */
		public final static DateTimePattern YYYY_MM_DD_B_HH_MM_SS_SSS = new DateTimePattern(
				DatePattern.YYYY_MM_DD.getPattern() + PatternSymbol.BLANK + TimePattern.HH_MM_SS_SSS.getPattern());

		/**
		 * Example: 2018-03-14 13:14:23.678789
		 */
		public final static DateTimePattern YYYY_MM_DD_B_HH_MM_SS_SSSSSS = new DateTimePattern(
				DatePattern.YYYY_MM_DD.getPattern() + PatternSymbol.BLANK + TimePattern.HH_MM_SS_SSSSSS.getPattern());

		/**
		 * Example: 2018-03-14T13:14:23
		 */
		public final static DateTimePattern YYYY_MM_DD_T_HH_MM_SS = new DateTimePattern(
				DatePattern.YYYY_MM_DD.getPattern() + PatternSymbol.TIME + TimePattern.HH_MM_SS.getPattern());

		/**
		 * Example: 2018-03-14T13:14:23.678
		 */
		public final static DateTimePattern YYYY_MM_DD_T_HH_MM_SS_SSS = new DateTimePattern(
				DatePattern.YYYY_MM_DD.getPattern() + PatternSymbol.TIME + TimePattern.HH_MM_SS_SSS.getPattern());

		/**
		 * Example: 2018-03-14T13:14:23.678789
		 */
		public final static DateTimePattern YYYY_MM_DD_T_HH_MM_SS_SSSSSS = new DateTimePattern(
				DatePattern.YYYY_MM_DD.getPattern() + PatternSymbol.TIME + TimePattern.HH_MM_SS_SSSSSS.getPattern());

		private DateTimePattern(String pattern) {
			super(pattern);
		}

	}

	public interface PatternSymbol {

		String LINE = "-";
		String BLANK = " ";
		String TIME = "T";

	}

}

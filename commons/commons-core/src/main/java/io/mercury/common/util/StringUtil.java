package io.mercury.common.util;

import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import io.mercury.common.character.Charsets;
import io.mercury.common.character.Separator;

public final class StringUtil {

	public static interface StringConst {
		
		String NULL = "null";
		
		String EMPTY = "";
		
	}

	private StringUtil() {
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	@Nonnull
	public static final String toString(@Nullable Object obj) {
		return obj == null ? StringConst.NULL : obj.toString();
	}

	/**
	 * 
	 * @param objs
	 * @return
	 */
	@Nonnull
	public static final String toString(@Nullable Object... objs) {
		if (objs == null)
			return StringConst.EMPTY;
		StringBuilder sb = new StringBuilder(objs.length * 16).append('[');
		for (int i = 0, j = objs.length - 1; i < objs.length; i++) {
			sb.append(toString(objs[i]));
			if (i < j)
				sb.append(',');
		}
		return sb.append(']').toString();
	}

	/**
	 * 
	 * @param strs
	 * @return
	 */
	@Nonnull
	public static final String toString(@Nullable String... strs) {
		if (strs == null)
			return StringConst.EMPTY;
		StringBuilder sb = new StringBuilder(strs.length * 16).append('[');
		for (int i = 0, j = strs.length - 1; i < strs.length; i++) {
			sb.append(toString(strs[i]));
			if (i < j)
				sb.append(',');
		}
		return sb.append(']').toString();
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	@Nonnull
	public static final String toString(@Nonnull byte... bytes) {
		return toString(Charsets.UTF8, bytes);
	}

	/**
	 * 
	 * @param charset
	 * @param bytes
	 * @return
	 */
	@Nonnull
	public static final String toString(@Nonnull Charset charset, @Nonnull byte... bytes) {
		return bytes == null ? StringConst.NULL : new String(bytes, charset == null ? Charsets.UTF8 : charset);
	}

	/**
	 * 
	 * @param chars
	 * @return
	 */
	@Nonnull
	public static final String toString(@Nonnull char... chars) {
		return chars == null ? StringConst.NULL : new String(chars);
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	@Nonnull
	public static final String toText(Object obj) {
		return "\"" + toString(obj) + "\"";
	}

	/**
	 * Use : <br>
	 * org.apache.commons.lang3.builder.ToStringBuilder <br>
	 * .reflectionToString(object, ToStringStyle.SHORT_PREFIX_STYLE, false)
	 * 
	 * @param obj
	 * @return String
	 * 
	 */
	@Nonnull
	public static final String toStringShortPrefixStyle(Object obj) {
		return obj == null ? StringConst.NULL
				: ToStringBuilder.reflectionToString(obj, ToStringStyle.SHORT_PREFIX_STYLE, false);
	}

	/**
	 * Use : <br>
	 * org.apache.commons.lang3.builder.ToStringBuilder <br>
	 * .reflectionToString(object, ToStringStyle.JSON_STYLE, false)
	 * 
	 * @param obj
	 * @return String
	 * 
	 */
	@Nonnull
	public static final String toStringJsonStyle(Object obj) {
		return obj == null ? StringConst.NULL
				: ToStringBuilder.reflectionToString(obj, ToStringStyle.JSON_STYLE, false);
	}

	/**
	 * 
	 * @param cs
	 * @return
	 */
	public static final boolean isNullOrEmpty(CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	/**
	 * 
	 * @param cs
	 * @return
	 */
	public static final boolean nonEmpty(CharSequence cs) {
		return cs != null && cs.length() != 0;
	}

	/**
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static final boolean isEquals(String str1, String str2) {
		return str1 != null ? str1.equals(str2) : str2 != null ? str2.equals(str1) : true;
	}

	/**
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static final boolean nonEquals(String str1, String str2) {
		return !isEquals(str1, str2);
	}

	/**
	 * 
	 */
	@Deprecated
	public static final boolean isDecimalDeprecated(String str) {
		// Character.isDigit(ch);
		if (isNullOrEmpty(str))
			return false;
		char[] chars = str.toCharArray();
		// 判断是否负数
		if (chars[0] == '-')
			chars[0] = '0';
		// 获取最后一个字符
		char lastChar = chars[chars.length - 1];
		// 判断是否为long double float的写法
		if (lastChar == 'L' || lastChar == 'l' || lastChar == 'D' || lastChar == 'd' || lastChar == 'F'
				|| lastChar == 'f')
			chars[chars.length - 1] = '0';
		// 小数点标识
		boolean decimalPointFlag = false;
		for (char ch : chars) {
			// 判断每个字母是否为数字
			if (!(ch >= '0' && ch <= '9'))
				// 出现第二个小数点返回false
				if (decimalPointFlag)
					return false;
				// 标识已出现一个小数点
				else if (ch == '.')
					decimalPointFlag = true;
				// 出现其他字符返回false
				else
					return false;
		}
		return true;
	}

	/**
	 * 检查输入参数是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static final boolean isDecimal(String str) {
		// null或空字符串
		if (isNullOrEmpty(str))
			return false;
		// 长度为1, 则判断是否是数字字符
		if (str.length() == 1)
			return str.charAt(0) >= '0' && str.charAt(0) <= '9';
		else {
			// 定义开始检查索引
			int offset = 0;
			// 判断是否负数,如果是负数,跳过第一位的检查
			if (str.charAt(0) == '-')
				offset = 1;
			// 定义结束位置
			int endPoint = str.length();
			// 获取最后一个字符
			char lastChar = str.charAt(str.length() - 1);
			// 判断是否为long double float的写法
			if (lastChar == 'L' || lastChar == 'l' || lastChar == 'D' || lastChar == 'd' || lastChar == 'F'
					|| lastChar == 'f')
				endPoint = str.length() - 1;
			// 如果没有数字可以检查且第一位与最后一位都跳过了检查, 则[offset == endPoint], 此时输入参数不是数字
			if (offset == endPoint)
				return false;
			// 是否已出现小数点
			boolean hasDecimalPoint = false;
			for (; offset < endPoint; offset++) {
				// 判断每个字母是否为数字
				char c = str.charAt(offset);
				if (!(c >= '0' && c <= '9')) {
					// 已出现小数点后再出现其他任何字符, 返回false
					if (hasDecimalPoint)
						return false;
					// 标识出现了一个小数点
					else if (c == '.')
						hasDecimalPoint = true;
					// 出现其他字符返回false
					else
						return false;
				}
			}
			return true;
		}
	}

	/**
	 * 检查输入参数是否非数字
	 * 
	 * @param str
	 * @return
	 */
	public static final boolean notDecimal(String str) {
		return !isDecimal(str);
	}

	/**
	 * 删除除字符串中的非数字
	 * 
	 * @param str
	 * @return
	 */
	@Nonnull
	public static final String removeNonDigits(String str) {
		if (isNullOrEmpty(str))
			return StringConst.EMPTY;
		StringBuilder builder = new StringBuilder(str.length());
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if ('0' <= ch && ch <= '9')
				builder.append(ch);
		}
		return builder.toString();
	}

	/**
	 * 去除分割符<b> "." </b>,<b> "-" </b>,<b> "_" </b>,<b> "/" </b>,<b> "\" </b>
	 * 
	 * @param str
	 * @return
	 */
	public static final String removeSplitChar(String str) {
		return isNullOrEmpty(str) ? StringConst.EMPTY
				: str.replace(".", "").replace("-", "").replace("_", "").replace("/", "").replace("\\", "");
	}

	/**
	 * 
	 * @param gbkStr
	 * @return
	 */
	public static final String conversionGbkToUtf8(String gbkStr) {
		return conversionTo(gbkStr, Charsets.GBK, Charsets.UTF8);
	}

	/**
	 * 
	 * @param utf8Str
	 * @return
	 */
	public static final String conversionUtf8ToGbk(String utf8Str) {
		return conversionTo(utf8Str, Charsets.UTF8, Charsets.GBK);
	}

	/**
	 * 
	 * @param gbkStr
	 * @return
	 */
	public static final String conversionGb2312ToUtf8(String gbkStr) {
		return conversionTo(gbkStr, Charsets.GB2312, Charsets.UTF8);
	}

	/**
	 * 
	 * @param utf8Str
	 * @return
	 */
	public static final String conversionUtf8ToGb2312(String utf8Str) {
		return conversionTo(utf8Str, Charsets.UTF8, Charsets.GB2312);
	}

	/**
	 * 
	 * @param sourceStr
	 * @param sourceCharset
	 * @return
	 */
	public static final String conversionToUtf8(String sourceStr, Charset sourceCharset) {
		return conversionTo(sourceStr, sourceCharset, Charsets.UTF8);
	}

	/**
	 * 
	 * @param sourceStr
	 * @param sourceCharset
	 * @param targetCharset
	 * @return
	 */
	public static final String conversionTo(String sourceStr, Charset sourceCharset, Charset targetCharset) {
		return sourceStr == null ? sourceStr : new String(sourceStr.getBytes(sourceCharset), targetCharset);
	}

	/**
	 * 是否为路径
	 * 
	 * @param path
	 * @return
	 */
	public static final boolean isPath(String path) {
		return isNullOrEmpty(path) ? false : path.endsWith("/") || path.endsWith("\\");
	}

	/**
	 * 是否为路径
	 * 
	 * @param path
	 * @return
	 */
	public static final boolean notPath(String path) {
		return !isPath(path);
	}

	/**
	 * 修补路径
	 * 
	 * @param path
	 * @return
	 */
	public static final String fixPath(String path) {
		return isNullOrEmpty(path) ? "/"
				: path.endsWith("/") || path.endsWith("\\") ? path : path + Separator.FILE_SEPARATOR;
	}

	/**
	 * 使用','将字符串连接
	 * 
	 * @param strs
	 * @return
	 */
	public static final String concatenateStr(String... strs) {
		return concatenateStr(strs.length * 16 + strs.length, ',', strs);
	}

	/**
	 * 指定缓冲区长度, 使用','将字符串连接
	 * 
	 * @param capacity 缓冲区长度
	 * @param strs     字符串数组
	 * @return
	 */
	public static final String concatenateStr(int capacity, String... strs) {
		return concatenateStr(capacity, ',', strs);
	}

	/**
	 * 使用指定符号将字符串连接
	 * 
	 * @param symbol 连接符号
	 * @param strs   字符串数组
	 * @return
	 */
	public static final String concatenateStr(char symbol, String... strs) {
		return concatenateStr(strs.length * 16 + strs.length, symbol, strs);
	}

	/**
	 * 指定缓冲区长度, 使用指定符号将字符串连接
	 * 
	 * @param capacity 缓冲区长度
	 * @param symbol   连接符号
	 * @param strs     字符串数组
	 * @return
	 */
	public static final String concatenateStr(int capacity, char symbol, String... strs) {
		if (strs == null || strs.length == 0)
			return StringConst.EMPTY;
		StringBuilder builder = new StringBuilder(capacity);
		for (int i = 0; i < strs.length; i++) {
			builder.append(strs[i]);
			if (i < strs.length - 1)
				builder.append(symbol);
		}
		return builder.toString();
	}

	/**
	 * Get 7-bit ASCII character array from input String. The lower 7 bits of each
	 * character in the input string is assumed to be the ASCII character value.
	 * 
	 * <pre>
	 Hexadecimal - Character
	
	 | 00 NUL| 01 SOH| 02 STX| 03 ETX| 04 EOT| 05 ENQ| 06 ACK| 07 BEL|
	 | 08 BS | 09 HT | 0A NL | 0B VT | 0C NP | 0D CR | 0E SO | 0F SI |
	 | 10 DLE| 11 DC1| 12 DC2| 13 DC3| 14 DC4| 15 NAK| 16 SYN| 17 ETB|
	 | 18 CAN| 19 EM | 1A SUB| 1B ESC| 1C FS | 1D GS | 1E RS | 1F US |
	 | 20 SP | 21  ! | 22  " | 23  # | 24  $ | 25  % | 26  & | 27  ' |
	 | 28  ( | 29  ) | 2A  * | 2B  + | 2C  , | 2D  - | 2E  . | 2F  / |
	 | 30  0 | 31  1 | 32  2 | 33  3 | 34  4 | 35  5 | 36  6 | 37  7 |
	 | 38  8 | 39  9 | 3A  : | 3B  ; | 3C  < | 3D  = | 3E  > | 3F  ? |
	 | 40  @ | 41  A | 42  B | 43  C | 44  D | 45  E | 46  F | 47  G |
	 | 48  H | 49  I | 4A  J | 4B  K | 4C  L | 4D  M | 4E  N | 4F  O |
	 | 50  P | 51  Q | 52  R | 53  S | 54  T | 55  U | 56  V | 57  W |
	 | 58  X | 59  Y | 5A  Z | 5B  [ | 5C  \ | 5D  ] | 5E  ^ | 5F  _ |
	 | 60  ` | 61  a | 62  b | 63  c | 64  d | 65  e | 66  f | 67  g |
	 | 68  h | 69  i | 6A  j | 6B  k | 6C  l | 6D  m | 6E  n | 6F  o |
	 | 70  p | 71  q | 72  r | 73  s | 74  t | 75  u | 76  v | 77  w |
	 | 78  x | 79  y | 7A  z | 7B  { | 7C  | | 7D  } | 7E  ~ | 7F DEL|
	 * </pre>
	 */
	public static byte[] getAsciiBytes(String input) {
		char[] c = input.toCharArray();
		byte[] b = new byte[c.length];
		for (int i = 0; i < c.length; i++) {
			b[i] = (byte) (c[i] & 0x007F);
		}
		return b;
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public static String getAsciiString(byte[] input) {
		StringBuffer buf = new StringBuffer(input.length);
		for (byte b : input) {
			buf.append((char) b);
		}
		return buf.toString();
	}

	public static void main(String[] args) {

		System.out.println(fixPath(null));
		System.out.println(fixPath("ddd"));
		System.out.println(fixPath("/user/"));

		System.out.println(isDecimal("-l"));
		System.out.println(isDecimal("877F"));
		System.out.println(isDecimal("877l"));
		System.out.println(isDecimal("877S"));
		System.out.println(isDecimal("877d"));
		System.out.println(isDecimal("877D"));
		System.out.println(isDecimal("-877"));
		System.out.println(isDecimal("-877L"));
		System.out.println(isDecimal("8.77L"));
		System.out.println(isDecimal(".877"));
		System.out.println(isDecimal("-.877"));
		System.out.println(isDecimal("-.87.7"));

		System.out.println(concatenateStr("A", "BB", "CCC"));
		System.out.println(concatenateStr("A", "BB", null));
		System.out.println(concatenateStr("A", "BB", "", null, "null"));
		System.out.println(concatenateStr("A", "BB", "", null));
		System.out.println(concatenateStr('%', "A", "BB", "", null));
		System.out.println(concatenateStr(null, null));

		System.out.println(removeNonDigits("fn909aje125f13de3132fde31dew"));

	}

}

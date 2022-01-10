package io.mercury.common.character;

import static java.nio.charset.Charset.defaultCharset;

import java.nio.charset.Charset;

import io.mercury.common.util.StringSupport;

public interface Charsets {

	static Charset forName(String charsetName) {
		if (StringSupport.isNullOrEmpty(charsetName))
			return SYS_DEFAULT;
		try {
			return Charset.forName(charsetName);
		} catch (Exception e) {
			return SYS_DEFAULT;
		}
	}

	Charset SYS_DEFAULT = defaultCharset();

	Charset ASCII = Charset.forName("ASCII");

	Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

	Charset UTF8 = Charset.forName("UTF-8");

	Charset UTF16 = Charset.forName("UTF-16");

	Charset UTF32 = Charset.forName("UTF-32");

	Charset GBK = Charset.forName("GBK");

	Charset GB2312 = Charset.forName("GB2312");

}

package io.mercury.common.character;

import static java.nio.charset.Charset.forName;

import java.nio.charset.Charset;

public interface Charsets {

	Charset ASCII = forName("ASCII");

	Charset ISO_8859_1 = forName("ISO-8859-1");

	Charset UTF8 = forName("UTF-8");

	Charset UTF16 = forName("UTF-16");

	Charset UTF32 = forName("UTF-32");

	Charset GBK = forName("GBK");

	Charset GB2312 = forName("GB2312");

}

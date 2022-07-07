package io.mercury.common.character;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import io.mercury.common.util.StringSupport;

public interface Charsets {

    /**
     * @param charsetName String
     * @return Charset
     */
    static Charset forName(String charsetName) {
        if (StringSupport.isNullOrEmpty(charsetName))
            return SYS_DEFAULT;
        return Charset.forName(charsetName);
    }

    Charset SYS_DEFAULT = Charset.defaultCharset();

    Charset ASCII = StandardCharsets.US_ASCII;

    Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;

    Charset UTF8 = StandardCharsets.UTF_8;

    Charset UTF16 = StandardCharsets.UTF_16;

    Charset UTF32 = Charset.forName("UTF-32");

    Charset BIG5 = Charset.forName("Big5");

    Charset GBK = Charset.forName("GBK");

    Charset GB2312 = Charset.forName("GB2312");

    static void main(String[] args) {
        Charset.availableCharsets().entrySet().forEach(System.out::println);
    }

}

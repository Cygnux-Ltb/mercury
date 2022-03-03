package io.mercury.common.character;

public final class UnsupportedCharsetsException extends RuntimeException {

    public UnsupportedCharsetsException(String charsetName) {
        super("Cannot find charset : [" + charsetName + "]");
    }

}

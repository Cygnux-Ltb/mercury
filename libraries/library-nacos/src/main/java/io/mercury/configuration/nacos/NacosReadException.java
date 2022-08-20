package io.mercury.configuration.nacos;

import java.io.Serial;

public final class NacosReadException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4866898838811364365L;

    public NacosReadException(String message) {
        super(message);
    }

    public NacosReadException(Throwable e) {
        super(e.getMessage(), e);
    }

    public NacosReadException(String message, Throwable e) {
        super(message, e);
    }

}

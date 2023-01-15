package com.linkedin.avro.fastserde;

import java.io.Serial;

public class SchemaAssistantException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6322376693674783661L;

    public SchemaAssistantException(String message, Throwable cause) {
        super(message, cause);
    }

    public SchemaAssistantException(String message) {
        super(message);
    }

    public SchemaAssistantException(Throwable cause) {
        super(cause);
    }
}

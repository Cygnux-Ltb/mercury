package io.mercury.library.ignite.user.model;

public record RespResult<T>(
        String statusCode,
        String message,
        T data) {
}

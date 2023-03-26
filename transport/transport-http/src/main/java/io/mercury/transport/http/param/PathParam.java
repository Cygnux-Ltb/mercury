package io.mercury.transport.http.param;

public record PathParam(
        String name,
        Object value) {

    @Override
    public String toString() {
        return name + "=" + value;
    }

}
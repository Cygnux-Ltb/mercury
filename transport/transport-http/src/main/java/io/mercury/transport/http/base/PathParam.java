package io.mercury.transport.http.base;

public final class PathParam {

    private final String name;
    private final String value;

    /**
     * @param name
     * @param value
     */
    public PathParam(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name + "=" + value;
    }

}
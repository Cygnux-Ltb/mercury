package io.mercury.common.http;

import io.mercury.common.collections.MutableMaps;
import org.eclipse.collections.api.map.MutableMap;

public final class HttpHeaders {

    public record HttpHeader(
            HttpHeaderName name,
            String value
    ) {
    }

    private final MutableMap<HttpHeaderName, String> headers = MutableMaps.newUnifiedMap();

    private HttpHeaders() {
    }

    public static HttpHeaders empty() {
        return new HttpHeaders();
    }

    public static HttpHeaders with(HttpHeader... headers) {
        return new HttpHeaders();
    }

    public HttpHeaders addHeader(HttpHeader... headers) {

        return this;
    }

    public HttpHeaders addHeader(HttpHeaderName name, String value) {
        headers.put(name, value);
        return this;
    }

}

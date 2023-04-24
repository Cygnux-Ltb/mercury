package io.mercury.common.http;

import io.mercury.common.http.PathParams.PathParam;
import io.mercury.common.log4j2.Log4j2LoggerFactory;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.Authenticator;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

public abstract class JreHttpClient {

    private static final Logger log = Log4j2LoggerFactory.getLogger(JreHttpClient.class);

    private static final HttpClient HC = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(12))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .authenticator(Authenticator.getDefault())
            .build();

    private JreHttpClient() {
    }

    public static HttpResponse<String> GET(@Nonnull String uri, PathParam... params)
            throws IOException, InterruptedException, URISyntaxException {
        return GET(uri, PathParams.with(params));
    }

    public static HttpResponse<String> GET(@Nonnull String uri, PathParams params)
            throws IOException, InterruptedException, URISyntaxException {
        return GET(params.toFullUri(uri));
    }

    public static HttpResponse<String> GET(@Nonnull URI uri)
            throws IOException, InterruptedException {
        return GET(HttpRequest.newBuilder()
                .GET().uri(uri)
                .headers(HttpHeaderName.CONTENT_TYPE.value(), MimeType.APPLICATION_JSON_UTF8)
                .build());
    }

    public static HttpResponse<String> GET(@Nonnull HttpRequest request)
            throws IOException, InterruptedException {
        return HC.send(// GET request
                request, BodyHandlers.ofString());
    }

    public static HttpResponse<String> POST(@Nonnull URI uri, String body)
            throws IOException, InterruptedException {
        return HC.send(// POST request
                HttpRequest.newBuilder().uri(uri)
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .header(HttpHeaderName.CONTENT_TYPE.value(), MimeType.APPLICATION_JSON_UTF8).build(),
                BodyHandlers.ofString());
    }


    public static HttpResponse<String> PUT(@Nonnull URI uri, String body)
            throws IOException, InterruptedException {
        return HC.send(// PUT request
                HttpRequest.newBuilder().uri(uri)
                        .PUT(HttpRequest.BodyPublishers.ofString(body))
                        .header(HttpHeaderName.CONTENT_TYPE.value(), MimeType.APPLICATION_JSON_UTF8).build(),
                BodyHandlers.ofString());
    }


    public static HttpResponse<String> DELETE(URI uri)
            throws IOException, InterruptedException {
        return HC.send(// DELETE request
                HttpRequest.newBuilder().DELETE().uri(uri).build(),
                BodyHandlers.ofString());
    }


}

package io.mercury.transport.http;

import io.mercury.common.log.Log4j2LoggerFactory;
import io.mercury.transport.http.param.HeaderParams;
import io.mercury.transport.http.param.HttpParam;
import io.mercury.transport.http.param.PathParams;
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
import java.util.Map;

public abstract class JreHttpClient {

    private static final Logger log = Log4j2LoggerFactory.getLogger(JreHttpClient.class);

    public static final HttpClient HC = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(12))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .authenticator(Authenticator.getDefault())
            .build();

    private JreHttpClient() {
    }


    public static String GET(@Nonnull String url, HttpParam... params)
            throws IOException, InterruptedException {
        return GET(url + PathParams.newInstance().addParams(params));
    }

    public static String GET(@Nonnull String url, PathParams paramSet)
            throws IOException, InterruptedException {
        return GET(url + paramSet.toUrlParams());
    }


    public static String GET(@Nonnull String url)
            throws IOException, InterruptedException {
        return GET(HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .GET().build());
    }


    public static String GET(@Nonnull HttpRequest request)
            throws IOException, InterruptedException {
        return HC.send(request, BodyHandlers.ofString()).body();
    }


    public static String PUT(@Nonnull URI uri, Map<String, String> params)
            throws IOException, InterruptedException {
        // PUT request
        HttpRequest putRequest = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .PUT(HttpRequest.BodyPublishers.ofString(buildQueryString(params)))
                .build();
        HttpResponse<String> putResponse = HC.send(putRequest, BodyHandlers.ofString());
        System.out.println("PUT response: " + putResponse.body());
        return "";
    }


    /**
     * @param uri    URI
     * @param params Map<String, String>
     * @return String
     */
    public static String POST(@Nonnull URI uri, HeaderParams params)
            throws IOException, InterruptedException {
        // POST request
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(buildQueryString(null)))
                .build();
        HttpResponse<String> postResponse = HC.send(postRequest, BodyHandlers.ofString());
        System.out.println("POST response: " + postResponse.body());

        return null;
    }

    public static String DELETE(String url) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(new URI(url))
                .DELETE()
                .build();
        HttpResponse<String> deleteResponse = HC.send(deleteRequest, BodyHandlers.ofString());
        System.out.println("DELETE response: " + deleteResponse.body());
        return url;
    }


    private static String buildQueryString(Map<String, String> parameters) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("&");
        }
        return sb.toString();
    }


}

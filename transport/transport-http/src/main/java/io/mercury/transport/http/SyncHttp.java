package io.mercury.transport.http;

import io.mercury.common.log.Log4j2LoggerFactory;
import io.mercury.transport.http.param.PathParam;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.Authenticator;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static java.time.Duration.ofSeconds;

public abstract class SyncHttp {

    private static final Logger log = Log4j2LoggerFactory.getLogger(SyncHttp.class);

    public static final HttpClient HC = HttpClient.newBuilder()
            .version(Version.HTTP_1_1)
            .followRedirects(Redirect.NORMAL)
            .connectTimeout(ofSeconds(10))
            .authenticator(Authenticator.getDefault())
            .build();

    private SyncHttp() {
    }

    /**
     * @param url    String
     * @param params PathParam[]
     * @return String
     * @throws IOException          ioe
     * @throws InterruptedException e
     */
    public static String sentGet(@Nonnull String url, PathParam... params)
            throws IOException, InterruptedException {
        // TODO add path params
        return sentGet(url);
    }

    /**
     * @param url String
     * @return String
     * @throws IOException          ioe
     * @throws InterruptedException e
     */
    public static String sentGet(@Nonnull String url)
            throws IOException, InterruptedException {
        return sentGet(HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .GET().build());
    }


    /**
     * @param request HttpRequest
     * @return String
     * @throws IOException          ioe
     * @throws InterruptedException e
     */
    public static String sentGet(@Nonnull HttpRequest request)
            throws IOException, InterruptedException {
        return HC.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    /**
     * @param url    String
     * @param params Map<String, String>
     * @return String
     */
    public static String sentPut(@Nonnull String url, Map<String, String> params) {
        return "";
    }


    /**
     * @param url    String
     * @param params Map<String, String>
     * @return String
     */
    public static String sentPost(@Nonnull String url, Map<String, String> params) {
        return "";
    }


}

package io.mercury.transport.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.jsoup.HttpStatusException;
import org.slf4j.Logger;

import io.mercury.common.log.Log4j2LoggerFactory;
import io.mercury.transport.http.base.PathParam;

public abstract class SyncHttp {

    private static final Logger log = Log4j2LoggerFactory.getLogger(SyncHttp.class);

    // CloseableHttpClient
    public static final CloseableHttpClient HC = HttpClients.createDefault();

    private SyncHttp() {
    }

    /**
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static String sentGet(@Nonnull String url, List<PathParam> params) throws IOException {
        // TODO add path params
        return sentGet(url);
    }

    /**
     * @param url
     * @return
     * @throws IOException
     */
    public static String sentGet(@Nonnull String url) throws IOException {
        final HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpResponse rsp = HC.execute(httpGet)) {
            int code = rsp.getCode();
            if (code > 307) {
                log.error("url -> {} | status -> {} : {} ", url, code, rsp.getReasonPhrase());
                throw new HttpStatusException("Sent GET request throw HttpStatusException", code, url);
            }
            final HttpEntity entity = rsp.getEntity();
            try {
                String content = EntityUtils.toString(entity);
                return content != null ? content : "";
            } catch (Exception e) {
                log.error("url -> {} | entity parsing exception -> {}", e.getMessage(), e);
                return "";
            } finally {
                EntityUtils.consume(entity);
            }

        } catch (IOException e) {
            log.error("", e);
            throw e;
        }
    }

    /**
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static String sentPut(@Nonnull String url, Map<String, String> params) throws IOException, ParseException {
        final HttpPut put = new HttpPut(url);
        put.setEntity(new UrlEncodedFormEntity(Optional.ofNullable(params).orElse(new HashMap<>()).entrySet().stream()
                .map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue())).collect(Collectors.toList())));
        try (CloseableHttpResponse rsp = HC.execute(put)) {
            log.info("Put URL -> {}, rspCode==[{}], rspReasonPhrase==[{}]", url, rsp.getCode(), rsp.getReasonPhrase());
            return handleHttpEntity(rsp.getEntity());
        } catch (IOException | ParseException e) {
            log.error("Put URL -> {}, Has Exception -> {}", url, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * @return
     * @throws IOException
     */
    public static String sentPost(@Nonnull String url, Map<String, String> params) throws IOException, ParseException {
        HttpPost post = new HttpPost(url);
        post.setEntity(new UrlEncodedFormEntity(Optional.ofNullable(params).orElse(new HashMap<>()).entrySet().stream()
                .map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue())).collect(Collectors.toList())));
        try (CloseableHttpResponse rsp = HC.execute(post)) {
            log.info("Post URL -> {}, rspCode==[{}], rspReasonPhrase==[{}]", url, rsp.getCode(), rsp.getReasonPhrase());
            return handleHttpEntity(rsp.getEntity());
        } catch (IOException | ParseException e) {
            log.error("Put URL -> {}, Has Exception -> {}", url, e.getMessage(), e);
            throw e;
        }
    }

    private static String handleHttpEntity(HttpEntity entity) throws IOException, ParseException {
        if (entity == null)
            return "";
        String str = EntityUtils.toString(entity);
        EntityUtils.consume(entity);
        return str;
    }

}

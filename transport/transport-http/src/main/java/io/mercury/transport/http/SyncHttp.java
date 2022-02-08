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
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public final static String sentGet(@Nonnull String url, List<PathParam> params) throws IOException {
		// TODO add path params
		return sentGet(url);
	}

	/**
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public final static String sentGet(@Nonnull String url) throws IOException {
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
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public final static String sentPut(@Nonnull String url, Map<String, String> params) {
		final HttpPut put = new HttpPut(url);

		put.setEntity(new UrlEncodedFormEntity(Optional.ofNullable(params).orElse(new HashMap<>()).entrySet().stream()
				.map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue())).collect(Collectors.toList())));

		try (CloseableHttpResponse rsp = HC.execute(put)) {
			System.out.println(rsp.getCode() + " " + rsp.getReasonPhrase());
			HttpEntity entity2 = rsp.getEntity();
			EntityUtils.consume(entity2);
		} catch (IOException e) {
			// TODO: handle exception
		}
		return "";
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public final static String sentPost(@Nonnull String url, Map<String, String> params) {

		HttpPost post = new HttpPost(url);

		post.setEntity(new UrlEncodedFormEntity(Optional.ofNullable(params).orElse(new HashMap<>()).entrySet().stream()
				.map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue())).collect(Collectors.toList())));

		try (CloseableHttpResponse rsp = HC.execute(post)) {
			System.out.println(rsp.getCode() + " " + rsp.getReasonPhrase());
			HttpEntity entity = rsp.getEntity();
			EntityUtils.consume(entity);
		} catch (IOException e) {
			// TODO: handle exception
		}
		return "";
	}

}

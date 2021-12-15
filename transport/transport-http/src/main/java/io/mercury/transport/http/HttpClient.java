package io.mercury.transport.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.jsoup.HttpStatusException;
import org.slf4j.Logger;

import io.mercury.common.log.Log4j2LoggerFactory;

public final class HttpClient {

	private static final Logger log = Log4j2LoggerFactory.getLogger(HttpClients.class);

	private static final CloseableHttpClient HC = HttpClients.createDefault();

	public final static String httpGet(@Nonnull String url) throws IOException {
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

	public final static String httpPut(@Nonnull String url, Map<String, Object> params) throws IOException {

		HttpPut httpPut = new HttpPut(url);

		List<NameValuePair> nvps = new ArrayList<>();
		nvps.add(new BasicNameValuePair("username", "vip"));
		nvps.add(new BasicNameValuePair("password", "secret"));
		httpPut.setEntity(new UrlEncodedFormEntity(nvps));

		try (CloseableHttpResponse response2 = HC.execute(httpPut)) {
			System.out.println(response2.getCode() + " " + response2.getReasonPhrase());
			HttpEntity entity2 = response2.getEntity();
			EntityUtils.consume(entity2);
		}
		return "";
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public final static String httpPost(@Nonnull String url, Map<String, Object> params) throws IOException {

		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<>();
		nvps.add(new BasicNameValuePair("username", "vip"));
		nvps.add(new BasicNameValuePair("password", "secret"));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));

		try (CloseableHttpResponse response2 = HC.execute(httpPost)) {
			System.out.println(response2.getCode() + " " + response2.getReasonPhrase());
			HttpEntity entity2 = response2.getEntity();
			EntityUtils.consume(entity2);
		}
		return "";
	}

	public static void main(String[] args) {

	}

}

package io.mercury.transport.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
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

import io.mercury.common.log.CommonLoggerFactory;

public final class HttpClient {

	private static final Logger log = CommonLoggerFactory.getLogger(HttpClients.class);

	private static final CloseableHttpClient HC = HttpClients.createDefault();

	public final static String httpGet(@Nonnull String url) throws HttpStatusException {
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
			// TODO Auto-generated catch block
			log.error("");
			return "";
		}
	}

	public final static String httpPost() throws IOException {

		// The underlying HTTP connection is still held by the response object
		// to allow the response content to be streamed directly from the network
		// socket.
		// In order to ensure correct deallocation of system resources
		// the user MUST call CloseableHttpResponse#close() from a finally clause.
		// Please note that if response content is not fully consumed the underlying
		// connection cannot be safely re-used and will be shut down and discarded
		// by the connection manager.

		HttpPost httpPost = new HttpPost("http://httpbin.org/post");
		List<NameValuePair> nvps = new ArrayList<>();
		nvps.add(new BasicNameValuePair("username", "vip"));
		nvps.add(new BasicNameValuePair("password", "secret"));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));

		try (CloseableHttpResponse response2 = HC.execute(httpPost)) {
			System.out.println(response2.getCode() + " " + response2.getReasonPhrase());
			HttpEntity entity2 = response2.getEntity();
			// do something useful with the response body
			// and ensure it is fully consumed
			EntityUtils.consume(entity2);
		}
		return "";
	}

}

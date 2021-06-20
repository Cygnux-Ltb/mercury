package io.mercury.transport.http;

import java.io.IOException;

import org.jsoup.HttpStatusException;
import org.slf4j.Logger;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.serialization.json.JsonWrapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public final class HttpRequester2 {

	private static final Logger log = CommonLoggerFactory.getLogger(HttpRequester2.class);

	// OkHttpClient
	private static final OkHttpClient HttpClient = new OkHttpClient();

	private HttpRequester2() {
	}

	/**
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String sentGet(String url) throws HttpStatusException, IOException {
		Request req = new Request.Builder().url(url).build();
		try (Response rsp = HttpClient.newCall(req).execute()) {
			if (rsp.code() > 307)
				throw new HttpStatusException("Sent GET request throw HttpStatusException", rsp.code(), url);
			ResponseBody body = rsp.body();
			if (body == null)
				return "";
			return body.string();
		} catch (IOException e) {
			log.error("IOException -> {}", e.getMessage(), e);
			throw e;
		}
	}

	//
	private static final MediaType ApplicationJson = MediaType.get("application/json; charset=utf-8");

	/**
	 * 
	 * @param url
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static String sentPost(String url, Object obj) throws HttpStatusException, IOException {
		RequestBody body = RequestBody.create(JsonWrapper.toJson(obj), ApplicationJson);
		Request req = new Request.Builder().url(url).post(body).build();
		try (Response rsp = HttpClient.newCall(req).execute()) {
			if (rsp.code() > 307)
				throw new HttpStatusException("Sent POST request throw HttpStatusException", rsp.code(), url);
			ResponseBody rspBody = rsp.body();
			if (rspBody == null)
				return "";
			return rspBody.string();
		} catch (IOException e) {
			log.error("IOException -> {}", e.getMessage(), e);
			throw e;
		}
	}

}

package io.mercury.transport.http;

import java.io.IOException;

import org.slf4j.Logger;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.serialization.json.JsonUtil;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpRequester {

	private static final Logger log = CommonLoggerFactory.getLogger(HttpRequester.class);

	private static final OkHttpClient CLIENT = new OkHttpClient();

	private HttpRequester() {
	}

	public static String sentGet(String url) {
		Request request = new Request.Builder().url(url).build();
		try (Response response = CLIENT.newCall(request).execute()) {
			if (response == null)
				throw new RuntimeException("RuntimeException -> Request Url: [" + url + "]");
			if (response.code() > 307)
				throw new RuntimeException(
						"RuntimeException -> Request Url: [" + url + "], return status code: " + response.code());
			ResponseBody body = response.body();
			if (body == null)
				return "";
			return body.string();
		} catch (IOException e) {
			log.error("IOException -> {}", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	private static final MediaType APPLICATION_JSON = MediaType.get("application/json; charset=utf-8");

	public static String sentPost(String url, Object obj) throws IOException {
		RequestBody body = RequestBody.create(JsonUtil.toJson(obj), APPLICATION_JSON);
		Request request = new Request.Builder().url(url).post(body).build();
		try (Response response = CLIENT.newCall(request).execute()) {
			if (response == null)
				throw new RuntimeException("RuntimeException -> Request Url: [" + url + "]");
			if (response.code() > 307)
				throw new RuntimeException(
						"RuntimeException -> Request Url: [" + url + "] return status code: " + response.code());
			ResponseBody rspBody = response.body();
			if (rspBody == null)
				return "";
			return rspBody.string();
		} catch (IOException e) {
			log.error("IOException -> {}", e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

}

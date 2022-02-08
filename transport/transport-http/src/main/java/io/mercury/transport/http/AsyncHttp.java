package io.mercury.transport.http;

import static org.asynchttpclient.Dsl.asyncHttpClient;

import org.asynchttpclient.AsyncHttpClient;
import org.slf4j.Logger;

import io.mercury.common.log.Log4j2LoggerFactory;

public abstract class AsyncHttp {

	@SuppressWarnings("unused")
	private static final Logger log = Log4j2LoggerFactory.getLogger(SyncHttp.class);

	public static final AsyncHttpClient AHC = asyncHttpClient();

	private AsyncHttp() {
	}

}

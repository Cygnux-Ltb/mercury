package io.mercury.transport.http;

import static org.asynchttpclient.Dsl.asyncHttpClient;

import org.asynchttpclient.AsyncHttpClient;

public enum AsyncHttp {

	;

	public static final AsyncHttpClient INSTANCE = asyncHttpClient();


}

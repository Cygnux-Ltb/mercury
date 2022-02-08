package io.mercury.transport.http.ws;

import org.asynchttpclient.ws.WebSocket;

import io.mercury.common.functional.ThrowableHandler;

public interface WebSocketEvent {

	@FunctionalInterface
	interface WsOpenHandler {
		void handle(WebSocket webSocket);
	}

	@FunctionalInterface
	interface WsCloseHandler {
		void handle(WebSocket webSocket, int code, String reason);
	}

	@FunctionalInterface
	interface WsPingFrameHandler {
		void handle(byte[] payload);

	}

	@FunctionalInterface
	interface WsPongFrameHandler {
		void handle(byte[] payload);

	}

	@FunctionalInterface
	interface WsBinaryFrameHandler {
		void handle(byte[] payload, boolean finalFragment, int rsv);

	}

	@FunctionalInterface
	interface WsTextFrameHandler {
		void handle(String payload, boolean finalFragment, int rsv);
	}

	@FunctionalInterface
	interface WsThrowableHandler extends ThrowableHandler<Throwable> {

	}

}

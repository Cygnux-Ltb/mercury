package io.mercury.transport.http.ws;

import io.mercury.common.functional.Handler;
import io.mercury.common.functional.ThrowableHandler;
import org.asynchttpclient.ws.WebSocket;

public interface WebSocketHandler {

    @FunctionalInterface
    interface WsOpenHandler extends Handler<WebSocket> {
    }

    @FunctionalInterface
    interface WsCloseHandler {
        void handle(WebSocket webSocket, int code, String reason);
    }

    @FunctionalInterface
    interface WsPingFrameHandler extends Handler<byte[]> {
    }

    @FunctionalInterface
    interface WsPongFrameHandler extends Handler<byte[]> {
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

package io.mercury.transport.http.ws;

import io.mercury.common.log4j2.Log4j2LoggerFactory;
import io.mercury.transport.http.ws.WebSocketHandler.WsBinaryFrameHandler;
import io.mercury.transport.http.ws.WebSocketHandler.WsCloseHandler;
import io.mercury.transport.http.ws.WebSocketHandler.WsOpenHandler;
import io.mercury.transport.http.ws.WebSocketHandler.WsPingFrameHandler;
import io.mercury.transport.http.ws.WebSocketHandler.WsPongFrameHandler;
import io.mercury.transport.http.ws.WebSocketHandler.WsTextFrameHandler;
import io.mercury.transport.http.ws.WebSocketHandler.WsThrowableHandler;
import org.asynchttpclient.ws.WebSocket;
import org.asynchttpclient.ws.WebSocketListener;
import org.asynchttpclient.ws.WebSocketUpgradeHandler.Builder;
import org.slf4j.Logger;

import java.util.concurrent.ExecutionException;

import static io.mercury.transport.http.AsyncHttpClient.AHC;

public final class WebSocketClient {




    private static final Logger log = Log4j2LoggerFactory.getLogger(WebSocketClient.class);

    /**
     * @param uri                WebSocket地址
     * @param openHandler        打开连接处理函数
     * @param closeHandler       关闭连接处理函数
     * @param pingFrameHandler   Ping帧处理函数
     * @param pongFrameHandler   Pong帧处理函数
     * @param binaryFrameHandler 字节数组帧处理函数
     * @param textFrameHandler   文本帧处理函数
     * @param throwableHandler   异常处理函数
     */
    public static void connect(
            // WebSocket地址
            String uri,
            // [打开/关闭] 连接处理函数
            WsOpenHandler openHandler,
            WsCloseHandler closeHandler,
            // [Ping/Pong] 帧处理函数
            WsPingFrameHandler pingFrameHandler,
            WsPongFrameHandler pongFrameHandler,
            // [二进制/文本] 帧处理函数
            WsBinaryFrameHandler binaryFrameHandler,
            WsTextFrameHandler textFrameHandler,
            // 异常处理函数
            WsThrowableHandler throwableHandler) {
        try {
            WebSocket webSocket = AHC.prepareGet(uri)
                    .execute(new Builder().addWebSocketListener(
                            new WebSocketListenerImpl(
                                    // WebSocket地址
                                    uri,
                                    // [打开/关闭] 连接处理函数
                                    openHandler, closeHandler,
                                    // [Ping/Pong] 帧处理函数
                                    pingFrameHandler, pongFrameHandler,
                                    // [二进制/文本] 帧处理函数
                                    binaryFrameHandler, textFrameHandler,
                                    // 异常处理函数
                                    throwableHandler
                            )).build())
                    .get();
            log.info("Ws uri -> {}, Open==[{}]", uri, webSocket.isOpen());
        } catch (InterruptedException | ExecutionException e) {
            log.error("Connect ws uri -> {}, has exception : {}", uri, e.getMessage(), e);
        }
    }


    record WebSocketListenerImpl(
            // WebSocket地址
            String uri,
            // [打开/关闭] 连接处理函数
            WsOpenHandler openHandler,
            WsCloseHandler closeHandler,
            // [Ping/Pong] 帧处理函数
            WsPingFrameHandler pingFrameHandler,
            WsPongFrameHandler pongFrameHandler,
            // [二进制/文本] 帧处理函数
            WsBinaryFrameHandler binaryFrameHandler,
            WsTextFrameHandler textFrameHandler,
            // 异常处理函数
            WsThrowableHandler throwableHandler)
            implements WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket) {
            if (openHandler != null)
                openHandler.handle(webSocket);
            else
                log.warn("Connect uri -> {}, WsOpenHandler is null", uri);
        }

        @Override
        public void onClose(WebSocket webSocket, int code, String reason) {
            if (closeHandler != null)
                closeHandler.handle(webSocket, code, reason);
            else
                log.warn("");
        }

        @Override
        public void onPingFrame(byte[] payload) {
            if (pingFrameHandler != null)
                pingFrameHandler.handle(payload);
            else
                log.warn("");
        }

        @Override
        public void onPongFrame(byte[] payload) {
            if (pongFrameHandler != null)
                pongFrameHandler.handle(payload);
            else
                log.warn("");
        }

        @Override
        public void onBinaryFrame(byte[] payload, boolean finalFragment, int rsv) {
            if (binaryFrameHandler != null)
                binaryFrameHandler.handle(payload, finalFragment, rsv);
            else
                log.warn("");
        }

        @Override
        public void onTextFrame(String payload, boolean finalFragment, int rsv) {
            if (textFrameHandler != null)
                textFrameHandler.handle(payload, finalFragment, rsv);
            else
                log.warn("");
        }

        @Override
        public void onError(Throwable t) {
            if (throwableHandler != null)
                throwableHandler.handle(t);
            else
                log.error("");
        }

    }

}

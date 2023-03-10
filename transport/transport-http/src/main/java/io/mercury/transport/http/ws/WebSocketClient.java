package io.mercury.transport.http.ws;

import io.mercury.common.log.Log4j2LoggerFactory;
import io.mercury.transport.http.AsyncHttp;
import io.mercury.transport.http.ws.WebSocketHandler.WsBinaryFrameHandler;
import io.mercury.transport.http.ws.WebSocketHandler.WsCloseHandler;
import io.mercury.transport.http.ws.WebSocketHandler.WsOpenHandler;
import io.mercury.transport.http.ws.WebSocketHandler.WsPingFrameHandler;
import io.mercury.transport.http.ws.WebSocketHandler.WsPongFrameHandler;
import io.mercury.transport.http.ws.WebSocketHandler.WsTextFrameHandler;
import io.mercury.transport.http.ws.WebSocketHandler.WsThrowableHandler;
import org.asynchttpclient.ws.WebSocket;
import org.asynchttpclient.ws.WebSocketListener;
import org.asynchttpclient.ws.WebSocketUpgradeHandler;
import org.slf4j.Logger;

import java.util.concurrent.ExecutionException;

public final class WebSocketClient {

    private static final Logger log = Log4j2LoggerFactory.getLogger(WebSocketClient.class);

    /**
     * @param url                WebSocket地址
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
            String url,
            // 打开连接处理函数
            WsOpenHandler openHandler,
            // 关闭连接处理函数
            WsCloseHandler closeHandler,
            // Ping帧处理函数
            WsPingFrameHandler pingFrameHandler,
            // Pong帧处理函数
            WsPongFrameHandler pongFrameHandler,
            // 字节数组帧处理函数
            WsBinaryFrameHandler binaryFrameHandler,
            // 文本帧处理函数
            WsTextFrameHandler textFrameHandler,
            // 异常处理函数
            WsThrowableHandler throwableHandler) {
        try {
            WebSocket webSocket = AsyncHttp.AHC.prepareGet(url).execute(new WebSocketUpgradeHandler
                    .Builder()
                    .addWebSocketListener(new WebSocketListener() {

                        @Override
                        public void onOpen(WebSocket webSocket) {
                            if (openHandler != null)
                                openHandler.handle(webSocket);
                        }

                        @Override
                        public void onClose(WebSocket webSocket, int code, String reason) {
                            if (closeHandler != null)
                                closeHandler.handle(webSocket, code, reason);
                        }

                        @Override
                        public void onPingFrame(byte[] payload) {
                            if (pingFrameHandler != null)
                                pingFrameHandler.handle(payload);
                        }

                        @Override
                        public void onPongFrame(byte[] payload) {
                            if (pongFrameHandler != null)
                                pongFrameHandler.handle(payload);
                        }

                        @Override
                        public void onBinaryFrame(byte[] payload, boolean finalFragment, int rsv) {
                            if (binaryFrameHandler != null)
                                binaryFrameHandler.handle(payload, finalFragment, rsv);
                        }

                        @Override
                        public void onTextFrame(String payload, boolean finalFragment, int rsv) {
                            if (textFrameHandler != null)
                                textFrameHandler.handle(payload, finalFragment, rsv);
                        }

                        @Override
                        public void onError(Throwable t) {
                            if (throwableHandler != null)
                                throwableHandler.handle(t);
                        }

                    }).build()).get();
            log.info("Url -> {}, Open==[{}]", url, webSocket.isOpen());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

}

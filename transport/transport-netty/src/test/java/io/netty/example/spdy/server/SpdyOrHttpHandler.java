/*
 * Copyright 2014 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.example.spdy.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.spdy.SpdyFrameCodec;
import io.netty.handler.codec.spdy.SpdyHttpDecoder;
import io.netty.handler.codec.spdy.SpdyHttpEncoder;
import io.netty.handler.codec.spdy.SpdyHttpResponseStreamIdHandler;
import io.netty.handler.codec.spdy.SpdySessionHandler;
import io.netty.handler.codec.spdy.SpdyVersion;
import io.netty.handler.ssl.ApplicationProtocolNames;
import io.netty.handler.ssl.ApplicationProtocolNegotiationHandler;

/**
 * Negotiates with the browser if SPDY or HTTP is going to be used. Once
 * decided, the Netty pipeline is set up with the correct handlers for the
 * selected protocol.
 */
public class SpdyOrHttpHandler extends ApplicationProtocolNegotiationHandler {

    private static final int MAX_CONTENT_LENGTH = 1024 * 100;

    protected SpdyOrHttpHandler() {
        super(ApplicationProtocolNames.HTTP_1_1);
    }

    @Override
    protected void configurePipeline(ChannelHandlerContext ctx, String protocol) {
        if (ApplicationProtocolNames.SPDY_3_1.equals(protocol)) {
            configureSpdy(ctx, SpdyVersion.SPDY_3_1);
            return;
        }

        if (ApplicationProtocolNames.HTTP_1_1.equals(protocol)) {
            configureHttp1(ctx);
            return;
        }

        throw new IllegalStateException("unknown protocol: " + protocol);
    }

    private static void configureSpdy(ChannelHandlerContext ctx, SpdyVersion version) {
        ctx.pipeline()
                .addLast(new SpdyFrameCodec(version))
                .addLast(new SpdySessionHandler(version, true))
                .addLast(new SpdyHttpEncoder(version))
                .addLast(new SpdyHttpDecoder(version, MAX_CONTENT_LENGTH))
                .addLast(new SpdyHttpResponseStreamIdHandler())
                .addLast(new SpdyServerHandler());
    }

    private static void configureHttp1(ChannelHandlerContext ctx) {
        ctx.pipeline()
                .addLast(new HttpServerCodec())
                .addLast(new HttpObjectAggregator(MAX_CONTENT_LENGTH))
                .addLast(new SpdyServerHandler());
    }
}

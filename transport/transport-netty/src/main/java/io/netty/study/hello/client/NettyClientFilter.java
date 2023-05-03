package io.netty.study.hello.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Title: NettyClientFilter
 * Description:
 * Netty客户端 过滤器
 * Version:1.0.0
 *
 * @author pancm
 * @date 2017-8-31
 */
public class NettyClientFilter extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline()
                /*
                 * 解码和编码，应和服务端一致
                 * */
                .addLast("framer",
                        new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()))
                .addLast("decoder", new StringDecoder())
                .addLast("encoder", new StringEncoder())
                .addLast("handler", new NettyClientHandler()); //客户端的逻辑
    }
}

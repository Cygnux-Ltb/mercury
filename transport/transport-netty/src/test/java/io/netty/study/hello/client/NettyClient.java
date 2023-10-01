package io.netty.study.hello.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;

/**
 * Title: NettyClient
 * Description:
 * Netty客户端
 * Version:1.0.0
 *
 * @author pancm
 * @date 2017-8-31
 */
public class NettyClient {

    public static String host = "127.0.0.1";  //ip地址
    public static int port = 6789;          //端口
    /// 通过nio方式来接收连接和处理连接   
    private static final EventLoopGroup GROUP = new NioEventLoopGroup();
    private static final Bootstrap BOOTSTRAP = new Bootstrap();
    private static Channel channel;

    /**
     * Netty创建全部都是实现自AbstractBootstrap。
     * 客户端的是Bootstrap，服务端的则是    ServerBootstrap。
     **/
    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("客户端成功启动...");
        BOOTSTRAP.group(GROUP);
        BOOTSTRAP.channel(NioSocketChannel.class);
        BOOTSTRAP.handler(new NettyClientFilter());
        // 连接服务端
        channel = BOOTSTRAP.connect(host, port).sync().channel();
        start();
    }

    public static void start() {
        String str = "Hello Netty";
        channel.writeAndFlush(str + "\r\n");
        System.out.println("客户端发送数据:" + str);
    }

}
package io.netty.study.http.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;


/**
 * Title: NettyServer
 * Description: Netty服务端  Http测试
 * Version:1.0.0
 *
 * @author pancm
 * @date 2017年10月26日
 */
public class NettyServer {

    //设置服务端端口
    private static final int port = 6789;

    // 通过nio方式来接收连接和处理连接
    private static final EventLoopGroup GROUP = new NioEventLoopGroup();

    private static final ServerBootstrap BOOTSTRAP = new ServerBootstrap();

    /**
     * Netty创建全部都是实现自AbstractBootstrap。
     * 客户端的是Bootstrap，服务端的则是	ServerBootstrap。
     **/
    public static void main(String[] args) throws InterruptedException {
        try {
            BOOTSTRAP.group(GROUP);
            BOOTSTRAP.channel(NioServerSocketChannel.class);
            BOOTSTRAP.childHandler(new NettyServerFilter()); //设置过滤器
            // 服务器绑定端口监听
            ChannelFuture f = BOOTSTRAP.bind(port).sync();
            System.out.println("服务端启动成功,端口是:" + port);
            // 监听服务器关闭监听
            f.channel().closeFuture().sync();
        } finally {
            GROUP.shutdownGracefully(); //关闭EventLoopGroup，释放掉所有资源包括创建的线程
        }
    }
}

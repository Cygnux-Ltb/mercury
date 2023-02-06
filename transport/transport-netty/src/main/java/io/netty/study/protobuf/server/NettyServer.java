package io.netty.study.protobuf.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author pancm
 * @version : 1.0.0
 * @description : Netty服务端
 * @date 2017年10月8日
 */
public class NettyServer {
    private static final int port = 9876; // 设置服务端端口
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(); // 通过nio方式来接收连接和处理连接
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(); // 通过nio方式来接收连接和处理连接
    private static final ServerBootstrap bootstrap = new ServerBootstrap();

    public void run() {
        try {
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new NettyServerFilter()); // 设置过滤器
            // 服务器绑定端口监听
            ChannelFuture f = bootstrap.bind(port).sync();
            System.out.println("服务端启动成功,端口是:" + port);
            // 监听服务器关闭监听
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭EventLoopGroup，释放掉所有资源包括创建的线程
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

}

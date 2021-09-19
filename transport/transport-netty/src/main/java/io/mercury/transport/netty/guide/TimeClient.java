package io.mercury.transport.netty.guide;

import java.util.Date;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {

	private final String host;
	private final int port;

	private TimeClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void connect() throws InterruptedException {
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap(); // (1)
			/*
			 * BootStrap 和 ServerBootstrap 类似, 不过他是对非服务端的 channel 而言, 比如客户端或者无连接传输模式的 channel
			 */
			bootstrap.group(workerGroup); // (2)
			bootstrap.channel(NioSocketChannel.class); // (3)
			bootstrap.option(ChannelOption.SO_KEEPALIVE, true); // (4)
			bootstrap.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new TimeClientHandler());
				}
			});

			// 启动客户端
			ChannelFuture future = bootstrap.connect(host, port).sync(); // (5)

			// 等待连接关闭
			future.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}

	private static class TimeClientHandler extends ChannelInboundHandlerAdapter {

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) {
			ByteBuf m = (ByteBuf) msg; // (1)
			try {
				long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
				System.out.println(new Date(currentTimeMillis));
				ctx.close();
			} finally {
				m.release();
			}
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
			cause.printStackTrace();
			ctx.close();
		}
	}

	public static void main(String[] args) throws Exception {
		new TimeClient("127.0.0.1", 8080).connect();
	}

}
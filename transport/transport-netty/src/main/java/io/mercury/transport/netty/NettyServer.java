package io.mercury.transport.netty;

import io.mercury.transport.core.api.TransportServer;
import io.mercury.transport.netty.configurator.NettyConfigurator;
import io.mercury.transport.netty.handler.GeneralNettyHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer extends NettyTransport implements TransportServer {

	private EventLoopGroup bossGroup;
	private ServerBootstrap serverBootstrap;

	/**
	 * 
	 * @param tag
	 * @param configurator
	 * @param channelHandlers
	 */
	public NettyServer(String tag, NettyConfigurator configurator, ChannelHandler... channelHandlers) {
		super(tag, configurator, channelHandlers);
	}

	@Override
	protected void init() {
		this.bossGroup = new NioEventLoopGroup();
		this.serverBootstrap = new ServerBootstrap();
		this.serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel socketChannel) throws Exception {
						socketChannel.pipeline().addLast(channelHandlers);
					}
				}).option(ChannelOption.SO_BACKLOG, configurator.backlog())
				.childOption(ChannelOption.SO_KEEPALIVE, configurator.keepAlive())
				.childOption(ChannelOption.TCP_NODELAY, configurator.tcpNoDelay());
		log.info(tag + " : Init-ServerBootStrap.bind -> " + configurator.port());
	}

	@Override
	public void startup() {
		try {
			// Start server.
			serverBootstrap.bind(configurator.host(), configurator.port()).sync()
					// Wait close.
					.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			log.error("NettyServer method startup() -> {}", e.getMessage(), e);
			destroy();
		}
	}

	@Override
	public boolean destroy() {
		log.info("NettyServer call method destroy().");
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
		return true;
	}

	@Override
	public boolean isConnected() {
		return bossGroup.isShutdown() && workerGroup.isShutdown();
	}

	public static void main(String[] args) throws Exception {

		NettyConfigurator configurator = NettyConfigurator.builder().host("192.168.1.138").port(7901).build();

		NettyServer nettyServer = new NettyServer("LocalTest", configurator, new GeneralNettyHandler() {

			@Override
			public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
				sendBytes(ctx, "hello".getBytes());
			}

			@Override
			public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
				byte[] recvBytes = getRecvBytes((ByteBuf) msg);
				System.out.println(new String(recvBytes));
			}

		});

		nettyServer.startup();

	}

}
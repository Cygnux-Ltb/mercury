package io.mercury.transport.netty;

import org.slf4j.Logger;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.transport.api.TransportServer;
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

	private static final Logger log = CommonLoggerFactory.getLogger(NettyServer.class);

	private EventLoopGroup bossGroup;
	private ServerBootstrap bootstrap;

	/**
	 * 
	 * @param tag
	 * @param configurator
	 * @param handlers
	 */
	public NettyServer(String tag, NettyConfigurator configurator, ChannelHandler... handlers) {
		super(tag, configurator, handlers);
	}

	@Override
	protected void init() {
		this.bossGroup = new NioEventLoopGroup();
		this.bootstrap = new ServerBootstrap();
		this.bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel channel) throws Exception {
						channel.pipeline().addLast(handlers);
					}
				}).option(ChannelOption.SO_BACKLOG, configurator.backlog())
				.childOption(ChannelOption.SO_KEEPALIVE, configurator.keepAlive())
				.childOption(ChannelOption.TCP_NODELAY, configurator.tcpNoDelay());
		log.info("{} : Init-ServerBootStrap.bind -> {}", tag, configurator.getConnectionInfo());
	}

	@Override
	public void startup() {
		try {
			// Create a new Channel and bind it.
			bootstrap.bind(configurator.getHost(), configurator.getPort()).sync()
					// Returns a channel where the I/O operation associated with this future takes
					// place.
					.channel()
					// Returns the ChannelFuture which will be notified when this channel is closed.
					// This method always returns the same future instance.
					.closeFuture()
					// Waits for this future until it is done, and rethrows the cause of the failure
					// if this future failed.
					.sync();
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

		NettyConfigurator configurator = NettyConfigurator.builder("192.168.1.138", 7901).build();

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
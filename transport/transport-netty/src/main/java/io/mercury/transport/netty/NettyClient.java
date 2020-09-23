package io.mercury.transport.netty;

import io.mercury.transport.core.api.TransportClient;
import io.mercury.transport.netty.configurator.NettyConfigurator;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient extends NettyTransport implements TransportClient {

	private Bootstrap bootstrap;

	/**
	 * 
	 * @param tag
	 * @param configurator
	 * @param channelHandlers
	 */
	public NettyClient(String tag, NettyConfigurator configurator, ChannelHandler... channelHandlers) {
		super(tag, configurator, channelHandlers);
	}

	@Override
	protected void init() {
		this.bootstrap = new Bootstrap();
		this.bootstrap.group(workerGroup).channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel channel) throws Exception {
						channel.pipeline().addLast(handlers);
					}
				}).option(ChannelOption.SO_KEEPALIVE, configurator.keepAlive())
				.option(ChannelOption.TCP_NODELAY, configurator.tcpNoDelay());
		log.info(tag + ": Init-BootStrap.connect -> " + configurator.port());

	}

	@Override
	public void connect() {
		try {
			// Start the client.
			// Connect a Channel to the remote peer.
			bootstrap.connect(configurator.host(), configurator.port()).sync()
					// Wait until the connection is closed.
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
			log.error("NettyClient method connection() -> {}", e.getMessage(), e);
			destroy();
		}
	}

	@Override
	public boolean isConnected() {
		return workerGroup.isShutdown();
	}

	@Override
	public boolean destroy() {
		log.info("NettyClient call method destroy().");
		workerGroup.shutdownGracefully();
		return true;
	}

}
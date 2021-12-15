package io.mercury.transport.netty;

import org.slf4j.Logger;

import io.mercury.common.log.Log4j2LoggerFactory;
import io.mercury.transport.api.TransportClient;
import io.mercury.transport.netty.configurator.NettyConfigurator;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient extends NettyTransport implements TransportClient {

	private static final Logger log = Log4j2LoggerFactory.getLogger(NettySender.class);

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
		log.info("{} : Init-BootStrap.connect -> {}", tag, configurator.getConnectionInfo());
	}

	@Override
	public void connect() {
		try {
			// Start the client.
			bootstrap.connect(configurator.getHost(), configurator.getPort())
					// Connect a Channel to the remote peer.
					.sync()
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
			closeIgnoreException();
		}
	}

	@Override
	public boolean isConnected() {
		return workerGroup.isShutdown();
	}

	@Override
	public boolean closeIgnoreException() {
		log.info("NettyClient call method destroy().");
		workerGroup.shutdownGracefully();
		newEndTime();
		return true;
	}


}
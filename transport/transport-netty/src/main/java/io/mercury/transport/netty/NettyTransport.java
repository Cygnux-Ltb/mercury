package io.mercury.transport.netty;

import static io.mercury.common.sys.CurrentRuntime.availableProcessors;

import org.slf4j.Logger;

import io.mercury.common.annotation.lang.ProtectedAbstractMethod;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.util.Assertor;
import io.mercury.transport.netty.configurator.NettyConfigurator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public abstract class NettyTransport {

	protected String tag;
	protected NettyConfigurator configurator;

	protected final EventLoopGroup workerGroup;

	protected final ChannelHandler[] channelHandlers;

	protected final Logger log = CommonLoggerFactory.getLogger(getClass());

	public NettyTransport(String tag, NettyConfigurator configurator, ChannelHandler... channelHandlers) {
		this.tag = tag;
		this.configurator = Assertor.nonNull(configurator, "configurator");
		this.channelHandlers = Assertor.requiredLength(channelHandlers, 1, "channelHandlers");
		this.workerGroup = new NioEventLoopGroup(availableProcessors() * 2 - availableProcessors() / 2);
		init();
	}

	@ProtectedAbstractMethod
	protected abstract void init();

	public String name() {
		return tag;
	}

}

package io.mercury.transport.netty;

import static io.mercury.common.sys.CurrentRuntime.availableProcessors;

import io.mercury.common.annotation.lang.AbstractFunction;
import io.mercury.common.util.Assertor;
import io.mercury.transport.netty.configurator.NettyConfigurator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public abstract class NettyTransport {

	protected String tag;
	protected NettyConfigurator configurator;

	protected final ChannelHandler[] handlers;
	protected final EventLoopGroup workerGroup;

	protected NettyTransport(String tag, NettyConfigurator configurator, ChannelHandler... handlers) {
		Assertor.nonNull(configurator, "configurator");
		Assertor.requiredLength(handlers, 1, "handlers");
		this.tag = tag;
		this.configurator = configurator;
		this.handlers = handlers;
		this.workerGroup = new NioEventLoopGroup(availableProcessors() * 2 - availableProcessors() / 2);
		init();
	}

	@AbstractFunction
	protected abstract void init();

	public String name() {
		return tag;
	}

}

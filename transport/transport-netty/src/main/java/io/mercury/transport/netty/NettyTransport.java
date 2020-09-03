package io.mercury.transport.netty;

import static io.mercury.common.sys.CurrentRuntime.availableProcessors;

import org.slf4j.Logger;

import io.mercury.common.annotation.lang.AbstractFunction;
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

	protected final ChannelHandler[] handlers;

	protected final Logger log = CommonLoggerFactory.getLogger(getClass());

	public NettyTransport(String tag, NettyConfigurator configurator, ChannelHandler... handlers) {
		this.tag = tag;
		Assertor.nonNull(configurator, "configurator");
		Assertor.requiredLength(handlers, 1, "handlers");
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

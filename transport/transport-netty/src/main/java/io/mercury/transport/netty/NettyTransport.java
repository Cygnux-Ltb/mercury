package io.mercury.transport.netty;

import static io.mercury.common.sys.CurrentRuntime.availableProcessors;

import io.mercury.common.annotation.AbstractFunction;
import io.mercury.common.lang.Asserter;
import io.mercury.transport.api.Transport;
import io.mercury.transport.api.TransportComponent;
import io.mercury.transport.netty.configurator.NettyConfigurator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public abstract class NettyTransport extends TransportComponent implements Transport {

    protected final String tag;
    protected final NettyConfigurator configurator;

    protected final ChannelHandler[] handlers;
    protected final EventLoopGroup workerGroup;

    /**
     * @param tag          String
     * @param configurator NettyConfigurator
     * @param handlers     ChannelHandler[]
     */
    protected NettyTransport(String tag, NettyConfigurator configurator, ChannelHandler... handlers) {
        Asserter.nonNull(configurator, "configurator");
        Asserter.requiredLength(handlers, 1, "handlers");
        this.tag = tag;
        this.configurator = configurator;
        this.handlers = handlers;
        this.workerGroup = new NioEventLoopGroup(availableProcessors() * 2 - availableProcessors() / 2);
        init();
        newStartTime();
    }

    @AbstractFunction
    protected abstract void init();

    @Override
    public String getName() {
        return tag;
    }

}

package io.mercury.transport.netty;

import org.slf4j.Logger;

import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.sequence.NanoSequence;
import io.mercury.transport.core.api.Sender;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

public class NettySender implements Sender<byte[]> {

	private static final Logger log = CommonLoggerFactory.getLogger(NettySender.class);

	private ChannelHandlerContext context;

	// private ByteBuf byteBuf;

	public NettySender(ChannelHandlerContext context) {
		this.context = context;
		// this.byteBuf = byteBuf;
	}

	// public NettySender(ChannelHandlerContext context) {
	// this(context);
	// }

	@Override
	public boolean isConnected() {
		context.disconnect();
		return true;
	}

	@Override
	public boolean destroy() {
		// byteBuf.release();
		context.disconnect();
		context.close();
		return true;
	}

	@Override
	public String name() {
		return "NettySender-ContextHashCode:" + context.hashCode() + "&";// + byteBuf.capacity();
	}

	@Override
	public void send(byte[] msg) {
		log.debug(NanoSequence.micro() + " call sender send -> data length : " + msg.length);
		ByteBuf byteBuf = context.alloc().buffer(msg.length);
		byteBuf.writeBytes(msg);
		ChannelFuture writeAndFlush = context.writeAndFlush(byteBuf.retain());
		writeAndFlush.addListener(future -> {
			log.debug("{} call sender send operation complete -> data length : {}", NanoSequence.micro(),
					byteBuf.writerIndex());
			byteBuf.clear();
			byteBuf.release();

		});
	}

}
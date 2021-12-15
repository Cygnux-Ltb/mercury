package io.mercury.transport.netty;

import org.slf4j.Logger;

import io.mercury.common.log.Log4j2LoggerFactory;
import io.mercury.common.sequence.LinearSequence;
import io.mercury.transport.api.Sender;
import io.mercury.transport.api.TransportComponent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

public class NettySender extends TransportComponent implements Sender<byte[]> {

	private static final Logger log = Log4j2LoggerFactory.getLogger(NettySender.class);

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
	public boolean closeIgnoreException() {
		// byteBuf.release();
		context.disconnect();
		context.close();
		newEndTime();
		return true;
	}

	@Override
	public String getName() {
		return "NettySender-ContextHashCode:" + context.hashCode() + "&";// + byteBuf.capacity();
	}

	@Override
	public void sent(byte[] msg) {
		log.debug(LinearSequence.getMicros() + " call sender send -> data length : " + msg.length);
		ByteBuf byteBuf = context.alloc().buffer(msg.length);
		byteBuf.writeBytes(msg);
		ChannelFuture writeAndFlush = context.writeAndFlush(byteBuf.retain());
		writeAndFlush.addListener(future -> {
			log.debug("{} call sender send operation complete -> data length : {}", LinearSequence.getMicros(),
					byteBuf.writerIndex());
			byteBuf.clear();
			byteBuf.release();
		});
	}

}
package io.mercury.transport.netty.handler;


import org.slf4j.Logger;

import io.mercury.common.log.CommonLoggerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public abstract class GeneralNettyHandler extends ChannelInboundHandlerAdapter {

	protected int byteBufSize;

	protected final Logger log = CommonLoggerFactory.getLogger(getClass());

	/**
	 * Use default bytebufSize;
	 * 
	 * Size = 1024 * 8.
	 */
	public GeneralNettyHandler() {
		this(1024 * 8);
	}

	public GeneralNettyHandler(int byteBufSize) {
		this.byteBufSize = byteBufSize;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		log.error(cause.getMessage() , cause);
		ctx.close();
	}

	protected byte[] getRecvBytes(ByteBuf byteBuf) {
		byte[] bytes = new byte[byteBuf.readableBytes()];
		byteBuf.retain().readBytes(bytes);
		byteBuf.release();
		return bytes;
	}

	protected ByteBuf allocByteBuf(ChannelHandlerContext ctx) {
		return allocByteBuf(ctx, this.byteBufSize);
	}

	protected ByteBuf allocByteBuf(ChannelHandlerContext ctx, int byteBufSize) {
		return ctx.alloc().buffer(byteBufSize);
	}

	protected void sendBytes(ChannelHandlerContext ctx, byte[] bytes) {
		ByteBuf byteBuf = ctx.alloc().buffer(bytes.length);
		byteBuf.writeBytes(bytes);
		ctx.writeAndFlush(byteBuf.retain()).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				byteBuf.clear();
				byteBuf.release();
			}
		});
	}

}

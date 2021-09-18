package io.mercury.transport.netty.guide;

import io.mercury.common.character.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 实现 ECHO 协议, 返回所有收到的数据. <br>
 * <br>
 * Handler(处理器) 实现，Handler 是由 Netty 生成用来处理 I/O 事件.
 * 
 * @author yellow013
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter { // (1)
	/*
	 * 1) EchoServerHandler 继承自 ChannelInboundHandlerAdapter.
	 * 
	 * 这个类实现了 ChannelInboundHandler 接口, ChannelInboundHandler 提供了许多事件处理的接口方法,
	 * 然后你可以覆盖这些方法.
	 * 
	 * 现在仅仅只需要继承 ChannelInboundHandlerAdapter 类而不是你自己去实现接口方法.
	 */

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
		/*
		 * 2) 覆盖 chanelRead() 事件处理方法.
		 * 
		 * 每当从客户端收到新的数据时, 这个方法会在收到消息时被调用, 这个例子中, 收到的消息的类型是 ByteBuf
		 */
		ByteBuf buf = ((ByteBuf) msg);
		System.out.println(buf);
		System.out.println(buf.toString(Charsets.UTF8)); // 输出接收到的字符

		ctx.write(buf); // (3)
		/*
		 * 3) ChannelHandlerContext 对象提供了许多操作, 使你能够触发各种各样的 I/O 事件和操作.
		 * 
		 * 这里我们调用了 write(Object) 方法来逐字地把接受到的消息写入.
		 * 
		 * 请注意不同于 DISCARD 的例子我们并没有释放接受到的消息, 这是因为当写入的时候 Netty 已经帮我们释放了.
		 */

		ctx.flush(); // (4)
		/*
		 * 4) ctx.write(Object) 方法不会使消息写入到通道上, 他被缓冲在了内部, 你需要调用 ctx.flush()
		 * 方法来把缓冲区中数据强行输出.
		 * 
		 * 或者你可以用更简洁的 cxt.writeAndFlush(msg) 以达到同样的目的.
		 */
	}

	/**
	 * 连接激活事件
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("call channelActive");
		super.channelActive(ctx);
	}

	/**
	 * 连接断开事件
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("call channelInactive");
		super.channelInactive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (5)
		/*
		 * 4) exceptionCaught() 事件处理方法是当出现 Throwable 对象才会被调用, 即当 Netty 由于 IO
		 * 错误或者处理器在处理事件时抛出的异常时.
		 * 
		 * 在大部分情况下, 捕获的异常应该被记录下来并且把关联的 channel 给关闭掉.
		 * 
		 * 然而这个方法的处理方式会在遇到不同异常的情况下有不同的实现, 比如你可能想在关闭连接之前发送一个错误码的响应消息.
		 */
		cause.printStackTrace(); // 当出现异常就关闭连接
		ctx.close();
	}
}
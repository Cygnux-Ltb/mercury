package io.netty.study.slidingwindow;

import io.netty.study.slidingwindow.client.NettyClient;
import io.netty.study.slidingwindow.server.NettyServer;

/**
 * Hello world!
 *
 */
public class Bootstrap {
	
	public static void main(String[] args) {

		// 启动服务端和客户端
		new Thread(() -> new NettyServer().run()).start();
		new Thread(() -> new NettyClient().run()).start();

	}
}

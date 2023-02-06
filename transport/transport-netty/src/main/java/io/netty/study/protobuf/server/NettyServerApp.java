package io.netty.study.protobuf.server;

/**
 * @author pancm
 * @version : 1.0.0
 * @description : Netty 服务端主程序
 * @date 2018年7月11日
 */
public class NettyServerApp {

    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer();
        nettyServer.run();
    }

}

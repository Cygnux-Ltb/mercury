package io.netty.study.protobuf.client;

/**
 * @author pancm
 * @version : 1.0.0
 * @description : Netty 客户端主程序
 * @date 2018年7月11日
 */
public class NettyClientApp {

    /**
     * @param args String[]
     */
    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient();
        nettyClient.run();
    }

}

package io.spridra.rpc.test.consumer.codec.init;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.spridra.rpc.codec.RpcDecoder;
import io.spridra.rpc.codec.RpcEncoder;
import io.spridra.rpc.test.consumer.codec.handler.RpcTestConsumerHandler;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-28 11:28
 * @Describe: Netty客户端Channel初始化类
 * @Version: 1.0
 */

public class RpcTestConsumerInitializer extends ChannelInitializer<SocketChannel> {
    /**
     * Netty 在每次建立新连接时都会调用
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline cp = socketChannel.pipeline();
        cp.addLast(new RpcEncoder());
        cp.addLast(new RpcDecoder());
        cp.addLast(new RpcTestConsumerHandler());
    }
}

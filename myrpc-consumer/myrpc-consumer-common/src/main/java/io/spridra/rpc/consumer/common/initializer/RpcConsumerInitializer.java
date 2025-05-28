package io.spridra.rpc.consumer.common.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.spridra.rpc.codec.RpcDecoder;
import io.spridra.rpc.codec.RpcEncoder;
import io.spridra.rpc.consumer.common.handler.RpcConsumerHandler;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-28 16:28
 * @Describe: 消费者处理器初始化类
 * @Version: 1.0
 */

public class RpcConsumerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline cp = channel.pipeline();
        cp.addLast(new RpcEncoder());
        cp.addLast(new RpcDecoder());
        cp.addLast(new RpcConsumerHandler());
    }
}

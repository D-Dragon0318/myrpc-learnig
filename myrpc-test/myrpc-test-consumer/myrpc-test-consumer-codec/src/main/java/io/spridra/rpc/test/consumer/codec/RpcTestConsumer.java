package io.spridra.rpc.test.consumer.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.spridra.rpc.test.consumer.codec.init.RpcTestConsumerInitializer;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-28 11:30
 * @Describe: 测试消费端
 * @Version: 1.0
 */

public class RpcTestConsumer {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
        try{
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new RpcTestConsumerInitializer());
            bootstrap.connect("127.0.0.1", 27880).sync();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            Thread.sleep(2000);
            eventLoopGroup.shutdownGracefully();
        }
    }
}

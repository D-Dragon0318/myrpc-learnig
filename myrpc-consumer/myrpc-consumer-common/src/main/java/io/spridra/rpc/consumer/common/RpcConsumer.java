package io.spridra.rpc.consumer.common;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.spridra.rpc.consumer.common.handler.RpcConsumerHandler;
import io.spridra.rpc.consumer.common.initializer.RpcConsumerInitializer;
import io.spridra.rpc.protocol.RpcProtocol;
import io.spridra.rpc.protocol.request.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-28 16:29
 * @Describe: 消费者
 * @Version: 1.0
 */

public class RpcConsumer {

    private final Logger logger = LoggerFactory.getLogger(RpcConsumer.class);
    //Netty 客户端启动器
    private final Bootstrap bootstrap;
    //Netty 线程组
    private final EventLoopGroup eventLoopGroup;
    //单例对象（双重检查锁）
    private static volatile RpcConsumer instance;
    //缓存服务地址 -> 连接处理器的映射
    private static Map<String, RpcConsumerHandler> handlerMap = new ConcurrentHashMap<>();

    private RpcConsumer() {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup(4);
        bootstrap.group(eventLoopGroup)
                //客户端TCP
                .channel(NioSocketChannel.class)
                .handler(new RpcConsumerInitializer());
    }

    public static RpcConsumer getInstance(){
        /*
          实现线程安全的懒加载单例模式；
          确保整个应用中只创建一个 RpcConsumer 实例和一个 EventLoopGroup。
         */
        if (instance == null){
            synchronized (RpcConsumer.class){
                if (instance == null){
                    instance = new RpcConsumer();
                }
            }
        }
        return instance;
    }

    public void close(){
        eventLoopGroup.shutdownGracefully();
    }

    public Object sendRequest(RpcProtocol<RpcRequest> protocol) throws Exception {
        //TODO 暂时写死，后续在引入注册中心时，从注册中心获取
        String serviceAddress = "127.0.0.1";
        int port = 27880;
        //构建唯一key
        String key = serviceAddress.concat("_").concat(String.valueOf(port));
        RpcConsumerHandler handler = handlerMap.get(key);
        //缓存中无RpcClientHandler
        if (handler == null){
            handler = getRpcConsumerHandler(serviceAddress, port);
            handlerMap.put(key, handler);
        }else if (!handler.getChannel().isActive()){  //缓存中存在RpcClientHandler，但不活跃
            handler.close();
            handler = getRpcConsumerHandler(serviceAddress, port);
            handlerMap.put(key, handler);
        }
        return handler.sendRequest(protocol);
    }

    /**
     * 创建连接并返回RpcClientHandler
     */
    private RpcConsumerHandler getRpcConsumerHandler(String serviceAddress, int port) throws InterruptedException {
        //建立连接
        ChannelFuture channelFuture = bootstrap.connect(serviceAddress, port).sync();
        channelFuture.addListener((ChannelFutureListener) listener -> {
            if (channelFuture.isSuccess()) {
                logger.info("connect rpc server {} on port {} success.", serviceAddress, port);
            } else {
                logger.error("connect rpc server {} on port {} failed.", serviceAddress, port);
                channelFuture.cause().printStackTrace();
                eventLoopGroup.shutdownGracefully();
            }
        });
        return channelFuture.channel().pipeline().get(RpcConsumerHandler.class);
    }
}

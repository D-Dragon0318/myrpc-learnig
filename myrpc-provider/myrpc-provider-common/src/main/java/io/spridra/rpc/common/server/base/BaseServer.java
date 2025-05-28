package io.spridra.rpc.common.server.base;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.spridra.rpc.codec.RpcDecoder;
import io.spridra.rpc.codec.RpcEncoder;
import io.spridra.rpc.common.handler.RpcProviderHandler;
import io.spridra.rpc.common.server.api.Server;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-27 11:35
 * @Describe: 服务提供端
 * @Version: 1.0
 */

public class BaseServer implements Server {

    private final Logger logger = LoggerFactory.getLogger(BaseServer.class);
    //主机域名或者IP地址
    protected String host = "127.0.0.1";
    //端口号
    protected int port = 27110;
    //反射类型
    private final String reflectType;
    //存储的是实体类关系
    protected Map<String, Object> handlerMap = new HashMap<>();

    public BaseServer(String serverAddress,String reflectType){
        if (!StringUtils.isEmpty(serverAddress)){
            String[] serverArray = serverAddress.split(":");
            this.host = serverArray[0];
            this.port = Integer.parseInt(serverArray[1]);
        }
        this.reflectType = reflectType;
    }

    @Override
    public void startNettyServer() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>(){
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    //实现自定义协议
                                    // .addLast(new StringDecoder())
                                    .addLast(new RpcDecoder())
                                    // .addLast(new StringEncoder())
                                    .addLast(new RpcEncoder())
                                    .addLast(new RpcProviderHandler(reflectType,handlerMap));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.bind(host, port).sync();
            logger.info("Server started on port {}", port);
            future.channel().closeFuture().sync();
        }catch (Exception e){
            logger.error("RPC Server start error", e);
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}

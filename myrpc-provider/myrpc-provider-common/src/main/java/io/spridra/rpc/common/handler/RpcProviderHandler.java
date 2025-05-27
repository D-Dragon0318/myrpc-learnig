package io.spridra.rpc.common.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-27 11:08
 * @Describe: 消息收发处理器
 * @Version: 1.0
 */

public class RpcProviderHandler extends SimpleChannelInboundHandler<Object> {
    private final Logger logger = LoggerFactory.getLogger(RpcProviderHandler.class);

    private final Map<String, Object> handlerMap;

    public RpcProviderHandler(Map<String, Object> handlerMap){
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
        logger.info("RPC提供者收到的数据为====>>> {}", o.toString());
        logger.info("handlerMap中存放的数据如下所示：");
        for(Map.Entry<String, Object> entry : handlerMap.entrySet()){
            logger.info("{} === {}", entry.getKey(), entry.getValue());
        }
        //直接返回数据
        ctx.writeAndFlush(o);
    }
}

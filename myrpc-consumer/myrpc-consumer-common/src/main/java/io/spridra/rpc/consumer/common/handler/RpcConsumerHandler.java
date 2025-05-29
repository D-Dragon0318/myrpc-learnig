package io.spridra.rpc.consumer.common.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.spridra.rpc.protocol.RpcProtocol;
import io.spridra.rpc.protocol.header.RpcHeader;
import io.spridra.rpc.protocol.request.RpcRequest;
import io.spridra.rpc.protocol.response.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-28 16:26
 * @Describe: RPC消费者处理器
 * @Version: 1.0
 */

public class RpcConsumerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {
    private final Logger logger = LoggerFactory.getLogger(RpcConsumerHandler.class);
    //多线程环境下网络连接通道的可见性和禁止指令重排序
    //重排序：jvm或cpu优化导致顺序错乱
    private volatile Channel channel;
    private SocketAddress remotePeer;
    private Map<Long,RpcProtocol<RpcResponse>> pendingResponse = new ConcurrentHashMap<>();

    public Channel getChannel() {
        return channel;
    }

    public SocketAddress getRemotePeer() {
        return remotePeer;
    }

    /**
     * 当Channel注册到Eventloop后触发
     * 保存Channel实例以便后续发送请求使用
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    /**
     * 连接建立后触发
     * 记录当前连接的服务端地址 IP+Port
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.remotePeer = this.channel.remoteAddress();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcResponse> protocol) throws Exception {
        if (protocol == null) {
            return;
        }
        logger.info("服务消费者接收到的数据===>>>{}", JSONObject.toJSONString(protocol));
        RpcHeader header = protocol.getHeader();
        long requestId = header.getRequestId();
        pendingResponse.put(requestId,protocol);
    }

    /**
     * 服务消费者向服务提供者发送请求
     */
    public Object sendRequest(RpcProtocol<RpcRequest> protocol){
        logger.info("服务消费者发送的数据===>>>{}", JSONObject.toJSONString(protocol));
        channel.writeAndFlush(protocol).addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                logger.error("Send request failed", future.cause());
            }
        });
        RpcHeader header = protocol.getHeader();
        long requestId = header.getRequestId();
        while (true){
            RpcProtocol<RpcResponse> responseRpcProtocol = pendingResponse.remove(requestId);
            if (responseRpcProtocol != null){
                return responseRpcProtocol.getBody().getResult();
            }
        }
    }

    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }
}

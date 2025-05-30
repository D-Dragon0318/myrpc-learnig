package io.spridra.rpc.consumer.common.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.spridra.rpc.consumer.common.context.RpcContext;
import io.spridra.rpc.protocol.RpcProtocol;
import io.spridra.rpc.protocol.header.RpcHeader;
import io.spridra.rpc.protocol.request.RpcRequest;
import io.spridra.rpc.protocol.response.RpcResponse;
import io.spridra.rpc.proxy.api.future.RPCFuture;
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
    private Map<Long, RPCFuture> pendingRPC = new ConcurrentHashMap<>();

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
        // pendingResponse.put(requestId,protocol);
        RPCFuture rpcFuture = pendingRPC.remove(requestId);
        if (rpcFuture != null){
            logger.info("开始执行rpcFuture.done");
            rpcFuture.done(protocol);
        }
    }

    public RPCFuture sendRequest(RpcProtocol<RpcRequest> protocol,boolean async,boolean oneway){
        logger.info("服务消费者发送的数据===>>>{}", JSONObject.toJSONString(protocol));
        return oneway?this.sendRequestOneway(protocol) : async ? sendRequestAsync(protocol) : sendRequestSync(protocol);
    }

    /**
     * 服务消费者向服务提供者发送同步请求
     */
    public RPCFuture sendRequestSync(RpcProtocol<RpcRequest> protocol){
        logger.info("同步请求==>>>");
        // channel.writeAndFlush(protocol).addListener((ChannelFutureListener) future -> {
        //     if (!future.isSuccess()) {
        //         logger.error("Send request failed", future.cause());
        //     }
        // });
        // RpcHeader header = protocol.getHeader();
        // long requestId = header.getRequestId();
        // while (true){
        //     RpcProtocol<RpcResponse> responseRpcProtocol = pendingResponse.remove(requestId);
        //     if (responseRpcProtocol != null){
        //         return responseRpcProtocol.getBody().getResult();
        //     }
        // }
        RPCFuture rpcFuture = this.getRpcFuture(protocol);
        channel.writeAndFlush(protocol);
        return rpcFuture;
    }

    /**
     * 异步调用
     * 服务消费者发送数据后，会通过RpcContext获取RPCFuture，进而获取最终结果
     */
    private RPCFuture sendRequestAsync(RpcProtocol<RpcRequest> protocol) {
        logger.info("异步请求==>>>");
        RPCFuture rpcFuture = this.getRpcFuture(protocol);
        //如果是异步调用，则将RPCFuture放入RpcContext
        RpcContext.getContext().setRPCFuture(rpcFuture);
        channel.writeAndFlush(protocol);
        return null;
    }

    /**
     * 单向调用，不关心结果
     */
    private RPCFuture sendRequestOneway(RpcProtocol<RpcRequest> protocol) {
        channel.writeAndFlush(protocol);
        return null;
    }

    private RPCFuture getRpcFuture(RpcProtocol<RpcRequest> protocol){
        RPCFuture rpcFuture = new RPCFuture(protocol);
        RpcHeader header = protocol.getHeader();
        long requestId = header.getRequestId();
        pendingRPC.put(requestId,rpcFuture);
        return rpcFuture;
    }
    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }
}

package io.spridra.rpc.common.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.spridra.rpc.common.helper.RpcServiceHelper;
import io.spridra.rpc.common.threadpool.ServerThreadPool;
import io.spridra.rpc.protocol.RpcProtocol;
import io.spridra.rpc.protocol.enumeration.RpcStatus;
import io.spridra.rpc.protocol.enumeration.RpcType;
import io.spridra.rpc.protocol.header.RpcHeader;
import com.alibaba.fastjson.JSONObject;
import io.spridra.rpc.protocol.request.RpcRequest;
import io.spridra.rpc.protocol.response.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-27 11:08
 * @Describe: 消息收发处理器
 * @Version: 1.0
 */

public class RpcProviderHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {
    private final Logger logger = LoggerFactory.getLogger(RpcProviderHandler.class);

    private final Map<String, Object> handlerMap;

    public RpcProviderHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> protocol) throws Exception {
        ServerThreadPool.submit(() -> {
            logger.info("RPC提供者收到的数据为====>>> {}", JSONObject.toJSONString(protocol));
            logger.info("handlerMap中存放的数据如下所示：");
            for (Map.Entry<String, Object> entry : handlerMap.entrySet()) {
                logger.info("{} === {}", entry.getKey(), entry.getValue());
            }
            // 直接返回数据
            // ctx.writeAndFlush(o);
            RpcHeader header = protocol.getHeader();
            // 将header中的消息类型设置为响应类型的消息
            header.setMsgType((byte) RpcType.RESPONSE.getType());
            RpcRequest request = protocol.getBody();
            logger.debug("Receive request {}", header.getRequestId());
            //构建响应协议
            RpcProtocol<RpcResponse> responseRpcProtocol = new RpcProtocol<RpcResponse>();
            // 构建响应协议数据
            RpcResponse response = new RpcResponse();
            try {
                Object result = handle(request);
                response.setResult(result);
                response.setAsync(request.getAsync());
                response.setOneway(request.getOneway());
                header.setStatus((byte) RpcStatus.SUCCESS.getCode());
            } catch (Throwable e) {
                response.setError(e.toString());
                header.setStatus((byte) RpcStatus.FAIL.getCode());
                logger.error("RPC provider handle request error", e);
            }
            responseRpcProtocol.setHeader(header);
            responseRpcProtocol.setBody(response);
            ctx.writeAndFlush(responseRpcProtocol).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception{
                    logger.debug("Send response for request {}", header.getRequestId());
                }
            });

        });
    }
    private Object handle(RpcRequest request) throws Throwable {
        //构建一个唯一的服务标识 key，通常格式为：className#version#group
        String serviceKey = RpcServiceHelper.buildServiceKey(request.getClassName(), request.getVersion(), request.getGroup());
        //获取服务实例
        Object serviceBean = handlerMap.get(serviceKey);
        if (serviceBean == null) {
            throw new RuntimeException(String.format("service not exist: %s:%s", request.getClassName(), request.getMethodName()));
        }
        //获取服务类
        Class<?> serviceClass = serviceBean.getClass();
        //获取方法名
        String methodName = request.getMethodName();
        //获取参数类型
        Class<?>[] parameterTypes = request.getParameterTypes();
        //获取参数
        Object[] parameters = request.getParameters();

        logger.debug("服务类名：{}",serviceClass.getName());
        logger.debug("服务方法名：{}",methodName);

        if (parameterTypes != null ){
            //打印每个参数类型
            for (Class<?> parameterType : parameterTypes) {
                logger.debug(parameterType.getName());
            }
        }

        if (parameters != null ){
            //打印每个参数
            for (Object parameter : parameters) {
                logger.debug(parameter.toString());
            }
        }
        return invokeMethod(serviceBean, serviceClass, methodName, parameterTypes, parameters);
    }
    //TODO 目前使用JDK动态代理方式，此处埋点
    private Object invokeMethod(Object serviceBean, Class<?> serviceClass, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Throwable {
        //通过方法名和参数类型获取方法
        Method method = serviceClass.getMethod(methodName, parameterTypes);
        //设置方法可访问
        method.setAccessible(true);
        //执行方法
        return method.invoke(serviceBean, parameters);
    }

    /**
     * Netty的异常捕获方法
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("server caught exception", cause);
        ctx.close();
    }
}

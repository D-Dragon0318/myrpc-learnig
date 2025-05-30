package io.spridra.rpc.consumer.common.context;


import io.spridra.rpc.consumer.common.RpcConsumer;
import io.spridra.rpc.proxy.api.future.RPCFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-29 16:23
 * @Describe: Rpc上下文
 * @Version: 1.0
 */

public class RpcContext {
    private final Logger logger = LoggerFactory.getLogger(RpcContext.class);
    private RpcContext(){
    }

    /**
     * RpcContext实例
     */
    private static final RpcContext AGENT = new RpcContext();

    /**
     * 存放RPCFuture的InheritableThreadLocal
     * 需要跨线程共享一些上下文但又不希望全局暴露的数据
     */
    private static final InheritableThreadLocal<RPCFuture> RPC_FUTURE_INHERITABLE_THREAD_LOCAL = new InheritableThreadLocal<>();

    /**
     * 获取上下文
     * @return RPC服务的上下文信息
     */
    public static RpcContext getContext(){

        return AGENT;
    }

    /**
     * 将RPCFuture保存到线程的上下文
     * @param rpcFuture
     */
    public void setRPCFuture(RPCFuture rpcFuture){
        logger.info("RPCFuture被保存了，setRPCFuture: {}", rpcFuture);
        RPC_FUTURE_INHERITABLE_THREAD_LOCAL.set(rpcFuture);
    }

    /**
     * 获取RPCFuture
     */
    public RPCFuture getRPCFuture(){
        logger.info("RPCFuture被获取了，getRPCFuture: {}", RPC_FUTURE_INHERITABLE_THREAD_LOCAL.get());
        return RPC_FUTURE_INHERITABLE_THREAD_LOCAL.get();
    }

    /**
     * 移除RPCFuture
     */
    public void removeRPCFuture(){
        logger.info("RPCFuture被移除了，removeRPCFuture: {}", RPC_FUTURE_INHERITABLE_THREAD_LOCAL.get());
        RPC_FUTURE_INHERITABLE_THREAD_LOCAL.remove();
    }
}

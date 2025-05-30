package io.spridra.rpc.proxy.api.async;

import io.spridra.rpc.proxy.api.future.RPCFuture;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-30 17:20
 * @Describe: 动态代理异步接口
 * @Version: 1.0
 */
public interface IAsyncObjectProxy {
    /**
     * 异步代理对象调用方法
     * @param funcName 方法名称
     * @param args 方法参数
     * @return 封装好的RPCFuture对象
     */
    RPCFuture call(String funcName, Object... args);
}

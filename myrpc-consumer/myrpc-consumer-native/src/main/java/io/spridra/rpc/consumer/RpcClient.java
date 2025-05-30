package io.spridra.rpc.consumer;

import io.spridra.rpc.consumer.common.RpcConsumer;
import io.spridra.rpc.proxy.api.async.IAsyncObjectProxy;
import io.spridra.rpc.proxy.api.object.ObjectProxy;
import io.spridra.rpc.proxy.jdk.JdkProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-30 10:28
 * @Describe: 服务消费者客户端
 * @Version: 1.0
 */

public class RpcClient {
    private final Logger logger = LoggerFactory.getLogger(RpcClient.class);
    /**
     * 服务版本
     */
    private String serviceVersion;
    /**
     * 服务分组
     */
    private String serviceGroup;
    /**
     * 序列化类型
     */
    private String serializationType;
    /**
     * 超时时间
     */
    private long timeout;

    /**
     * 是否异步调用
     */
    private boolean async;

    /**
     * 是否单向调用
     */
    private boolean oneway;

    /**
     * 构建客户端
     */
    public RpcClient(String serviceVersion, String serviceGroup, String serializationType, long timeout, boolean async, boolean oneway) {
        this.serviceVersion = serviceVersion;
        this.timeout = timeout;
        this.serviceGroup = serviceGroup;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
    }

    /**
     * 创建代理对象
     *
     * @param interfaceClass 接口类
     * @param <T>            接口类型
     * @return 代理对象
     */
    public <T> T create(Class<T> interfaceClass) {
        JdkProxyFactory<T> jdkProxyFactory = new JdkProxyFactory<T>(serviceVersion, serviceGroup, serializationType, timeout, RpcConsumer.getInstance(), async, oneway);
        return jdkProxyFactory.getProxy(interfaceClass);
    }

    /**
     * 创建异步代理对象
     */
    public <T> IAsyncObjectProxy createAsync(Class<T> interfaceClass) {
        return new ObjectProxy<T>(interfaceClass, serviceVersion, serviceGroup, serializationType, timeout, RpcConsumer.getInstance(), async, oneway);
    }

    public void shutdown() {
        RpcConsumer.getInstance().close();
    }
}

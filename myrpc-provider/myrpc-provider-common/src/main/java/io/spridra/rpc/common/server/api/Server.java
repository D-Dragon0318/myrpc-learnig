package io.spridra.rpc.common.server.api;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-27 11:12
 * @Describe: 启动myrpc框架服务提供者的核心接口
 * @Version: 1.0
 */

public interface Server {
    /**
     * 启动Netty服务
     */
    void startNettyServer();
}

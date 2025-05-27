package io.spridra.rpc.provider;

import io.spridra.rpc.common.scanner.RpcServiceScanner;
import io.spridra.rpc.common.server.base.BaseServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-27 11:46
 * @Describe: 以Java原生方式启动启动Rpc
 * @Version: 1.0
 */

public class RpcSingleServer extends BaseServer {

    private final Logger logger = LoggerFactory.getLogger(RpcSingleServer.class);

    public RpcSingleServer(String serverAddress, String scanPackage) {
        //调用父类构造方法
        super(serverAddress);
        try {
            this.handlerMap = RpcServiceScanner.doScannerWithRpcServiceAnnotationFilterAndRegistryService(host, port, scanPackage);
        } catch (Exception e) {
            logger.error("RPC Server init error", e);
        }
    }
}

package io.spridra.rpc.test.provider.single;

import io.spridra.rpc.provider.RpcSingleServer;
import org.junit.Test;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-27 14:48
 * @Describe:
 * @Version: 1.0
 */

public class RpcSingleServeTest {
    @Test
    public void startRpcSingleServer(){
        // RpcSingleServer singleServer = new RpcSingleServer("127.0.0.1:27880", "127.0.0.1:27880","127.0.0.1:2181", "zookeeper", "random","io.binghe.rpc.test", "asm", 3000, 6000, false, 10000, 16, 16, "print", 2, "strategy_default", false, 2, false, "counter", 100, 1000, "exception" /**direct/fallback/exception**/, true, "counter", 1, 5000, "print");
        RpcSingleServer singleServer = new RpcSingleServer("127.0.0.1:27880", "io.spridra.rpc.test");
        singleServer.startNettyServer();
    }
}

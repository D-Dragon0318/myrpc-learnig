package io.spridra.rpc.test.consumer;

import io.spridra.rpc.consumer.RpcClient;
import io.spridra.rpc.proxy.api.async.IAsyncObjectProxy;
import io.spridra.rpc.proxy.api.future.RPCFuture;
import io.spridra.rpc.test.api.DemoService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-30 10:52
 * @Describe:
 * @Version: 1.0
 */

public class RpcConsumerNativeTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcConsumerNativeTest.class);

    public static void main(String[] args){
        RpcClient rpcClient = new RpcClient("1.0.0", "spridra", "jdk", 3000, false, false);
        DemoService demoService = rpcClient.create(DemoService.class);
        String result = demoService.hello("spridra");
        LOGGER.info("返回的结果数据===>>> " + result);
        rpcClient.shutdown();
    }
    @Test
    public void testInterfaceRpc(){
        RpcClient rpcClient = new RpcClient("1.0.0", "spridra", "jdk", 3000, false, false);
        DemoService demoService = rpcClient.create(DemoService.class);
        String result = demoService.hello("spridra");
        LOGGER.info("同步调用返回的结果数据===>>> " + result);
        rpcClient.shutdown();
    }

    @Test
    public void testAsyncInterfaceRpc() throws Exception {
        RpcClient rpcClient = new RpcClient("1.0.0", "spridra", "jdk", 3000, true, false);
        IAsyncObjectProxy demoService = rpcClient.createAsync(DemoService.class);
        RPCFuture future = demoService.call("hello", "spridra");
        LOGGER.info("异步返回的结果数据===>>> " + future.get());
        rpcClient.shutdown();
    }
}

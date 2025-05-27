package io.spridra.rpc.test.scanner.consumer.service.impl;

import io.spridra.rpc.annotation.RpcReference;
import io.spridra.rpc.test.scanner.service.DemoService;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-27 09:46
 * @Describe:
 * @Version: 1.0
 */

public class ConsumerBusinessServiceImpl {
    @RpcReference(registryType = "zookeeper", registryAddress = "127.0.0.1:2181", version = "1.0.0", group = "spridra")
    private DemoService demoService;
}

package io.spridra.rpc.test.provider.service.impl;

import io.spridra.rpc.annotation.RpcService;
import io.spridra.rpc.test.api.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-27 14:41
 * @Describe:
 * @Version: 1.0
 */

@RpcService(interfaceClass = DemoService.class,
        interfaceClassName = "io.spridra.rpc.test.scanner.service.DemoService",
        version = "1.0.0",
        group = "spridra")
public class ProviderDemoServiceImpl implements DemoService {

    private final Logger logger = LoggerFactory.getLogger(ProviderDemoServiceImpl.class);
    @Override
    public String hello(String name) {
        logger.info("调用hello方法传入的参数为===>>>{}", name);
        return "hello " + name;
    }
}

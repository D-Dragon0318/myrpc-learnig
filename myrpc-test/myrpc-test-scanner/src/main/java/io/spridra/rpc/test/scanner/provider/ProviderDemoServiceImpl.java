package io.spridra.rpc.test.scanner.provider;

import io.spridra.rpc.annotation.RpcService;
import io.spridra.rpc.test.scanner.service.DemoService;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-27 09:42
 * @Describe:
 * @Version: 1.0
 */

@RpcService(interfaceClass = DemoService.class, interfaceClassName = "io.spridra.rpc.test.scanner.service.DemoService", version = "1.0.0", group = "spridra")
public class ProviderDemoServiceImpl implements DemoService {
}

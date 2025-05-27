package io.spridra.rpc.common.scanner.id;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-27 16:11
 * @Describe: 简易ID工厂类
 * @Version: 1.0
 */

public class IdFactory {
    //  请求ID生成器
    // 多次调用 第一次是1 后面每次加1，第二次是2
    /**
     * 生成的id单机唯一
     * 重启归零
     */
    private final static AtomicLong REQUEST_ID_GEN = new AtomicLong(0);

    public static Long getId() {
        return REQUEST_ID_GEN.incrementAndGet();
    }
}

package io.spridra.rpc.protocol.base;

import java.io.Serializable;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-27 15:43
 * @Describe: 基础信息类
 * @Version: 1.0
 */

public class RpcMessage implements Serializable {
    /**
     * 单向传输
     */
    private boolean oneway;
    /**
     * 异步传输
     */
    private boolean async;
}

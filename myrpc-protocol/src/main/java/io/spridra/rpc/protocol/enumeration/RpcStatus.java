package io.spridra.rpc.protocol.enumeration;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-27 16:07
 * @Describe: RPC服务状态
 * @Version: 1.0
 */

public enum RpcStatus {
    SUCCESS(0),
    FAIL(1);

    private final int code;

    RpcStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

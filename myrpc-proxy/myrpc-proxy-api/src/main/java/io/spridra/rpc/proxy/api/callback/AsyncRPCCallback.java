package io.spridra.rpc.proxy.api.callback;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-29 17:25
 * @Describe: 异步回调方法
 * @Version: 1.0
 */

public interface AsyncRPCCallback {
    /**
     * 成功后的回调方法
     */
    void onSuccess(Object result);

    /**
     * 异常的回调方法
     */
    void onException(Exception e);
}

package io.spridra.rpc.proxy.api.consumer;

import io.spridra.rpc.protocol.RpcProtocol;
import io.spridra.rpc.protocol.request.RpcRequest;
import io.spridra.rpc.proxy.api.future.RPCFuture;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-30 09:46
 * @Describe:
 * @Version: 1.0
 */
public interface Consumer {
    /**
     * 消费者发送 request 请求
     */
    RPCFuture sendRequest(RpcProtocol<RpcRequest> protocol) throws Exception;
}

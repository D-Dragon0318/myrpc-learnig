package io.spridra.rpc.protocol.response;

import io.spridra.rpc.protocol.base.RpcMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-27 15:47
 * @Describe: 响应消息类
 * @Version: 1.0
 */
@Data
public class RpcResponse extends RpcMessage {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 425335064405584525L;

    /**
     * 错误信息
     */
    private String error;

    /**
     * 返回结果
     */
    private Object result;
}

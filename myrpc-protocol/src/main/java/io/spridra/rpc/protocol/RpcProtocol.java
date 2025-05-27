package io.spridra.rpc.protocol;

import io.spridra.rpc.protocol.header.RpcHeader;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-27 16:01
 * @Describe: 协议类
 * @Version: 1.0
 */
@Data
public class RpcProtocol<T> implements Serializable {
    private static final long serialVersionUID = 292789485166173277L;

    /**
     * 消息头
     */
    private RpcHeader header;

    /**
     * 消息体
     */
    private T body;
}

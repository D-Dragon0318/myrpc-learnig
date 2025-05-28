package io.spridra.rpc.protocol.request;

import io.spridra.rpc.protocol.base.RpcMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: Spridra
 * @CreateTime: 2025-05-27 15:45
 * @Describe: 请求消息类
 * @Version: 1.0
 */
@Data
public class RpcRequest extends RpcMessage {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 5555776886650396129L;

    /**
     * 请求类名
     */
    private String className;

    /**
     * 请求方法名
     */
    private String methodName;

    /**
     * 请求参数类型
     */
    private Class<?>[] parameterTypes;

    /**
     * 请求参数
     */
    private Object[] parameters;

    /**
     * 服务版本号
     */
    private String version;

    /**
     * 服务分组
     */
    private String group;
}
